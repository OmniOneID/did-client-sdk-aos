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
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;

import java.text.ParseException;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;

public final class JwkThumbprintUtil {

    private JwkThumbprintUtil() {}

    @NonNull
    public static byte[] sha256(@NonNull JsonObject jwk) throws WalletCoreException {
        try {
            return JWK.parse(jwk.toString()).computeThumbprint().decode();
        } catch (ParseException | JOSEException | RuntimeException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_JWE_KEY,
                    "Failed to compute JWK thumbprint: " + e.getMessage());
        }
    }
}
