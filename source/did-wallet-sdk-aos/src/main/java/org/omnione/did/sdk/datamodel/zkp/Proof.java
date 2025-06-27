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

import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.util.List;
import java.util.Vector;

public class Proof {

    @SerializedName("proofs")
    private Vector<SubProof> proofs;

    @SerializedName("aggregatedProof")
    private AggregatedProof aggregatedProof;

    @SerializedName("requestedProof")
    private RequestedProof requestedProof;

    @SerializedName("identifiers")
    private List<Identifiers> identifiers;


    public Proof(Vector<SubProof> proofs, AggregatedProof aggregated_proof, RequestedProof requestedProof, List<Identifiers> identifiers) {
        this.proofs = proofs;
        this.aggregatedProof = aggregated_proof;
        this.requestedProof = requestedProof;
        this.identifiers = identifiers;
    }

    public Vector<SubProof> getProofs() {
        return proofs;
    }

    public AggregatedProof getAggregatedProof() {
        return aggregatedProof;
    }


    public RequestedProof getRequestedProof() {
        return requestedProof;
    }

    public void setRequestedProof(RequestedProof requestedProof) {
        this.requestedProof = requestedProof;
    }

    public List<Identifiers> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifiers> identifiers) {
        this.identifiers = identifiers;
    }
    public String toJson() {
        return GsonWrapper.getGsonPrettyPrinting().toJson(this);
    }
}