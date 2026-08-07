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

public final class CredentialOfferWire {

    @SerializedName("credential_issuer")
    public String credentialIssuer;

    @SerializedName("credential_configuration_ids")
    public List<String> credentialConfigurationIds;

    @SerializedName("grants")
    public Grants grants;

    public static final class Grants {
        @SerializedName("urn:ietf:params:oauth:grant-type:pre-authorized_code")
        public PreAuth preAuthorizedCode;
    }

    public static final class PreAuth {
        @SerializedName("pre-authorized_code")
        public String preAuthorizedCode;

        @SerializedName("tx_code")
        public TxCode txCode;
    }

    public static final class TxCode {
        @SerializedName("length")
        public Integer length;

        @SerializedName("input_mode")
        public String inputMode;

        @SerializedName("description")
        public String description;
    }
}
