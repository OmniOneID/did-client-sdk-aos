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

package org.omnione.did.sdk.core.oid4vc.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class IssuerMetadataResponse {

    @NonNull private final String credentialIssuer;
    @NonNull private final String credentialEndpoint;
    @NonNull private final String tokenEndpoint;
    @Nullable private final List<String> authorizationServers;
    @NonNull private final Map<String, CredentialConfigDescriptor> configurationsSupported;
    @Nullable private final CredentialResponseEncryptionSpec responseEncryption;
    @Nullable private final String nonceEndpoint;

    public IssuerMetadataResponse(@NonNull String credentialIssuer,
                                 @NonNull String credentialEndpoint,
                                 @NonNull String tokenEndpoint,
                                 @Nullable List<String> authorizationServers,
                                 @NonNull Map<String, CredentialConfigDescriptor> configurationsSupported,
                                 @Nullable CredentialResponseEncryptionSpec responseEncryption,
                                 @Nullable String nonceEndpoint) {
        this.credentialIssuer = credentialIssuer;
        this.credentialEndpoint = credentialEndpoint;
        this.tokenEndpoint = tokenEndpoint;
        this.authorizationServers = authorizationServers == null
                ? null : Collections.unmodifiableList(authorizationServers);
        this.configurationsSupported = Collections.unmodifiableMap(configurationsSupported);
        this.responseEncryption = responseEncryption;
        this.nonceEndpoint = nonceEndpoint;
    }

    @NonNull public String getCredentialIssuer() { return credentialIssuer; }
    @NonNull public String getCredentialEndpoint() { return credentialEndpoint; }
    @NonNull public String getTokenEndpoint() { return tokenEndpoint; }
    @Nullable public List<String> getAuthorizationServers() { return authorizationServers; }
    @NonNull public Map<String, CredentialConfigDescriptor> getConfigurationsSupported() { return configurationsSupported; }

    @Nullable public CredentialResponseEncryptionSpec getResponseEncryption() { return responseEncryption; }

    @Nullable public String getNonceEndpoint() { return nonceEndpoint; }

    @Nullable
    public CredentialConfigDescriptor configForId(@NonNull String configurationId) {
        return configurationsSupported.get(configurationId);
    }
}
