/*
 * Copyright 2026 OmniOne.
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

package org.omnione.did.sdk.core.oid4vc.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.oid4vc.AuthorizationRequest;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OID4VPResponseUtilTest {

    private static AuthorizationRequest request(String responseMode) {
        AuthorizationRequest req = new AuthorizationRequest();
        req.setState("state-1");
        req.setResponseMode(responseMode);
        return req;
    }

    private static Map<String, Object> vpToken() {
        Map<String, Object> vpToken = new LinkedHashMap<>();
        vpToken.put("q1", Collections.singletonList("token"));
        return vpToken;
    }

    private static String encode(String responseMode) throws Exception {
        return new String(OID4VPResponseUtil.encodeResponseBody(request(responseMode), vpToken()),
                StandardCharsets.UTF_8);
    }

    private static void assertUnsupported(String responseMode) {
        try {
            encode(responseMode);
            fail("expected 06509 for response_mode '" + responseMode + "'");
        } catch (Exception e) {
            assertTrue(e instanceof WalletCoreException);
            WalletCoreException wce = (WalletCoreException) e;
            assertEquals(WalletCoreErrorCode.ERR_CODE_OID4VP_UNSUPPORTED_RESPONSE_MODE.getCode(),
                    wce.getErrorCode());
            assertTrue(wce.getErrMsg().endsWith(responseMode));
        }
    }

    @Test
    public void directPostIsSentAsAClearFormBody() throws Exception {
        String body = encode("direct_post");
        assertTrue(body.startsWith("vp_token="));
        assertTrue(body.endsWith("&state=state-1"));
    }

    @Test
    public void absentResponseModeKeepsTheClearDirectPostBody() throws Exception {
        assertTrue(encode(null).startsWith("vp_token="));
    }

    @Test
    public void directPostJwtStillTakesTheEncryptionPath() {
        try {
            encode("direct_post.jwt");
            fail("expected the encryption path to demand a verifier key");
        } catch (Exception e) {
            assertTrue(e instanceof WalletCoreException);
            assertEquals(WalletCoreErrorCode.ERR_CODE_OID4VP_MISSING_VERIFIER_ENCRYPTION_KEY.getCode(),
                    ((WalletCoreException) e).getErrorCode());
        }
    }

    @Test
    public void everyOtherResponseModeIsRejectedInsteadOfSilentlySentInTheClear() {
        for (String mode : List.of("fragment", "query", "direct_post.JWT", "Direct_post",
                "direct_post ", "", "direct_post.jwtx")) {
            assertUnsupported(mode);
        }
    }
}
