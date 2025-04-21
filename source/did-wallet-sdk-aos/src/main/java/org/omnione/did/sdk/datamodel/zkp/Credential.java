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

import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.util.LinkedHashMap;

public class Credential extends BaseObject {
    @SerializedName("credentialId")
    private String credentialId;

    @SerializedName("schemaId")
    private String schemaId;

    @SerializedName("credDefId")
    private String credDefId;

    private LinkedHashMap<String, AttributeValue> values;

    @SerializedName("signature")
    private CredentialSignature credentialSignature;

    @SerializedName("signatureCorrectnessProof")
    private SignatureCorrectnessProof signatureCorrectnessProof;

    public Credential() {
    }

    public String getSchemaId() {
        return schemaId;
    }
    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public String getCredDefId() {
        return credDefId;
    }
    public void setCredDefId(String credDefId) {
        this.credDefId = credDefId;
    }

    public LinkedHashMap<String, AttributeValue> getValues() {
        return values;
    }
    public void setValues(LinkedHashMap<String, AttributeValue> values) {
        this.values = values;
    }

    public CredentialSignature getCredentialSignature() {
        return credentialSignature;
    }
    public void setCredentialSignature(CredentialSignature credentialSignature) {
        this.credentialSignature = credentialSignature;
    }

    public SignatureCorrectnessProof getSignatureCorrectnessProof() {
        return signatureCorrectnessProof;
    }
    public void setSignatureCorrectnessProof(SignatureCorrectnessProof signatureCorrectnessProof) {
        this.signatureCorrectnessProof = signatureCorrectnessProof;
    }


    public void fromJson(String val) {
        GsonWrapper gson = new GsonWrapper();
        Credential result = gson.fromJson(val, Credential.class);

        this.schemaId = result.getSchemaId();
        this.credDefId = result.getCredDefId();
        this.values = result.getValues();
        this.credentialSignature = result.getCredentialSignature();
        this.signatureCorrectnessProof = result.getSignatureCorrectnessProof();
    }

    public String toJson() {
        GsonWrapper gson = new GsonWrapper();
        return gson.toJson(this);
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }
}