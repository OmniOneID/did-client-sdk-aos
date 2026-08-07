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

import com.google.gson.JsonObject;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.crypto.VPResponseEncryptor;
import org.omnione.did.sdk.datamodel.oid4vc.AuthorizationRequest;
import org.omnione.did.sdk.datamodel.oid4vc.ClientMetadata;
import org.omnione.did.sdk.datamodel.oid4vc.EncryptedResponseSubmission;
import org.omnione.did.sdk.datamodel.oid4vc.VPTokenSubmission;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public final class OID4VPResponseUtil {

    private static final String RESPONSE_MODE_DIRECT_POST_JWT = "direct_post.jwt";
    private static final String ENC_A256GCM = "A256GCM";
    private static final String ENC_A128GCM = "A128GCM";

    private OID4VPResponseUtil() {}

    public static byte[] encodeResponseBody(AuthorizationRequest req, Map<String, Object> vpToken)
            throws WalletCoreException {
        String state = req.getState();
        if (RESPONSE_MODE_DIRECT_POST_JWT.equals(req.getResponseMode())) {
            ClientMetadata cm = req.getClientMetadata();
            String enc = selectEnc(cm);
            JsonObject recipientJwk = selectRecipientKey(cm);
            String alg = recipientJwk.has("alg") ? recipientJwk.get("alg").getAsString() : null;

            String payload = new String(new VPTokenSubmission(vpToken, state).toJsonData(), StandardCharsets.UTF_8);
            String jwe = VPResponseEncryptor.encrypt(payload, recipientJwk, alg, enc);
            return new EncryptedResponseSubmission(jwe).toFormData().getBytes(StandardCharsets.UTF_8);
        }

        return new VPTokenSubmission(vpToken, state).toFormData().getBytes(StandardCharsets.UTF_8);
    }

    private static String selectEnc(ClientMetadata cm) throws WalletCoreException {
        List<String> supported = (cm != null) ? cm.getEncryptedResponseEncValuesSupported() : null;
        if (supported == null || supported.isEmpty()) {
            return ENC_A256GCM;
        }
        if (supported.contains(ENC_A256GCM)) {
            return ENC_A256GCM;
        }
        if (supported.contains(ENC_A128GCM)) {
            return ENC_A128GCM;
        }
        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VP_UNSUPPORTED_RESPONSE_ENCRYPTION,
                "enc: " + supported);
    }

    private static JsonObject selectRecipientKey(ClientMetadata cm) throws WalletCoreException {
        if (cm == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VP_MISSING_VERIFIER_ENCRYPTION_KEY);
        }
        JsonObject fallback = null;
        for (JsonObject jwk : cm.jwkKeys()) {
            if (!isEcP256(jwk)) {
                continue;
            }
            if (jwk.has("use") && "enc".equals(jwk.get("use").getAsString())) {
                return jwk;
            }
            if (fallback == null) {
                fallback = jwk;
            }
        }
        if (fallback == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VP_MISSING_VERIFIER_ENCRYPTION_KEY);
        }
        return fallback;
    }

    private static boolean isEcP256(JsonObject jwk) {
        return jwk.has("kty") && "EC".equals(jwk.get("kty").getAsString())
                && jwk.has("crv") && "P-256".equals(jwk.get("crv").getAsString());
    }
}
