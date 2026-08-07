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

import org.omnione.did.sdk.datamodel.oid4vc.dcql.DCQLQuery;

public class AuthorizationRequest {

    @SerializedName("client_id")
    @Expose
    private String clientId;

    @SerializedName("nonce")
    @Expose
    private String nonce;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("response_mode")
    @Expose
    private String responseMode;

    @SerializedName("response_uri")
    @Expose
    private String responseUri;

    @SerializedName("dcql_query")
    @Expose
    private DCQLQuery dcqlQuery;

    @SerializedName("client_metadata")
    @Expose
    private ClientMetadata clientMetadata;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResponseMode() {
        return responseMode;
    }

    public void setResponseMode(String responseMode) {
        this.responseMode = responseMode;
    }

    public String getResponseUri() {
        return responseUri;
    }

    public void setResponseUri(String responseUri) {
        this.responseUri = responseUri;
    }

    public DCQLQuery getDcqlQuery() {
        return dcqlQuery;
    }

    public void setDcqlQuery(DCQLQuery dcqlQuery) {
        this.dcqlQuery = dcqlQuery;
    }

    public ClientMetadata getClientMetadata() {
        return clientMetadata;
    }

    public void setClientMetadata(ClientMetadata clientMetadata) {
        this.clientMetadata = clientMetadata;
    }
}
