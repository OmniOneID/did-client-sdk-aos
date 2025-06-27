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

import java.util.Vector;

public class PrimaryProof {

    @SerializedName("eqProof")
    private PrimaryEqualProof eqProof;

    @SerializedName("neProofs")
    private Vector<PrimaryPredicateInequalityProof> neProofs;

    public PrimaryProof() {}
    public PrimaryProof(PrimaryEqualProof eqProof, Vector<PrimaryPredicateInequalityProof> neProofs) {
        this.eqProof = eqProof;
        this.neProofs = neProofs;
    }

    public PrimaryEqualProof getEqProof() {
        return eqProof;
    }
    public void setEqProof(PrimaryEqualProof eqProof) {
        this.eqProof = eqProof;
    }

    public Vector<PrimaryPredicateInequalityProof> getNeProofs() {
        return neProofs;
    }
    public void setNeProofs(Vector<PrimaryPredicateInequalityProof> neProofs) {
        this.neProofs = neProofs;
    }
}