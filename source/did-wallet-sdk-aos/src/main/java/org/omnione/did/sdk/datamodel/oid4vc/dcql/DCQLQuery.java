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

package org.omnione.did.sdk.datamodel.oid4vc.dcql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DCQLQuery {

    @SerializedName("credentials")
    @Expose
    private List<CredentialQuery> credentials;

    @SerializedName("credential_sets")
    @Expose
    private List<CredentialSetQuery> credentialSets;

    public List<CredentialQuery> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialQuery> credentials) {
        this.credentials = credentials;
    }

    public List<CredentialSetQuery> getCredentialSets() {
        return credentialSets;
    }

    public void setCredentialSets(List<CredentialSetQuery> credentialSets) {
        this.credentialSets = credentialSets;
    }
}
