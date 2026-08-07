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

package org.omnione.did.sdk.core.oid4vc.crypto;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.jwk.ECKey;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;

public final class VPResponseEncryptor {

    private VPResponseEncryptor() {}

    @NonNull
    public static String encrypt(@NonNull String plaintextJson,
                                 @NonNull JsonObject recipientJwk,
                                 String alg,
                                 @NonNull String enc) throws WalletCoreException {
        if (alg != null && !alg.equals(JWEAlgorithm.ECDH_ES.getName())) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_ALGORITHM_JWE,
                    "alg: " + alg + " (only ECDH-ES is supported)");
        }

        ECKey recipientKey;
        try {
            recipientKey = ECKey.parse(recipientJwk.toString());
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_JWE_KEY,
                    "Invalid verifier encryption key: " + e.getMessage());
        }

        EncryptionMethod encMethod = EncryptionMethod.parse(enc);
        try {
            JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.ECDH_ES, encMethod)
                    .keyID(recipientKey.getKeyID())
                    .build();
            JWEObject jwe = new JWEObject(header, new Payload(plaintextJson));
            jwe.encrypt(new ECDHEncrypter(recipientKey.toPublicJWK()));
            return jwe.serialize();
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_FAILED_TO_ENCRYPT,
                    "Failed to encrypt VP response: " + e.getMessage());
        }
    }
}
