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

import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

public class OID4VCICredential extends BaseObject {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("format")
    @Expose
    private String format;

    @SerializedName("credentialConfigurationId")
    @Expose
    private String credentialConfigurationId;

    @SerializedName("credentialIdentifier")
    @Expose
    private String credentialIdentifier;

    @SerializedName("credential")
    @Expose
    private String credential;

    @SerializedName("kid")
    @Expose
    private String kid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCredentialConfigurationId() {
        return credentialConfigurationId;
    }

    public void setCredentialConfigurationId(String credentialConfigurationId) {
        this.credentialConfigurationId = credentialConfigurationId;
    }

    public String getCredentialIdentifier() {
        return credentialIdentifier;
    }

    public void setCredentialIdentifier(String credentialIdentifier) {
        this.credentialIdentifier = credentialIdentifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    @Override
    public void fromJson(String val) {
        GsonWrapper gson = new GsonWrapper();
        OID4VCICredential obj = gson.fromJson(val, OID4VCICredential.class);
        id = obj.getId();
        format = obj.getFormat();
        credentialConfigurationId = obj.getCredentialConfigurationId();
        credentialIdentifier = obj.getCredentialIdentifier();
        credential = obj.getCredential();
        kid = obj.getKid();
    }
}
