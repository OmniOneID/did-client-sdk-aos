/*
 * Copyright 2024-2025 OmniOne.
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

package org.omnione.did.sdk.datamodel.vc.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.common.SortData;

import java.util.List;

public class ReqRevokeVC extends SortData implements ProofContainer {

    @SerializedName("vcId")
    @Expose
    private String vcId;

    @SerializedName("issuerNonce")
    @Expose
    private String issuerNonce;

    @SerializedName("proof")
    @Expose
    private Proof proof;

    @SerializedName("proofs")
    @Expose
    private List<Proof> proofs;

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public String getIssuerNonce() {
        return issuerNonce;
    }

    public void setIssuerNonce(String issuerNonce) {
        this.issuerNonce = issuerNonce;
    }

    @Override
    public Proof getProof() {
        return proof;
    }

    @Override
    public void setProof(Proof proof) {
        this.proof = proof;
    }

    @Override
    public List<Proof> getProofs() {
        return proofs;
    }

    @Override
    public void setProofs(List<Proof> proofs) {
        this.proofs = proofs;
    }

    @Override
    public void fromJson(String val) {

    }
}
