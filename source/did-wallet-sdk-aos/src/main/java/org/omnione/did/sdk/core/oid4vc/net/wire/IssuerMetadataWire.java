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

package org.omnione.did.sdk.core.oid4vc.net.wire;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public final class IssuerMetadataWire {

    @SerializedName("credential_issuer")
    public String credentialIssuer;

    @SerializedName("authorization_servers")
    public List<String> authorizationServers;

    @SerializedName("credential_endpoint")
    public String credentialEndpoint;

    @SerializedName("token_endpoint")
    public String tokenEndpoint;

    @SerializedName("nonce_endpoint")
    public String nonceEndpoint;

    @SerializedName("credential_configurations_supported")
    public Map<String, ConfigSupported> credentialConfigurationsSupported;

    @SerializedName("credential_response_encryption")
    public ResponseEncryption credentialResponseEncryption;

    public static final class ConfigSupported {
        @SerializedName("format")
        public String format;

        @SerializedName("vct")
        public String vct;

        @SerializedName("doctype")
        public String doctype;
    }

    public static final class ResponseEncryption {
        @SerializedName("alg_values_supported")
        public List<String> algValuesSupported;

        @SerializedName("enc_values_supported")
        public List<String> encValuesSupported;

        @SerializedName("encryption_required")
        public boolean encryptionRequired;
    }
}
