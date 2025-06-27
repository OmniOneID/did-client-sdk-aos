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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;

import java.math.BigInteger;

public class CredentialOffer {

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger nonce;

    @SerializedName("schemaId")
    private String schemaId;

    @SerializedName("credDefId")
    private String credDefId;

    @SerializedName("keyCorrectnessProof")
    private KeyCorrectnessProof keyCorrectnessProof;

//    @SerializedName("method_name")
    @Expose
    private String methodName;

    public BigInteger getNonce() {
        return nonce;
    }
    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
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

    public KeyCorrectnessProof getKeyCorrectnessProof() {
        return keyCorrectnessProof;
    }
    public void setKeyCorrectnessProof(KeyCorrectnessProof keyCorrectnessProof) {
        this.keyCorrectnessProof = keyCorrectnessProof;
    }

    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String toJson() {
        GsonWrapper gson = new GsonWrapper();
        return gson.toJson(this);
    }
}
