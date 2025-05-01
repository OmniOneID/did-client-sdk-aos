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

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerUtil;

import java.math.BigInteger;
import java.util.Vector;

public class PrimaryInitProof {

    private PrimaryEqualInitProof eq_proof;
    private Vector<PrimaryPredicateInequalityInitProof> ne_proofs;

    public PrimaryInitProof(PrimaryEqualInitProof eq_proof,
                            Vector<PrimaryPredicateInequalityInitProof> ne_proofs) {

        this.eq_proof = eq_proof;
        this.ne_proofs = ne_proofs;
    }

    public PrimaryEqualInitProof getEqProof() {
        return eq_proof;
    }

    public Vector<PrimaryPredicateInequalityInitProof> getNeProofs() {
        return ne_proofs;
    }

    public Vector<byte[]> getCommonValue() throws WalletCoreException {

        Vector<byte[]> retvalue = new Vector<byte[]>();

        retvalue.add(eq_proof.getCommonValue());

        for (PrimaryPredicateInequalityInitProof entry : ne_proofs) {
            for (BigInteger entry1 : entry.getCommonValues()) {
                retvalue.add(BigIntegerUtil.asUnsignedByteArray(entry1));
            }
        }

        return retvalue;
    }

    public Vector<byte[]> getTValue() throws WalletCoreException {
        Vector<byte[]> retvalue = new Vector<byte[]>();

        retvalue.add(eq_proof.getTvalue());

        for (PrimaryPredicateInequalityInitProof entry : ne_proofs) {
            for (BigInteger entry1 : entry.getTValues()) {
                retvalue.add(BigIntegerUtil.asUnsignedByteArray(entry1));
            }
        }

        return retvalue;

    }
}
