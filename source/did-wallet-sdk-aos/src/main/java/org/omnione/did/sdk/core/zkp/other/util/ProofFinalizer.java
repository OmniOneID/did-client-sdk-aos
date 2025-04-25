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

package org.omnione.did.sdk.core.zkp.other.util;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.zkp.AggregatedProof;
import org.omnione.did.sdk.datamodel.zkp.CredentialSchema;
import org.omnione.did.sdk.datamodel.zkp.CredentialValue;
import org.omnione.did.sdk.datamodel.zkp.CredentialValues;
import org.omnione.did.sdk.datamodel.zkp.Identifiers;
import org.omnione.did.sdk.datamodel.zkp.InitProof;
import org.omnione.did.sdk.datamodel.zkp.NonCredentialSchema;
import org.omnione.did.sdk.datamodel.zkp.Predicate;
import org.omnione.did.sdk.datamodel.zkp.PrimaryEqualInitProof;
import org.omnione.did.sdk.datamodel.zkp.PrimaryEqualProof;
import org.omnione.did.sdk.datamodel.zkp.PrimaryInitProof;
import org.omnione.did.sdk.datamodel.zkp.PrimaryPredicateInequalityInitProof;
import org.omnione.did.sdk.datamodel.zkp.PrimaryPredicateInequalityProof;
import org.omnione.did.sdk.datamodel.zkp.PrimaryProof;
import org.omnione.did.sdk.datamodel.zkp.Proof;
import org.omnione.did.sdk.datamodel.zkp.RequestedProof;
import org.omnione.did.sdk.datamodel.zkp.SubProof;
import org.omnione.did.sdk.datamodel.zkp.SubProofRequest;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class ProofFinalizer {

    public static Proof finalize(Vector<InitProof> initProofs, Vector<byte[]> cList, Vector<byte[]> tauList, BigInteger nonce, RequestedProof requestedProof, List<Identifiers> identifiers) throws WalletCoreException, UtilityException {

        if (cList == null || tauList == null || nonce == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "proof finalize is fail detected null field");
        }

        BigInteger challenge = new ChallengeBuilder()
                .add(tauList)
                .add(cList)
                .add(nonce)
                .buildWithHashing();

//        WalletLogger.getInstance().d("proof challenge: "+challenge);

        Vector<SubProof> proofs = new Vector<SubProof>();

        //PrimaryProof 생성
        for (InitProof initProof : initProofs) {
            PrimaryProof primaryProof = finalizePrimaryProof(initProof, challenge);
            //TODO: SubProof 구조의 개선필요
            proofs.add(new SubProof(primaryProof));
        }
        // Then (c, {PrC }, {Prp}, C) is the full proof sent to the Verifier.
        return new Proof(proofs, new AggregatedProof(challenge, cList), requestedProof, identifiers);
    }

    private static PrimaryProof finalizePrimaryProof(InitProof initProof, BigInteger challenge) throws WalletCoreException {
        return finalizePrimaryProof(initProof.getPrimaryInitProof(),
                challenge,
                initProof.getCredentialSchema(),
                initProof.getNonCredentialSchema(),
                initProof.getCredentialValues(),
                initProof.getSubProofRequest());
    }
    private static PrimaryProof finalizePrimaryProof(PrimaryInitProof initProof,
                                                     BigInteger challenge,
                                                     CredentialSchema credSchema,
                                                     NonCredentialSchema nonCredSchema,
                                                     CredentialValues credValues,
                                                     SubProofRequest subProofReq) throws WalletCoreException {

        PrimaryEqualProof equalProof = finalizeEqProof(initProof.getEqProof(),
                challenge,
                credSchema,
                nonCredSchema,
                credValues,
                subProofReq);

        Vector<PrimaryPredicateInequalityProof> neProofs = generateNeProofs(initProof, equalProof, challenge);

        return new PrimaryProof(equalProof, neProofs);
    }

    //Wrapper Method
    private static Vector<PrimaryPredicateInequalityProof> generateNeProofs(PrimaryInitProof initProof, PrimaryEqualProof equalProof, BigInteger challenge) throws WalletCoreException {
        Vector<PrimaryPredicateInequalityProof> neProofs = new Vector<PrimaryPredicateInequalityProof>();

        for (PrimaryPredicateInequalityInitProof neInitProof : initProof.getNeProofs()) {
            neProofs.add(finalizeNeProof(challenge, neInitProof, equalProof));
        }
        return neProofs;
    }

    private static PrimaryPredicateInequalityProof finalizeNeProof(BigInteger challenge,
                                                                   PrimaryPredicateInequalityInitProof neInitProof,
                                                                   PrimaryEqualProof eqProof) throws WalletCoreException {
        if (challenge == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "finalizeNeProof challenge is null in finalizeNeProof");
        } else if (neInitProof == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "finalizeNeProof neInitProof is null in finalizeNeProof");
        } else if (eqProof == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "finalizeNeProof eqProof is null in finalizeNeProof");
        }

        Map<String, BigInteger> u = new HashMap<String, BigInteger>();
        Map<String, BigInteger> r = new HashMap<String, BigInteger>();
        BigInteger ur = BigInteger.ZERO;

        final Map<String, BigInteger> u_tilde = neInitProof.getUTilde();
        final Map<String, BigInteger> r_tilde = neInitProof.getRTilde();
        final Map<String, BigInteger> uInit = neInitProof.getU();
        final Map<String, BigInteger> rInit = neInitProof.getR();
        final Map<String, BigInteger> tInit = neInitProof.getT();
        final BigInteger alpha_tilde = neInitProof.getAlphaTilde();
        final Predicate predicate = neInitProof.getPredicate();

        //TODO: for 조건문 ZkpConstants.ITERATION으로 변경 고려
        for (String key : neInitProof.getUTilde().keySet()) {
            BigInteger cur_u = uInit.get(key);
            BigInteger cur_r = rInit.get(key);
            // For 1 ≤ i ≤ 4 compute ui ← ui + cu.
            u.put(key, challenge.multiply(cur_u).add(u_tilde.get(key)));
            // For1 ≤ i ≤ 4 computer ri ← ri + cr.
            r.put(key, challenge.multiply(cur_r).add(r_tilde.get(key)));

            ur = cur_u.multiply(cur_r).add(ur);
        }

        //TODO: indy에서의 구현이 이상해서 일단 변경해둠
        BigInteger r_delta = rInit.get(ZkpConstants.DELTA);
        BigInteger r_delta_tilde = r_tilde.get(ZkpConstants.DELTA);

        r.put(ZkpConstants.DELTA, challenge.multiply(r_delta).add(r_delta_tilde));

        // Compute r∆ ← r∆ + c * r∆.
        // Compute α ← α + c(r∆ − u1*r1 − u2*r2 − u3*r3 − u4*r4).
        BigInteger alpha = r_delta.subtract(ur).multiply(challenge).add(alpha_tilde);

        //The values Prp = ({ui},{ri},r∆,α,mj) are the sub-proof for predicate p
        PrimaryPredicateInequalityProof neProof = new PrimaryPredicateInequalityProof(u, r, tInit, eqProof.getM().get(predicate.getAttrName()), alpha, predicate);
        return neProof;
    }

    private static PrimaryEqualProof finalizeEqProof(PrimaryEqualInitProof eqInitProof,
                                                     BigInteger challenge,
                                                     CredentialSchema credSchema,
                                                     NonCredentialSchema nonCredSchema,
                                                     CredentialValues credValues,
                                                     SubProofRequest subProofReq) throws WalletCoreException {

        // For each credential C = (I = {mj }, A, e, v) and Issuer’s public key pkI compute
        final BigInteger a_prime = eqInitProof.getAPrime();
        final BigInteger e_tilde = eqInitProof.getETilde();
        final BigInteger v_tilde = eqInitProof.getVTilde();
        final Map<String, BigInteger> m_tilde = eqInitProof.getM_tilde();

        BigInteger e = challenge.multiply(eqInitProof.getEPrime());
        BigInteger e_hat = e_tilde.add(e);

        BigInteger v = challenge.multiply(eqInitProof.getVPrime());
        BigInteger v_hat = v_tilde.add(v);

        Set<String> unrevealedAttrs = new HashSet<String>();
        // for 연산식 문제로 set 합집합, 차집합 연산 처리
        Set<String> nonCredSchemaList = nonCredSchema.getNonCredSchema();
        List<String> credSchemaList = credSchema.getAttrNames();
        Set<String> subProofReqAttrsList = subProofReq.getRevealedAttrs();

        unrevealedAttrs.addAll(nonCredSchemaList);
        unrevealedAttrs.addAll(credSchemaList);
        unrevealedAttrs.removeAll(subProofReqAttrsList);

        Map<String, BigInteger> m_hat = new HashMap<String, BigInteger>();
        // For all j ∈ Ar compute
        for (String key : unrevealedAttrs) {

            BigInteger cur_m_tilde = m_tilde.get(key);
            if (cur_m_tilde == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key '{}' not found in init_proof.mtilde in finalizeEqProof");
            }
            CredentialValue cur_value = credValues.get(key);
            if (cur_value == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key '{}' not found in attributes_values in finalizeEqProof");
            }

            m_hat.put(key, cur_value.getValue().multiply(challenge).add(cur_m_tilde));
        }

//        BigInteger m2_tilde = eqInitProof.getM2Tilde();
        BigInteger m2 = eqInitProof.getM2().multiply(challenge).add(eqInitProof.getM2Tilde());

        Map<String, BigInteger> revealedAttrsWithValue = new TreeMap<String, BigInteger>();

        for (String key : subProofReq.getRevealedAttrs()) {
            CredentialValue credValue = credValues.get(key);

            if (credValue == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "InvalidStructure: Encoded value not found in finalizeEqProof");
            }
            revealedAttrsWithValue.put(key, credValue.getValue());
        }
        // The values PrC = (e_hat, v_hat, m_hat, A′ ) are the sub-proof for credential C
        PrimaryEqualProof primaryEqualProof = new PrimaryEqualProof(revealedAttrsWithValue,
                a_prime,
                e_hat,
                v_hat,
                m_hat,
                m2);

        return primaryEqualProof;
    }
}
