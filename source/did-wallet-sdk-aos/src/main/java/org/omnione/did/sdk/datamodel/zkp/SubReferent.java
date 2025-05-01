/*
 * Copyright 2025 OmniOne.
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

package org.omnione.did.sdk.datamodel.zkp;

import com.google.gson.annotations.SerializedName;

public class SubReferent {
    private String raw;
    @SerializedName("credId")
    private String credentialId;
    @SerializedName("credDefId")
    private String credentialDefId;

    public SubReferent(String raw, String credentialId, String credentialDefId) {
        this.raw = raw;
        this.credentialId = credentialId;
        this.credentialDefId = credentialDefId;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getCredentialDefId() {
        return credentialDefId;
    }

    public void setCredentialDefId(String credentialDefId) {
        this.credentialDefId = credentialDefId;
    }
}
