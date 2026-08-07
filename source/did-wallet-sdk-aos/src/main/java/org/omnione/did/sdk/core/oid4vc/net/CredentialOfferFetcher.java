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

package org.omnione.did.sdk.core.oid4vc.net;

import androidx.annotation.NonNull;

import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.communication.urlconnection.HttpUrlConnectionTask;
import org.omnione.did.sdk.core.oid4vc.errors.Oid4vciException;
import org.omnione.did.sdk.core.oid4vc.model.CredentialOfferResponse;
import org.omnione.did.sdk.core.oid4vc.model.TxCodeSpec;
import org.omnione.did.sdk.core.oid4vc.net.wire.CredentialOfferWire;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class CredentialOfferFetcher {

    @NonNull private final HttpUrlConnectionTask http;

    public CredentialOfferFetcher(@NonNull HttpUrlConnectionTask http) {
        this.http = http;
    }

    public CredentialOfferResponse parse(@NonNull String rawPayload) throws CommunicationException {
        String trimmed = rawPayload.trim();
        if (trimmed.startsWith("{")) {
            return parseJson(trimmed);
        }
        if (trimmed.startsWith("openid-credential-offer://")) {
            return parseOpenidOfferUri(trimmed);
        }
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return parseJson(http.makeHttpRequest(trimmed, "GET", null, null));
        }
        throw new Oid4vciException("Unrecognized credential offer payload");
    }

    private CredentialOfferResponse parseOpenidOfferUri(@NonNull String uriString) throws CommunicationException {
        URI uri = URI.create(uriString);
        String query = uri.getRawQuery();
        if (query == null || query.isEmpty()) {
            throw new Oid4vciException("openid-credential-offer URI has no query string");
        }
        String credentialOfferParam = null;
        String credentialOfferUriParam = null;
        for (String pair : query.split("&")) {
            int eq = pair.indexOf('=');
            if (eq <= 0) continue;
            String key = pair.substring(0, eq);
            String value = decode(pair.substring(eq + 1));
            if ("credential_offer".equals(key)) {
                credentialOfferParam = value;
            } else if ("credential_offer_uri".equals(key)) {
                credentialOfferUriParam = value;
            }
        }
        if (credentialOfferParam != null) {
            return parseJson(credentialOfferParam);
        }
        if (credentialOfferUriParam != null) {
            return parseJson(http.makeHttpRequest(credentialOfferUriParam, "GET", null, null));
        }
        throw new Oid4vciException("openid-credential-offer URI missing credential_offer/credential_offer_uri");
    }

    private static String decode(@NonNull String encoded) {
        try {
            return URLDecoder.decode(encoded, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return encoded;
        }
    }

    private CredentialOfferResponse parseJson(@NonNull String json) {
        CredentialOfferWire wire = GsonWrapper.getGson().fromJson(json, CredentialOfferWire.class);
        if (wire == null || wire.credentialIssuer == null) {
            throw new Oid4vciException("Credential offer missing credential_issuer");
        }
        List<String> configIds = wire.credentialConfigurationIds == null
                ? new ArrayList<>() : wire.credentialConfigurationIds;

        String preAuthCode = null;
        TxCodeSpec txCodeSpec = null;
        if (wire.grants != null && wire.grants.preAuthorizedCode != null) {
            preAuthCode = wire.grants.preAuthorizedCode.preAuthorizedCode;
            CredentialOfferWire.TxCode tx = wire.grants.preAuthorizedCode.txCode;
            if (tx != null) {
                TxCodeSpec.InputMode mode = TxCodeSpec.parseInputMode(tx.inputMode);

                if (mode != TxCodeSpec.InputMode.NUMERIC) {
                    throw new Oid4vciException(
                            "Unsupported tx_code input_mode: " + (tx.inputMode == null ? "unknown" : tx.inputMode));
                }
                int length = tx.length == null ? 6 : tx.length;
                txCodeSpec = new TxCodeSpec(length, mode, tx.description);
            }
        }

        if (preAuthCode == null) {
            throw new Oid4vciException(
                    "Only pre-authorized_code grant is supported in this iteration");
        }

        return new CredentialOfferResponse(
                wire.credentialIssuer,
                configIds,
                preAuthCode,
                txCodeSpec);
    }
}
