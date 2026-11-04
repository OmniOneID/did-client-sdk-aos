/*
 * Copyright 2024-2026 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omnione.did.sdk.core.api;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnione.did.sdk.datamodel.common.enums.WalletTokenPurpose;
import org.omnione.did.sdk.wallet.walletservice.db.DBManager;
import org.omnione.did.sdk.wallet.walletservice.db.Preference;
import org.omnione.did.sdk.wallet.walletservice.db.Token;
import org.omnione.did.sdk.wallet.walletservice.db.TokenDao;
import org.omnione.did.sdk.wallet.walletservice.db.User;
import org.omnione.did.sdk.wallet.walletservice.db.UserDao;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@RunWith(AndroidJUnit4.class)
public class WalletTokenTest {

    private static final String PKG_NAME = "org.omnione.did.wallet.test";

    private Context context;
    private WalletToken walletToken;
    private TokenDao tokenDao;
    private UserDao userDao;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        walletToken = new WalletToken(context, new WalletCore());
        DBManager walletDB = DBManager.getDatabases(context);
        tokenDao = walletDB.tokenDao();
        userDao = walletDB.userDao();
        Preference.saveWalletId(context, "walletId");
        userDao.deleteAll();
        tokenDao.deleteAll();
    }

    @After
    public void tearDown() {
        userDao.deleteAll();
        tokenDao.deleteAll();
    }

    private void personalize() {
        userDao.insertAll(new User("sha256_pii"));
    }

    private interface ThrowingBlock {
        void run() throws Exception;
    }

    private void assertCode(WalletErrorCode expected, ThrowingBlock block) {
        String expectedPrefix = "[" + expected.getWalletCoreCode() + expected.getFeature()
                + String.format(Locale.US, "%03d", expected.getCode()) + "]";
        try {
            block.run();
            Assert.fail("expected " + expectedPrefix + " but nothing was thrown");
        } catch (Exception e) {
            Assert.assertNotNull("exception carried no message", e.getMessage());
            Assert.assertTrue("expected " + expectedPrefix + " but got " + e.getMessage(),
                    e.getMessage().startsWith(expectedPrefix));
        }
    }

    @Test
    public void testEmptyPkgNameIsRejected() {
        personalize();
        assertCode(WalletErrorCode.ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL,
                () -> walletToken.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, ""));
    }

    @Test
    public void testPurposeOutsideWhitelistIsRejected() {
        personalize();
        assertCode(WalletErrorCode.ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL,
                () -> walletToken.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.REMOVE_VC, PKG_NAME));
        Assert.assertEquals("a rejected purpose must not leave a token behind", 0, tokenDao.getAll().size());
    }

    @Test
    public void testNotPersonalizedIsRejected() {
        assertCode(WalletErrorCode.ERR_CODE_WALLET_NOT_PERSONALIZED,
                () -> walletToken.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, PKG_NAME));
    }

    @Test
    public void testIssuedTokenVerifiesForItsPurpose() throws Exception {
        personalize();
        String token = walletToken.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, PKG_NAME);

        Assert.assertEquals(64, token.length());
        Assert.assertTrue("token must be lowercase hex with no multibase prefix", token.matches("^[0-9a-f]+$"));
        walletToken.verifyWalletToken(token,
                Collections.singletonList(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC));

        Assert.assertEquals(1, tokenDao.getAll().size());
        Token stored = tokenDao.getAll().get(0);
        Assert.assertEquals(token, stored.hWalletToken);
        Assert.assertEquals(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC.toString(), stored.purpose);
        Assert.assertEquals("", stored.nonce);
        Assert.assertEquals("sha256_pii", stored.pii);
        Assert.assertEquals(PKG_NAME, stored.pkgName);
        Assert.assertEquals("walletId", stored.walletId);
        long span = parseUtc(stored.validUntil) - parseUtc(stored.createDate);
        Assert.assertTrue("validUntil must sit 30 minutes past createDate, was " + span + "ms",
                span <= 30L * 60L * 1000L && span > 30L * 60L * 1000L - 2000L);
    }

    @Test
    public void testIssuedTokenIsRejectedForAnotherPurpose() throws Exception {
        personalize();
        String token = walletToken.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, PKG_NAME);

        Assert.assertEquals(1, tokenDao.getAll().size());
        assertCode(WalletErrorCode.ERR_CODE_WALLET_VERIFY_TOKEN_FAIL,
                () -> walletToken.verifyWalletToken(token,
                        Collections.singletonList(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.REMOVE_VC)));
    }

    @Test
    public void testNewTokenInvalidatesPreviousOne() throws Exception {
        personalize();
        String first = walletToken.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, PKG_NAME);

        tokenDao.deleteAll();
        tokenDao.insertAll(onlineToken("online", WalletTokenPurpose.WALLET_TOKEN_PURPOSE.ISSUE_VC));

        Assert.assertEquals(1, tokenDao.getAll().size());
        assertCode(WalletErrorCode.ERR_CODE_WALLET_VERIFY_TOKEN_FAIL,
                () -> walletToken.verifyWalletToken(first,
                        Collections.singletonList(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC)));

        String second = walletToken.createLocalWalletToken(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, PKG_NAME);

        Assert.assertEquals("a local issue must leave exactly one token row", 1, tokenDao.getAll().size());
        assertCode(WalletErrorCode.ERR_CODE_WALLET_VERIFY_TOKEN_FAIL,
                () -> walletToken.verifyWalletToken("online",
                        Collections.singletonList(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.ISSUE_VC)));
        walletToken.verifyWalletToken(second,
                Collections.singletonList(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC));
    }

    @Test
    public void testEveryWhitelistedPurposeIssues() throws Exception {
        personalize();
        for (WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose : WalletToken.localTokenPurposes) {
            String token = walletToken.createLocalWalletToken(purpose, PKG_NAME);
            walletToken.verifyWalletToken(token, Collections.singletonList(purpose));
        }
        Assert.assertEquals(4, WalletToken.localTokenPurposes.size());
        Assert.assertEquals(Arrays.asList(
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.DETAIL_VC,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.PRESENT_VP,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC_AND_PRESENT_VP),
                WalletToken.localTokenPurposes);
    }

    private Token onlineToken(String hWalletToken, WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose) {
        Token token = new Token();
        token.idx = WalletUtil.genId();
        token.walletId = "walletId";
        token.validUntil = WalletUtil.createValidUntil(30);
        token.purpose = purpose.toString();
        token.pii = "sha256_pii";
        token.nonce = "nonce";
        token.pkgName = PKG_NAME;
        token.hWalletToken = hWalletToken;
        token.createDate = WalletUtil.getDate();
        return token;
    }

    private long parseUtc(String value) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = sdf.parse(value);
        Assert.assertNotNull("unparseable date: " + value, date);
        return date.getTime();
    }
}
