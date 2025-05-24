/*
 * Copyright 2024-2025 OmniOne.
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
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnione.did.sdk.datamodel.did.AttestedDidDoc;
import org.omnione.did.sdk.datamodel.did.SignedDidDoc;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.datamodel.vc.issue.ReturnEncVP;
import org.omnione.did.sdk.datamodel.token.WalletTokenData;
import org.omnione.did.sdk.datamodel.common.enums.WalletTokenPurpose;
import org.omnione.did.sdk.datamodel.token.WalletTokenSeed;
import org.omnione.did.sdk.datamodel.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.profile.IssueProfile;
import org.omnione.did.sdk.datamodel.profile.VerifyProfile;
import org.omnione.did.sdk.datamodel.util.MessageUtil;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;
import org.omnione.did.sdk.utility.CryptoUtils;
import org.omnione.did.sdk.utility.DataModels.CipherInfo;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DataModels.ec.EcUtils;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Encodings.Base16;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.wallet.walletservice.LockManager;
import org.omnione.did.sdk.wallet.walletservice.config.Constants;
import org.omnione.did.sdk.wallet.walletservice.db.Token;
import org.omnione.did.sdk.wallet.walletservice.db.User;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;
import org.omnione.did.sdk.core.common.SecureEncryptor;
import org.omnione.did.sdk.core.exception.WalletCoreException;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WalletTest {
    private WalletService mockWalletService;
    private WalletToken mockWalletToken;
    private LockManager mockLcokManager;
    private WalletApi testWalletApi;
    Token token = new Token();
    User user = new User();
    private String walletId;
    Context appContext;
    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mockWalletService = new WalletService(appContext, new WalletCore(appContext)) {
            @Override
            public void fetchCaInfo(String tasUrl) {
                String pkgName = "org.omnione.did.ca";
                Log.d("WalletTest","fetchCaInfo : " + pkgName);
            }

            @Override
            public void createDeviceDocument(String walletUrl, String tasUrl) throws WalletException, WalletCoreException, UtilityException, ExecutionException, InterruptedException {
                Log.d("WalletTest","createDeviceDocument");
                DIDDocument deviceDIDDoc = walletCore.createDeviceDIDDoc();
                String did = deviceDIDDoc.getId();
                List<String> keyIds = List.of(Constants.KEY_ID_ASSERT, Constants.KEY_ID_AUTH);
                WalletApi.isLock = false;
                DIDDocument ownerDIDDoc = (DIDDocument) addProofsToDocument(deviceDIDDoc, keyIds, did, Constants.DID_DOC_TYPE_DEVICE, "", false);
                Log.d("WalletTest", "ownerDIDDoc : " + ownerDIDDoc.toJson());
                String attDIDDoc = WalletTestData.TEST_ATTESTED_DID_DOC;
                AttestedDidDoc attestedDIDDoc = MessageUtil.deserialize(attDIDDoc, AttestedDidDoc.class);
                Log.d("WalletTest", "WalletAPi attDIDDoc wallet ID : " + attestedDIDDoc.getWalletId());
                walletId = attestedDIDDoc.getWalletId();
                walletCore.saveDocument(Constants.DID_DOC_TYPE_DEVICE);
                WalletApi.isLock = true;
            }

            @Override
            public boolean bindUser(){
                Log.d("WalletTest", "bindUser() TEST");

                String pii = token.pii;
                Log.d("WalletTest","bindUser pii : " + pii);
                user.pii = pii;
                Log.d("WalletTest","bindUser pii : " + user.pii);
                Log.d("WalletTest", "bind user success");

                return true;
            }
            @Override
            public boolean unbindUser(){
                return true;
            }

            @Override
            public CompletableFuture<String> requestRegisterUser(String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) {
                return CompletableFuture.completedFuture(txId);
            }

            public CompletableFuture<String> requestIssueVc(String tasUrl, String apiGateWayUrl, String serverToken, String refId, IssueProfile profile, DIDAuth signedDIDAuth, String txId) throws WalletCoreException, UtilityException {
                try {
                    String clientPrivKey = "mMIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgNdy+e2T694f0QbSd/gdLvmdgUjti+RR6wQY4F+kMYfCgCgYIKoZIzj0DAQehRANCAAT7qjXANGpEQoAuWp7+n00BbLUMp6tlmYGIP/RIdEJAMt3uDVWWb54UkVN5RsN4PZ7mnM5ZL9fgEgZTf2nXNpaK";
                    byte[] privateKey = MultibaseUtils.decode(clientPrivKey);
                    byte[] clientPrivateKey = EcUtils.convertCompressedPrivateKey(privateKey);
                    Log.d("WalletTest", "compressedPrivateKey : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, clientPrivateKey));
                    byte[] clientPublicKey = MultibaseUtils.decode("mAvuqNcA0akRCgC5anv6fTQFstQynq2WZgYg/9Eh0QkAy");

                    byte[] serverPubKey = MultibaseUtils.decode("mA+1jfCC06BtbLwUkkAAsiU46i4GWz17SWnaME4yx7g2c");
                    byte[] secretKey = CryptoUtils.generateSharedSecret(EcType.EC_TYPE.SECP256_R1, clientPrivateKey, serverPubKey);
                    byte[] serverNonce = MultibaseUtils.decode("mbRz+NJ7fEZEyFiAGIcfktQ");
                    byte[] e2eKey = WalletUtil.mergeSharedSecretAndNonce(secretKey, serverNonce, SymmetricCipherType.SYMMETRIC_CIPHER_TYPE.AES256CBC);

                    byte[] iv = MultibaseUtils.decode("mwZPbY3+4RBYwwzuLOM6AFA");
                    byte[] dec = CryptoUtils.decrypt(
                            MultibaseUtils.decode(WalletTestData.TEST_ENC_VC),
                            new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                            e2eKey,
                            iv);
                    String vc = new String(dec);
                    VerifiableCredential credential = MessageUtil.deserialize(vc, VerifiableCredential.class);
                    Log.d("WalletTest", "vc : " + credential.toJson());
                    walletCore.addCredentials(credential);
                    return CompletableFuture.completedFuture(credential.getId());
                } catch (InvalidKeySpecException | NoSuchAlgorithmException | WalletException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        mockWalletToken = new WalletToken(appContext,  new WalletCore(appContext)) {
            @Override
            public void verifyWalletToken(String hWalletToken, List<WalletTokenPurpose.WALLET_TOKEN_PURPOSE> purposeList) throws WalletException {
                Log.d("WalletTest","verifyWalletToken mock");
                String hWalletToken_db = token.hWalletToken;
                String validUntil_db = token.validUntil;
                String purpose_db = token.purpose;

                Log.d("WalletTest","verifyWalletToken 인가앱 hwallettoken: " + hWalletToken);
                Log.d("WalletTest","verifyWalletToken walletToken db: " + hWalletToken_db);
                Log.d("WalletTest","db: validUntil_db - " + validUntil_db + " / " + WalletTokenPurpose.WALLET_TOKEN_PURPOSE.fromValue(Integer.valueOf(purpose_db)).toString());

                if(!hWalletToken.equals(hWalletToken_db)){
                    Log.d("WalletTest","walletToken verification fail");
                    throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_TOKEN_FAIL);
                }
                if(!WalletUtil.checkDate(validUntil_db)){
                    Log.d("WalletTest","valid until verification fail");
                    throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_TOKEN_FAIL);
                }
                boolean isPurpose = false;
                for(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose : purposeList) {
                    if (purpose_db.equals(String.valueOf(purpose.getValue()))) {
                        isPurpose = true;
                        break;
                    }
                }
                // purpose verification
                if(!isPurpose)
                    throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_TOKEN_FAIL);
                Log.d("WalletTest","walletToken verification success");
                Log.d("WalletTest","valid until verification success");
                Log.d("WalletTest","purpose verification success");

            }

            @Override
            public WalletTokenSeed createWalletTokenSeed(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName, String userId) {
                WalletTokenSeed walletTokenSeed = new WalletTokenSeed();
                walletTokenSeed.setNonce("F2F5ED525DE7B45EBAFF5B325459A372C");
                walletTokenSeed.setPurpose(purpose);
                walletTokenSeed.setPkgName(pkgName);
                walletTokenSeed.setUserId(userId);
                Log.d("WalletTest","createWalletTokenSeed : " + walletTokenSeed.getNonce());
            return walletTokenSeed;
            }
            @Override
            public String createNonceForWalletToken(String apiGateWayUrl, WalletTokenData walletTokenData) {
                String resultNonce = "F9909CA2B700D1EC658098ECE03A97343";
                String hWalletToken = WalletTestData.hWalletToken[walletTokenData.getSeed().getPurpose().getValue() - 1];

                token.hWalletToken = hWalletToken;
                token.validUntil = walletTokenData.getSeed().getValidUntil();
                token.purpose = String.valueOf(walletTokenData.getSeed().getPurpose().getValue());
                token.pii = walletTokenData.getSha256_pii();
                return resultNonce;
            }
        };

        mockLcokManager = new LockManager(appContext) {
            @Override
            public boolean registerLock(String passCode, boolean isLock) throws UtilityException, WalletCoreException {
                if(isLock){

                    byte[] pwd = passCode.getBytes();
                    Log.d("WalletTest","pwd : " + passCode);

                    byte[] cek = CryptoUtils.generateNonce(32);
                    Log.d("WalletTest","cek : " + Base16.toHex(cek) + " / " + cek.length);

                    int dk_keySize = 48;
                    int iterator = 2048;
                    byte[] salt = CryptoUtils.generateNonce(20);

                    salt = Base16.toBytes("ef81d6d71d43fc53a18fddb9600f03e1532a8dd0");
                    Log.d("WalletTest","salt : " + Base16.toHex(salt) + " / " + salt.length);

                    byte[] kek = CryptoUtils.pbkdf2(pwd, salt, iterator, dk_keySize);
                    Log.d("WalletTest","kek : " + Base16.toHex(kek) + " / " + kek.length);

                    byte[] key = Arrays.copyOfRange(kek, 0, 32);
                    byte[] iv = Arrays.copyOfRange(kek, 32, kek.length);
                    byte[] encCek = CryptoUtils.encrypt(
                            cek,
                            new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                            key,
                            iv);
                    Log.d("WalletTest","encCek : " + Base16.toHex(encCek) + " / " + encCek.length);

                    byte[] finalEncCek = SecureEncryptor.encrypt(encCek, appContext); //"finalEncCek" AES encrypt
                    Log.d("WalletTest","finalEncCek : " + Base16.toHex(finalEncCek) + " / " + finalEncCek.length);
                    user.fek = Base16.toHex(finalEncCek);

                } else {
                    user.fek = "";
                }
                WalletApi.isLock = false;
                return true;
            }

            @Override
            public void authenticateLock(String passCode) throws WalletCoreException, UtilityException {
                byte[] pwd = passCode.getBytes();
                Log.d("WalletTest", "pwd : " + passCode);

                byte[] finalEncCek = Base16.toBytes(user.fek);
                Log.d("WalletTest", "Load finalEncCek : " + user.fek);

                byte[] encCek = SecureEncryptor.decrypt(finalEncCek);
                Log.d("WalletTest", "dec encCek : " + Base16.toHex(encCek) + " / " + encCek.length);

                int dk_keySize = 48;
                int iterator = 2048;
                byte[] salt = CryptoUtils.generateNonce(20);
                // salt
                salt = Base16.toBytes("ef81d6d71d43fc53a18fddb9600f03e1532a8dd0");
                Log.d("WalletTest", "salt : " + Base16.toHex(salt) + " / " + salt.length);

                byte[] kek = CryptoUtils.pbkdf2(pwd, salt, iterator, dk_keySize);
                Log.d("WalletTest", "kek : " + Base16.toHex(kek) + " / " + kek.length);

                byte[] key = Arrays.copyOfRange(kek, 0, 32);
                byte[] iv = Arrays.copyOfRange(kek, 32, kek.length);
                byte[] cek = CryptoUtils.decrypt(
                        encCek,
                        new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                        key,
                        iv);
                Log.d("WalletTest", "dec cek : " + Base16.toHex(cek) + " / " + cek.length);

                // unlock status
                WalletApi.isLock = false;

            }
        };
        testWalletApi = WalletApi.getInstance(appContext);

        Field walletServiceField = WalletApi.class.getDeclaredField("walletService");
        walletServiceField.setAccessible(true);
        walletServiceField.set(testWalletApi, mockWalletService);

        Field walletTokenField = WalletApi.class.getDeclaredField("walletToken");
        walletTokenField.setAccessible(true);
        walletTokenField.set(testWalletApi, mockWalletToken);

        Field lockManagerField = WalletApi.class.getDeclaredField("lockManager");
        lockManagerField.setAccessible(true);
        lockManagerField.set(testWalletApi, mockLcokManager);
    }
    @Test
    public void walletTest() throws Exception {
        Log.i("WalletTest", "===========================Create Wallet =========================");
        testWalletApi.deleteWallet();
        Assert.assertTrue(testWalletApi.createWallet("wallet","tas"));
        testWalletApi.createWalletTokenSeed(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.PERSONALIZE, "org.omnione.did.ca","fbc20bee");
        Log.i("WalletTest", "===========================================================");

        Log.i("WalletTest", "===========================Bind User =========================");
        WalletTokenData walletTokenData_PERSONALIZE = MessageUtil.deserialize(WalletTestData.WALLET_TOKEN_DATA_PERSONALIZE, WalletTokenData.class);
        String resultNonce = testWalletApi.createNonceForWalletToken("apiGateWay", walletTokenData_PERSONALIZE);
        String walletToken = walletTokenData_PERSONALIZE.toJson() + resultNonce;
        String hWalletToken = Base16.toHex(DigestUtils.getDigest(walletToken.getBytes(), DigestEnum.DIGEST_ENUM.SHA_256));
        Assert.assertTrue(testWalletApi.bindUser(hWalletToken));
        Log.i("WalletTest", "===========================================================");

        Log.i("WalletTest", "===========================Wallet Lock / Unlock =========================");
        WalletTokenData walletTokenData_CONFIG_LOCK = MessageUtil.deserialize(WalletTestData.WALLET_TOKEN_DATA_CONFIGLOCK, WalletTokenData.class);
        resultNonce = testWalletApi.createNonceForWalletToken("apiGateWay", walletTokenData_CONFIG_LOCK);
        walletToken = walletTokenData_CONFIG_LOCK.toJson() + resultNonce;
        hWalletToken = Base16.toHex(DigestUtils.getDigest(walletToken.getBytes(), DigestEnum.DIGEST_ENUM.SHA_256));
        Assert.assertTrue(testWalletApi.registerLock(hWalletToken,WalletTestData.TEST_PASSCODE, true));
        Assert.assertFalse(WalletApi.isLock);
        testWalletApi.authenticateLock(WalletTestData.TEST_PASSCODE);
        Assert.assertFalse(WalletApi.isLock);
        Log.i("WalletTest", "===========================================================");

        Log.i("WalletTest", "===========================user reg =========================");
        WalletTokenData walletTokenData_CreateDID = MessageUtil.deserialize(WalletTestData.WALLET_TOKEN_DATA_CREATE_DID, WalletTokenData.class);
        resultNonce = testWalletApi.createNonceForWalletToken("apiGateWay", walletTokenData_CreateDID);
        walletToken = walletTokenData_CreateDID.toJson() + resultNonce;
        hWalletToken = Base16.toHex(DigestUtils.getDigest(walletToken.getBytes(), DigestEnum.DIGEST_ENUM.SHA_256));
        // PIN key generation
        testWalletApi.generateKeyPair(hWalletToken, WalletTestData.TEST_PASSCODE);

        // holder did doc generation
        DIDDocument holderDIDDoc = testWalletApi.createHolderDIDDoc(hWalletToken);
        Log.d("WalletTest", "holder did doc : " + holderDIDDoc.toJson());
        // type 1 : device key , 2 : holder key
        DIDDocument ownerDIDDoc = (DIDDocument) testWalletApi.addProofsToDocument(holderDIDDoc, List.of("pin"), holderDIDDoc.getId(), 2, WalletTestData.TEST_PASSCODE, false);
        Log.d("WalletTest", "ownerDIDDoc : " + ownerDIDDoc.toJson());

        // signed DID Doc generation
        SignedDidDoc signedDidDoc = testWalletApi.createSignedDIDDoc(ownerDIDDoc);
        Log.d("WalletTest", "signedDidDoc : " + signedDidDoc.toJson());
        Log.d("WalletTest", "requestRegisterUser : " + testWalletApi.requestRegisterUser(hWalletToken, "tas", WalletTestData.TEST_TX_ID, WalletTestData.TEST_SERVER_TOKEN, signedDidDoc).get());
        Log.i("WalletTest", "===========================================================");


        Log.i("WalletTest", "===========================VC issue =========================");
        WalletTokenData walletTokenData_IssueVC = MessageUtil.deserialize(WalletTestData.WALLET_TOKEN_DATA_ISSUE_VC, WalletTokenData.class);
        resultNonce = testWalletApi.createNonceForWalletToken("apiGateWay", walletTokenData_IssueVC);
        walletToken = walletTokenData_IssueVC.toJson() + resultNonce;
        hWalletToken = Base16.toHex(DigestUtils.getDigest(walletToken.getBytes(), DigestEnum.DIGEST_ENUM.SHA_256));

        IssueProfile issueProfile = MessageUtil.deserialize(WalletTestData.TEST_ISSUE_PROFILE, IssueProfile.class);
        DIDAuth signedDidAuth = testWalletApi.getSignedDIDAuth(WalletTestData.TEST_AUTH_NONCE, WalletTestData.TEST_PASSCODE);
        String vcId = testWalletApi.requestIssueVc(
                hWalletToken,
                "tas",
                "apiGateWay",
                WalletTestData.TEST_SERVER_TOKEN,
                WalletTestData.TEST_REF_ID,
                issueProfile,
                signedDidAuth,
                WalletTestData.TEST_TX_ID, null, null
                ).get();
        Log.d("WalletTest", "vc ID : " + vcId);
        Log.i("WalletTest", "===========================================================");
        Log.i("WalletTest", "===========================VP submit =========================");
        WalletTokenData walletTokenData_PresentVP = MessageUtil.deserialize(WalletTestData.WALLET_TOKEN_DATA_PRESENT_VP, WalletTokenData.class);
        resultNonce = testWalletApi.createNonceForWalletToken("apiGateWay", walletTokenData_PresentVP);
        walletToken = walletTokenData_PresentVP.toJson() + resultNonce;
        hWalletToken = Base16.toHex(DigestUtils.getDigest(walletToken.getBytes(), DigestEnum.DIGEST_ENUM.SHA_256));

        VerifyProfile vpProfile = MessageUtil.deserialize(WalletTestData.TEST_VERIFY_PROFILE, VerifyProfile.class);
        List<String> claimCode = vpProfile.getProfile().filter.getCredentialSchemas().get(0).requiredClaims;
        ReturnEncVP returnEncVP = testWalletApi.createEncVp(
                hWalletToken,
                vcId,
                claimCode,
                vpProfile.getProfile().process.reqE2e,
                WalletTestData.TEST_PASSCODE,
                vpProfile.getProfile().process.verifierNonce,
                vpProfile.getProfile().process.authType
        );
        Log.d("WalletTest", "enc VP : " + returnEncVP.getEncVp());
        Log.i("WalletTest", "===========================================================");
    }
}
