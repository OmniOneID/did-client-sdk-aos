/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.datamodel.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.omnione.did.sdk.datamodel.common.enums.ProofPurpose;
import org.omnione.did.sdk.datamodel.common.enums.ProofType;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;

public class Proof {
    String created; //utc date time
    ProofPurpose.PROOF_PURPOSE proofPurpose;
    String verificationMethod;
    ProofType.PROOF_TYPE type;
    String proofValue;
    public Proof() {}
    public Proof(String created, ProofPurpose.PROOF_PURPOSE proofPurpose, String verificationMethod, ProofType.PROOF_TYPE type) {
        this.created = created;
        this.proofPurpose = proofPurpose;
        this.verificationMethod = verificationMethod;
        this.type = type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public ProofPurpose.PROOF_PURPOSE getProofPurpose() {
        return proofPurpose;
    }

    public void setProofPurpose(ProofPurpose.PROOF_PURPOSE proofPurpose) {
        this.proofPurpose = proofPurpose;
    }

    public String getVerificationMethod() {
        return verificationMethod;
    }

    public void setVerificationMethod(String verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

    public ProofType.PROOF_TYPE getType() {
        return type;
    }

    public void setType(ProofType.PROOF_TYPE type) {
        this.type = type;
    }

    public String getProofValue() {
        return proofValue;
    }

    public void setProofValue(String proofValue) {
        this.proofValue = proofValue;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                .registerTypeAdapterFactory(new StringEnumAdapterFactory())
                .create();

        String json = gson.toJson(this);
        return json;
    }
}
