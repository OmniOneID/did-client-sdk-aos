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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.sdjwt.SdJwtCompact;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwt;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Encodings.Base64;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class SDJWTPresenter {

    private static final String KB_TYP = "kb+jwt";
    private static final String ALG = "ES256";
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private SDJWTPresenter() {}

    public static String createVpToken(SdJwt sdjwt,
                                       List<String> claimCodes,
                                       String aud,
                                       String nonce,
                                       JsonObject holderJwk,
                                       SignDigest signDigest,
                                       long iatEpochSeconds)
            throws WalletException, WalletCoreException, UtilityException {

        SdJwtCompact parsed;
        try {
            parsed = SdJwtCompact.parse(sdjwt.getCompact());
        } catch (RuntimeException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "Failed to parse SD-JWT for presentation: " + e.getMessage());
        }

        boolean discloseAll = (claimCodes == null || claimCodes.isEmpty());
        List<SdJwtCompact.Disclosure> selected = new ArrayList<>();
        for (SdJwtCompact.Disclosure d : parsed.getDisclosures()) {
            if (discloseAll || (!d.isArrayElement() && claimCodes.contains(d.getClaimName()))) {
                selected.add(d);
            }
        }

        StringBuilder base = new StringBuilder(parsed.getCredentialJwt());
        for (SdJwtCompact.Disclosure d : selected) {
            base.append('~').append(d.getEncoded());
        }
        base.append('~');
        String presentationBase = base.toString();

        String sdHash = Base64.encodeUrlString(
                DigestUtils.getDigest(presentationBase.getBytes(StandardCharsets.UTF_8),
                        DigestEnum.DIGEST_ENUM.SHA_256));

        JsonObject header = new JsonObject();
        header.addProperty("typ", KB_TYP);
        header.addProperty("alg", ALG);
        header.add("jwk", holderJwk);

        JsonObject payload = new JsonObject();
        payload.addProperty("iat", iatEpochSeconds);
        payload.addProperty("aud", aud);
        payload.addProperty("nonce", nonce);
        payload.addProperty("sd_hash", sdHash);

        String signingInput = Base64.encodeUrlString(GSON.toJson(header).getBytes(StandardCharsets.UTF_8))
                + "." + Base64.encodeUrlString(GSON.toJson(payload).getBytes(StandardCharsets.UTF_8));

        byte[] signature = signDigest.sign(signingInput.getBytes(StandardCharsets.UTF_8));
        if (signature == null || signature.length != 64) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_FAILED_TO_VERIFY_SIGNATURE,
                    "KB-JWT signature must be 64 bytes (raw R||S)");
        }
        String kbJwt = signingInput + "." + Base64.encodeUrlString(signature);

        return presentationBase + kbJwt;
    }
}
