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

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.zkp.other.helper.CommitmentHelper;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.KeyCorrectnessProof;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Vector;

public class KeyPairVerifier {

    public static boolean verify(CredentialPrimaryPublicKey publicKey, KeyCorrectnessProof keyProof) throws WalletCoreException, UtilityException {

        final BigInteger n = publicKey.getN();
        final BigInteger s = publicKey.getS();
        final BigInteger z = publicKey.getZ();
        final LinkedHashMap<String, BigInteger> r = publicKey.getR();

        final BigInteger proof_c = keyProof.getC();
        final BigInteger xz_cap = keyProof.getXzCap();
        final LinkedHashMap<String, BigInteger> xr_cap = keyProof.getXrCap();

        //z_cap operation
        BigInteger z_cap = CommitmentHelper.commitment(z.modInverse(n), proof_c, s, xz_cap, n);

        //r_cap operation
        Vector<BigInteger> r_cap = new Vector<BigInteger>();

        for (String attrName : publicKey.getR().keySet()) {
            BigInteger r_inverse = r.get(attrName).modInverse(n);
            BigInteger r_cap_value = CommitmentHelper.commitment(r_inverse, proof_c, s, xr_cap.get(attrName), n);
            r_cap.add(r_cap_value);
        }

        BigInteger c = new ChallengeBuilder()
                .add(z)
                .add(r)
                .add(z_cap)
                .add(r_cap)
                .buildWithHashing();

        if (c.equals(proof_c)) {
            WalletLogger.getInstance().d("KeyPairVerifier verify [c, proof_c] match\nc :"+c.toString()+"\nproof_c :"+proof_c.toString());
            return true;
        }
        WalletLogger.getInstance().d("KeyPairVerifier verify [c, proof_c] not match\nc :"+c.toString()+"\nproof_c :"+proof_c.toString());
//        throw new ZkpException(ErrorCode.OMNI_ERROR_ZKP_PROVER_CHECK_CREDENTIAL_KEY_CORRECTNESS_PROOF_FAIL);
        return false;
    }
}