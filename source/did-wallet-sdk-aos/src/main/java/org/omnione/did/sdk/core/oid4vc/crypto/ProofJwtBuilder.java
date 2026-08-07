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
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.omnione.did.sdk.utility.Encodings.Base64;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.nio.charset.StandardCharsets;

public final class ProofJwtBuilder {

    private static final String TYP = "openid4vci-proof+jwt";
    private static final String ALG = "ES256";
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private ProofJwtBuilder() {}

    @NonNull
    public static String buildSigningInput(@NonNull JsonObject holderJwk,
                                           @NonNull String audience,
                                           @Nullable String nonce,
                                           long iatEpochSeconds) {
        JsonObject header = new JsonObject();
        header.addProperty("typ", TYP);
        header.addProperty("alg", ALG);
        header.add("jwk", holderJwk);

        JsonObject payload = new JsonObject();
        payload.addProperty("aud", audience);
        payload.addProperty("iat", iatEpochSeconds);
        if (nonce != null) {
            payload.addProperty("nonce", nonce);
        }

        String encodedHeader = b64Url(GSON.toJson(header).getBytes(StandardCharsets.UTF_8));
        String encodedPayload = b64Url(GSON.toJson(payload).getBytes(StandardCharsets.UTF_8));
        return encodedHeader + "." + encodedPayload;
    }

    @NonNull
    public static String assemble(@NonNull String signingInput, byte[] rawSignature) throws WalletException {
        if (rawSignature == null || rawSignature.length != 64) {
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_CREATE_PROOF_FAIL,
                    "ES256 signature must be 64 bytes (raw R||S), was "
                    + (rawSignature == null ? "null" : rawSignature.length));
        }
        return signingInput + "." + b64Url(rawSignature);
    }

    @NonNull
    private static String b64Url(@NonNull byte[] data) {
        return Base64.encodeUrlString(data);
    }
}
