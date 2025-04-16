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

package org.omnione.did.sdk.core.zkp.other.gen;

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.zkp.BlindedCredentialSecrets;
import org.omnione.did.sdk.datamodel.zkp.BlindedCredentialSecretsCorrectnessProof;
import org.omnione.did.sdk.core.zkp.other.util.ChallengeBuilder;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.MasterSecret;
import org.omnione.did.sdk.core.zkp.other.util.ZkpConstants;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerUtil;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class BlindedSecretsProofGenerator {

//    //Wrapper Method
//    public static BlindedCredentialSecretsCorrectnessProof generateBlindedSecretsProof(CredentialPrimaryPublicKey publicKey, BlindedCredentialSecrets blindedSecret,
//                                                                                       MasterSecret masterSecret, BigInteger nonce) throws ZkpException {
//        //TODO: wallet을 masterSecretId로 조회하여 획득
////        BigInteger masterSecret = TestConstants.MASTER_SECRET;
//        return generateBlindedSecretsProof(publicKey, blindedSecret, masterSecret, nonce);
//    }

    public static BlindedCredentialSecretsCorrectnessProof generateBlindedSecretsProof(CredentialPrimaryPublicKey publicKey,
                                                                                       BlindedCredentialSecrets blindedSecret,
                                                                                       MasterSecret masterSecret,
                                                                                       BigInteger nonce, BigInteger v_prime) throws WalletCoreException, UtilityException {
        BigIntegerUtil generator = new BigIntegerUtil();

        final BigInteger s = publicKey.getS();
        final BigInteger n = publicKey.getN();
        final Map<String, BigInteger> r = publicKey.getR();

        //TODO: 상수값 검증 필요
        BigInteger v_dash_tilde = generator.createRandomBigInteger(ZkpConstants.LARGE_VPRIME_TILDE);
        BigInteger m_tilde = generator.createRandomBigInteger(ZkpConstants.LARGE_MTILDE);
        //1 + Fiat-Shamir Heuristic + security parameter + attribute size

        BigInteger u_tilde = s.modPow(v_dash_tilde, n);
        u_tilde = u_tilde.multiply(r.get(ZkpConstants.MASTER_SECRET_KEY).modPow(m_tilde, n));

        u_tilde = u_tilde.mod(n);

        //add 항목들의 순서는 고정되어야 한다.
        BigInteger c = new ChallengeBuilder()
                .add(blindedSecret.getU())
                .add(u_tilde)
                .add(nonce)
                .buildWithHashing();

        BigInteger v_dash_cap = c.multiply(v_prime).add(v_dash_tilde);

        LinkedHashMap<String, BigInteger> mCaps = new LinkedHashMap<String, BigInteger>();
        //TODO: commitment 타입때 사용



        //TODO: hidden Value들에 대한 재고려 필요 (현재 1개만 존재)
        BigInteger mCap = c.multiply(masterSecret.getMasterSecret()).add(m_tilde);

        mCaps.put(ZkpConstants.MASTER_SECRET_KEY, mCap);

        BlindedCredentialSecretsCorrectnessProof secretProof = new BlindedCredentialSecretsCorrectnessProof();
        secretProof.setC(c);
        secretProof.setVDashCap(v_dash_cap);
        secretProof.setMCaps(mCaps);

        return secretProof;
    }
}