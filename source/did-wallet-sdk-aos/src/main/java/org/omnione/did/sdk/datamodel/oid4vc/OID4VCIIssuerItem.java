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

package org.omnione.did.sdk.datamodel.oid4vc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OID4VCIIssuerItem {
    @SerializedName("credentialIssuer")
    @Expose
    private String credentialIssuer;

    @SerializedName("credentialIssuerMetadataUri")
    @Expose
    private String credentialIssuerMetadataUri;

    @SerializedName("userInitiationUri")
    @Expose
    private String userInitiationUri;

    public String getCredentialIssuer() {
        return credentialIssuer;
    }

    public void setCredentialIssuer(String credentialIssuer) {
        this.credentialIssuer = credentialIssuer;
    }

    public String getCredentialIssuerMetadataUri() {
        return credentialIssuerMetadataUri;
    }

    public void setCredentialIssuerMetadataUri(String credentialIssuerMetadataUri) {
        this.credentialIssuerMetadataUri = credentialIssuerMetadataUri;
    }

    public String getUserInitiationUri() {
        return userInitiationUri;
    }

    public void setUserInitiationUri(String userInitiationUri) {
        this.userInitiationUri = userInitiationUri;
    }
}
