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
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.communication.urlconnection.HttpUrlConnectionTask;
import org.omnione.did.sdk.core.oid4vc.errors.Oid4vciException;
import org.omnione.did.sdk.core.oid4vc.model.CredentialConfigDescriptor;
import org.omnione.did.sdk.core.oid4vc.model.CredentialResponseEncryptionSpec;
import org.omnione.did.sdk.core.oid4vc.model.IssuerMetadataResponse;
import org.omnione.did.sdk.core.oid4vc.net.wire.IssuerMetadataWire;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.util.LinkedHashMap;
import java.util.Map;

public final class IssuerMetadataFetcher {

    @NonNull private final HttpUrlConnectionTask http;

    public IssuerMetadataFetcher(@NonNull HttpUrlConnectionTask http) {
        this.http = http;
    }

    public IssuerMetadataResponse fetch(@NonNull String issuerUrl) throws CommunicationException {
        String wellKnown = issuerUrl + (issuerUrl.endsWith("/") ? "" : "/")
                + ".well-known/openid-credential-issuer";
        String body = http.makeHttpRequest(wellKnown, "GET", null, null);
        IssuerMetadataWire wire = GsonWrapper.getGson().fromJson(body, IssuerMetadataWire.class);
        if (wire == null || wire.credentialEndpoint == null) {
            throw new Oid4vciException("Issuer metadata missing credential_endpoint");
        }

        String tokenEndpoint = resolveTokenEndpointFromAuthServer(wire);
        if (tokenEndpoint == null) {
            tokenEndpoint = wire.tokenEndpoint;
        }
        if (tokenEndpoint == null) {
            throw new Oid4vciException("Could not resolve token endpoint");
        }

        Map<String, CredentialConfigDescriptor> configs = new LinkedHashMap<>();
        if (wire.credentialConfigurationsSupported != null) {
            for (Map.Entry<String, IssuerMetadataWire.ConfigSupported> entry
                    : wire.credentialConfigurationsSupported.entrySet()) {
                IssuerMetadataWire.ConfigSupported cs = entry.getValue();
                if (cs == null || cs.format == null) continue;
                configs.put(entry.getKey(), new CredentialConfigDescriptor(
                        entry.getKey(), cs.format, cs.vct, cs.doctype));
            }
        }

        return new IssuerMetadataResponse(
                wire.credentialIssuer == null ? issuerUrl : wire.credentialIssuer,
                wire.credentialEndpoint,
                tokenEndpoint,
                wire.authorizationServers,
                configs,
                mapResponseEncryption(wire.credentialResponseEncryption),
                wire.nonceEndpoint);
    }

    @Nullable
    private static CredentialResponseEncryptionSpec mapResponseEncryption(
            @Nullable IssuerMetadataWire.ResponseEncryption enc) {
        if (enc == null) return null;
        boolean hasAlg = enc.algValuesSupported != null && !enc.algValuesSupported.isEmpty();
        boolean hasEnc = enc.encValuesSupported != null && !enc.encValuesSupported.isEmpty();
        if (!hasAlg || !hasEnc) return null;
        return new CredentialResponseEncryptionSpec(
                enc.algValuesSupported, enc.encValuesSupported, enc.encryptionRequired);
    }

    private String resolveTokenEndpointFromAuthServer(@NonNull IssuerMetadataWire metadata) {
        if (metadata.authorizationServers == null || metadata.authorizationServers.isEmpty()) {
            return null;
        }
        String authServer = metadata.authorizationServers.get(0);
        String wellKnown = authServer + (authServer.endsWith("/") ? "" : "/")
                + ".well-known/oauth-authorization-server";
        try {
            String body = http.makeHttpRequest(wellKnown, "GET", null, null);
            JsonObject obj = GsonWrapper.getGson().fromJson(body, JsonObject.class);
            if (obj == null || !obj.has("token_endpoint") || obj.get("token_endpoint").isJsonNull()) {
                return null;
            }
            return obj.get("token_endpoint").getAsString();
        } catch (CommunicationException | RuntimeException e) {

            return null;
        }
    }
}
