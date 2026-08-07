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

package org.omnione.did.sdk.core.oid4vc.format.sdjwt;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.OID4VCICredential;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwt;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwtCredentialItem;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.utility.Encodings.Base64;
import org.omnione.did.sdjwt.datamodel.Disclosure;
import org.omnione.did.sdjwt.datamodel.SDJWT;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SdJwtParser {

    private SdJwtParser() {}

    @NonNull
    public static SdJwt parse(@NonNull String compact) throws WalletCoreException {
        SDJWT sdjwt;
        try {
            sdjwt = SDJWT.parse(compact);
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "Failed to parse SD-JWT VC: " + e.getMessage());
        }
        String credentialJwt = sdjwt.getCredentialJwt();
        if (credentialJwt == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "SD-JWT has no credential JWT");
        }

        JsonObject jwtPayload = decodeJwtPayload(credentialJwt);
        Map<String, Object> disclosed = new LinkedHashMap<>();
        if (sdjwt.hasDisclosures()) {
            for (Disclosure d : sdjwt.getDisclosures()) {
                if (d.isArrayElement() || d.getClaimName() == null) continue;
                disclosed.put(d.getClaimName(), d.getClaimValue());
            }
        }
        return new SdJwt(compact, credentialJwt, jwtPayload, disclosed);
    }

    /**
     * Parses each stored OID4VC credential into a presentable {@link SdJwtCredentialItem}.
     *
     * @param stored The stored OID4VC credentials (may be {@code null}).
     * @return The parsed items; an empty list when {@code stored} is {@code null} or empty.
     * @throws WalletCoreException When any stored credential fails to parse as an SD-JWT.
     */
    @NonNull
    public static List<SdJwtCredentialItem> mapToSdJwtItems(List<OID4VCICredential> stored) throws WalletCoreException {
        List<SdJwtCredentialItem> items = new ArrayList<>();
        if (stored == null) {
            return items;
        }
        for (OID4VCICredential c : stored) {
            SdJwt sdjwt = parse(c.getCredential());
            items.add(new SdJwtCredentialItem(c.getId(), CredentialFormat.fromValue(c.getFormat()),
                    c.getCredentialConfigurationId(), c.getKid(), c.getCredentialIdentifier(), sdjwt));
        }
        return items;
    }

    @NonNull
    private static JsonObject decodeJwtPayload(@NonNull String jwt) throws WalletCoreException {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                        "Credential JWT is not a well-formed JWS");
            }
            String json = new String(Base64.decodeUrl(parts[1]), StandardCharsets.UTF_8);
            JsonObject obj = GsonWrapper.getGson().fromJson(json, JsonObject.class);
            return obj == null ? new JsonObject() : obj;
        } catch (WalletCoreException e) {
            throw e;
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "Failed to decode credential JWT payload");
        }
    }
}
