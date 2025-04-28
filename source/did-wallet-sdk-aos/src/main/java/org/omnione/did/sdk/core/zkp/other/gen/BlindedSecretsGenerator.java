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

import org.omnione.did.sdk.datamodel.zkp.BlindedCredentialSecrets;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.MasterSecret;
import org.omnione.did.sdk.core.zkp.other.util.ZkpConstants;
import org.omnione.did.sdk.core.zkp.other.helper.CommitmentHelper;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerUtil;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BlindedSecretsGenerator {
    // P1. generate U, vPrime, Ur and vrPrime with prover-generated values
    public static BlindedCredentialSecrets generateBlindedSecrets(CredentialPrimaryPublicKey credentialPublicKey,
                                                                  MasterSecret masterSecret, BigInteger v_prime) {

        /**
         * 2.3 Pseudonym registration
         * For the master secret m1 and Issuer’s public key pkI Prover
         * 1. Generates random r < ρ and computes nym = gm1 hr (mod Γ). 2. Sends nym to the Issuer.
         * */

        final BigInteger s = credentialPublicKey.getS();
        final BigInteger n = credentialPublicKey.getN();
        final Map<String, BigInteger> r = credentialPublicKey.getR();


        /**
         Generate nym using issuer's public key (pk) and masterSecret (m1)
         generates r < p, nym = g^m1 * h^r (mod T)
         u : nym
         u = r1^m1 * s^vPrime (mod n)
         */
        BigInteger u = CommitmentHelper.commitment(s, v_prime,r.get(ZkpConstants.MASTER_SECRET_KEY),masterSecret.getMasterSecret(),n);
        // s.modPow(v_prime, n).multiply(r.get(ZkpConstants.MASTER_SECRET_KEY).modPow(masterSecret.getMasterSecret(), n)).mod(n);


        List<String> hiddenAttrs = new LinkedList<String>();
        hiddenAttrs.add(ZkpConstants.MASTER_SECRET_KEY);

        // TODO: Consider committedAttrs
        return new BlindedCredentialSecrets(u,
                v_prime,
                hiddenAttrs, null);

    }
}