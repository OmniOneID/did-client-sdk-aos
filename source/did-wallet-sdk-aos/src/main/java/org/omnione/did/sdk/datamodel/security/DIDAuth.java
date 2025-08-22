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

package org.omnione.did.sdk.datamodel.security;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.common.SortData;
import org.omnione.did.sdk.datamodel.did.SignedDidDoc;

import java.util.List;

public class DIDAuth extends SortData implements ProofContainer {
    @SerializedName("did")
    @Expose
    private String did;

    @SerializedName("authNonce")
    @Expose
    private String authNonce;

    @SerializedName("proof")
    @Expose
    private Proof proof;

    @SerializedName("proofs")
    @Expose
    private List<Proof> proofs;

    public String getDID() {
        return did;
    }

    public void setDID(String did) {
        this.did = did;
    }

    public String getAuthNonce() {
        return authNonce;
    }

    public void setAuthNonce(String authNonce) {
        this.authNonce = authNonce;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    public List<Proof> getProofs() {
        return proofs;
    }

    public void setProofs(List<Proof> proofs) {
        this.proofs = proofs;
    }

    @Override
    public void fromJson(String val) {

        Gson gson = new Gson();
        DIDAuth obj = gson.fromJson(val, DIDAuth.class);
        this.did = obj.getDID();
        this.authNonce = obj.getAuthNonce();
        this.proof = obj.getProof();
        this.proofs = obj.getProofs();
    }
}
