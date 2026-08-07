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
import com.google.gson.JsonParser;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

public final class CredentialResponseDecryptor {

    @NonNull private final ECKey ephemeralKey;
    @NonNull private final String alg;
    @NonNull private final String enc;

    private CredentialResponseDecryptor(@NonNull ECKey ephemeralKey,
                                        @NonNull String alg,
                                        @NonNull String enc) {
        this.ephemeralKey = ephemeralKey;
        this.alg = alg;
        this.enc = enc;
    }

    @NonNull
    public static CredentialResponseDecryptor create(@NonNull String alg, @NonNull String enc) throws WalletCoreException {
        if (!alg.startsWith("ECDH-ES")) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_ALGORITHM_JWE,
                    "Unsupported credential_response_encryption alg: " + alg
                            + " (only ECDH-ES with an EC recipient key is supported)");
        }
        try {
            ECKey key = new ECKeyGenerator(Curve.P_256).generate();
            return new CredentialResponseDecryptor(key, alg, enc);
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_JWE_KEY,
                    "Failed to generate response-encryption key: " + e.getMessage());
        }
    }

    @NonNull
    public JsonObject requestParams() {
        JsonObject params = new JsonObject();
        params.add("jwk", JsonParser.parseString(
                ephemeralKey.toPublicJWK().toJSONString()).getAsJsonObject());
        params.addProperty("alg", alg);
        params.addProperty("enc", enc);
        return params;
    }

    @NonNull
    public String decrypt(@NonNull String jweCompact) throws WalletCoreException {
        WalletLogger.getInstance().d(
                "CredentialResponseDecryptor: decrypting response (alg=" + alg + ", enc=" + enc + ")");
        try {
            JWEObject jwe = JWEObject.parse(jweCompact.trim());
            jwe.decrypt(new ECDHDecrypter(ephemeralKey));
            return jwe.getPayload().toString();
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_JWE,
                    "Failed to decrypt credential response: " + e.getMessage());
        }
    }
}
