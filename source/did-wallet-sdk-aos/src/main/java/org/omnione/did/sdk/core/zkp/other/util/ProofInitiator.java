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
import org.omnione.did.sdk.core.zkp.other.helper.CommitmentHelper;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.CredentialSchema;
import org.omnione.did.sdk.datamodel.zkp.CredentialValue;
import org.omnione.did.sdk.datamodel.zkp.CredentialValues;
import org.omnione.did.sdk.datamodel.zkp.NonCredentialSchema;
import org.omnione.did.sdk.datamodel.zkp.Predicate;
import org.omnione.did.sdk.datamodel.zkp.PrimaryCredentialSignature;
import org.omnione.did.sdk.datamodel.zkp.PrimaryEqualInitProof;
import org.omnione.did.sdk.datamodel.zkp.PrimaryInitProof;
import org.omnione.did.sdk.datamodel.zkp.PrimaryPredicateInequalityInitProof;
import org.omnione.did.sdk.datamodel.zkp.SubProofRequest;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ProofInitiator {

    public static PrimaryInitProof initPrimaryProof(CredentialPrimaryPublicKey publicKey,
                                                    CredentialSchema credSchema,
                                                    NonCredentialSchema nonCredSchema,
                                                    SubProofRequest subProofReq,
                                                    CredentialValues credValues,
                                                    PrimaryCredentialSignature credSign,
                                                    BigInteger m2_tilde,
                                                    Map<String, BigInteger> commonAttributes) throws WalletCoreException {

        PrimaryEqualInitProof eqProof = initEqProof(publicKey, credSchema, nonCredSchema, credSign, subProofReq, m2_tilde, commonAttributes);
        Vector<PrimaryPredicateInequalityInitProof> neProofs = generateInitNeProofs(publicKey, eqProof.getM_tilde(), credValues, subProofReq);
        return new PrimaryInitProof(eqProof, neProofs);
    }

    //Wrapper Method
    private static <T extends PrimaryPredicateInequalityInitProof> Vector<T> generateInitNeProofs(CredentialPrimaryPublicKey publicKey,
                                                                                                  Map<String, BigInteger> m_tilde,
                                                                                                  CredentialValues credValues,
                                                                                                  SubProofRequest subProofReq) throws WalletCoreException {
        Vector<T> neProofs = new Vector();
        for (Predicate predicate : subProofReq.getPredicates()) {
            neProofs.add((T)initNeProof(publicKey, m_tilde, credValues, predicate));
        }
        return neProofs;
    }

    private static PrimaryPredicateInequalityInitProof initNeProof(CredentialPrimaryPublicKey publicKey,
                                                                   Map<String, BigInteger> m_tilde,
                                                                   CredentialValues credValues,
                                                                   Predicate predicate) throws WalletCoreException {

        try {
            BigIntegerUtil generator = new BigIntegerUtil();

            // Load Z, S from issuer’s public key.
            final BigInteger z = publicKey.getZ();
            final BigInteger s = publicKey.getS();

            final BigInteger n = publicKey.getN();

            CredentialValue credValue = credValues.get(predicate);

            if (credValue == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key " + predicate.getAttrName() + " not found in cred_values");
            }

            int delta = predicate.getDelta(credValue);

            //TODO: 0은 왜 정상인지 재고려
            if (delta < 0) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NEGATIVE_DELTA, "Delta value must be positive");
            }

            // ∆ ← mj − zj 라고 하고 ∆ = (u1)^2 + (u2)^2 + (u3)^2 + (u4)^2가 되도록 u1, u2, u3, u4를 찾는다.

            int[] uList = BigIntegerUtil.four_squares(delta);

            Map<String, BigInteger> u = new HashMap<String, BigInteger>();
            Map<String, BigInteger> r = new HashMap<String, BigInteger>();
            Map<String, BigInteger> t = new HashMap<String, BigInteger>();

            Vector<BigInteger> cList = new Vector<BigInteger>();

            for (int i = 0; i < uList.length; i++) {
                int value = uList[i];
                String index = String.valueOf(i);
                // Generate random 2128-bit numbers r1, r2, r3, r4, r∆, compute
                BigInteger cur_r = generator.createRandomBigInteger(ZkpConstants.LARGE_VPRIME);

                BigInteger cur_t = CommitmentHelper.commitment(z, BigInteger.valueOf(value), s, cur_r, n);

                u.put(index, BigInteger.valueOf(value));
                r.put(index, cur_r);
                t.put(index, cur_t);
                // add these values to cList
                cList.add(cur_t);
            }

            BigInteger rDelta = generator.createRandomBigInteger(ZkpConstants.LARGE_VPRIME);
            // T∆ ← Z∆ Sr∆
            BigInteger tDelta = CommitmentHelper.commitment(z, BigInteger.valueOf(delta), s, rDelta, n);

            r.put(ZkpConstants.DELTA, rDelta);
            t.put(ZkpConstants.DELTA, tDelta);
            // add these values to cList
            cList.add(tDelta);

            //////////////////////////////////////////////////////////////////////////////////////////////////////

            Map<String, BigInteger> u_tilde = new HashMap<String, BigInteger>();
            Map<String, BigInteger> r_tilde = new HashMap<String, BigInteger>();


            for (int i = 0; i < ZkpConstants.ITERATION; i++) {
                // generate random 592-bit numbers u1,u2,u3,u4
                u_tilde.put(Integer.toString(i), generator.createRandomBigInteger(ZkpConstants.LARGE_UTILDE));
                // generate random 672-bit numbers r1 , r2 , r3 , r4 , r∆ compute
                r_tilde.put(Integer.toString(i), generator.createRandomBigInteger(ZkpConstants.LARGE_RTILDE));
            }
            r_tilde.put(ZkpConstants.DELTA, generator.createRandomBigInteger(ZkpConstants.LARGE_RTILDE));


            // Generate random 2787-bit number α and compute
            BigInteger alpha_tilde = generator.createRandomBigInteger(ZkpConstants.LARGE_ALPHATILDE);
            BigInteger mj = m_tilde.get(predicate.getAttrName());

            if (mj == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "Value by key " + predicate.getAttrName() + " not found in eqProof.mTilde");
            }
            // add this values to T in the order T1, T2, T3, T4, T∆.
            Vector<BigInteger> tList = BigIntegerUtil.calc_tne(publicKey, u_tilde, r_tilde, mj, alpha_tilde, t, predicate.isLess());

            if (tList.isEmpty()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_CALCULATE_TNE_FAIL);
            }

            return new PrimaryPredicateInequalityInitProof(cList, tList, u, u_tilde, r, r_tilde, alpha_tilde, predicate, t);

        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_INITIALIZE_PRIMARY_NE_PROOF_FAIL);
        }
    }

    private static PrimaryEqualInitProof initEqProof(CredentialPrimaryPublicKey publicKey,
                                                     CredentialSchema credSchema,
                                                     NonCredentialSchema nonCredSchema,
                                                     PrimaryCredentialSignature credSign,
                                                     SubProofRequest subProofReq,
                                                     BigInteger m2_tilde,
                                                     Map<String, BigInteger> commonAttributes) throws WalletCoreException {
        try {
            BigIntegerUtil generator = new BigIntegerUtil();

            // 6.2.4 ProveCL
            // Choose random 2128-bit r;
            BigInteger r = generator.createRandomBigInteger(ZkpConstants.LARGE_VPRIME);

            BigInteger e_tilde = generator.createRandomBigInteger(ZkpConstants.LARGE_ETILDE);
            BigInteger v_tilde = generator.createRandomBigInteger(ZkpConstants.LARGE_VTILDE);

            // unrevealedAttrs 연산
            Set<String> unrevealedAttrs = new HashSet<String>();


            for (String value : nonCredSchema.getNonCredSchema()) {
                WalletLogger.getInstance().d("initEqProof getNonCredSchema value: "+value);
                unrevealedAttrs.add(value);
            }

            for (String value : credSchema.getAttrNames()) {
                if (!subProofReq.getRevealedAttrs().contains(value)) {
                    WalletLogger.getInstance().d("initEqProof !subProofReq.getRevealedAttrs().contains(value) value: "+value);
                    unrevealedAttrs.add(value);
                }
            }

            WalletLogger.getInstance().d("initEqProof unrevealedAttrs: "+unrevealedAttrs);

            Map<String, BigInteger> m_tilde = new HashMap<String, BigInteger>();
            m_tilde.putAll(commonAttributes);

            // 공개되지 않은 각 속성에 대해 i ∈ A는 임의의 592비트 숫자 m1을 생성.
            // generate m_tilde <- by unrevealedAttrs
            for (String key : unrevealedAttrs) {
//            WalletLogger.getInstance().d("unrevealedAttrs ..... "+key);
                if (key.equals(ZkpConstants.MASTER_SECRET_KEY)) {
                    continue;
                }
                m_tilde.put(key, generator.createRandomBigInteger(ZkpConstants.LARGE_MVECT));
            }

            BigInteger s = publicKey.getS();
            BigInteger n = publicKey.getN();

            BigInteger a = credSign.getA();
            BigInteger e = credSign.getE();
            BigInteger v = credSign.getV();

            // a' = A*S^r (mod n) 인데, 성능 향상을 위한 {(S^r mod n)*(A mod n)} mod n 작업 처리
            // A′ ← A * S^r (mod n)
            BigInteger a_prime = s.modPow(r, n).multiply(a.mod(n)).mod(n);

            //  e′ ← e - 2^596.
            BigInteger e_prime = e.subtract(ZkpConstants.LARGE_E_START_VALUE);
            // v′ ← v−e * r in integers.
            BigInteger v_prime = v.subtract(e.multiply(r));

            BigInteger t = BigIntegerUtil.calc_teq(publicKey, a_prime, e_tilde, v_tilde, m_tilde, m2_tilde, unrevealedAttrs);

            if (t.equals(BigInteger.ZERO)) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_CALCULATE_TEQ_FAIL);
            }

            return new PrimaryEqualInitProof(a_prime,
                    t,
                    e_tilde,
                    e_prime,
                    v_tilde,
                    v_prime,
                    m_tilde,
                    m2_tilde,
                    credSign.getM2());
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_INITIALIZE_PRIMARY_EQ_PROOF_FAIL);
        }
    }
}
