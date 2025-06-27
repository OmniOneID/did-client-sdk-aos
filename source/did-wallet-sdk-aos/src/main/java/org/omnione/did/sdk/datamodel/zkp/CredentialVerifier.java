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

import android.util.Log;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.zkp.other.helper.CommitmentHelper;
import org.omnione.did.sdk.core.zkp.other.util.ChallengeBuilder;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.math.BigInteger;
import java.util.Map;

public class CredentialVerifier {

    public static boolean verify(CredentialSignature credSign,
                                 SignatureCorrectnessProof credSignProof,
                                 CredentialValues credValues,
                                 CredentialPrimaryPublicKey publicKey,
                                 BigInteger v_prime,
                                 BigInteger nonce) throws WalletCoreException, UtilityException {

        BigInteger s = publicKey.getS();
        BigInteger z = publicKey.getZ();
        BigInteger n = publicKey.getN();
        Map<String, BigInteger> r = publicKey.getR();
        PrimaryCredentialSignature pCredSign = credSign.getPrimaryCredential();

        // v = v' + v''
        BigInteger v = pCredSign.getV().add(v_prime);

        // update C1 with vPrime
        pCredSign.setV(v);

        BigInteger a = pCredSign.getA();
        BigInteger e = pCredSign.getE();

//        BigInteger sv = s.modPow(v, n);

        BigInteger m2 = credSign.getPrimaryCredential().getM2();
        BigInteger rctxt = publicKey.getRctxt();
        BigInteger sv = CommitmentHelper.commitment(s, v, rctxt, m2, n);

        for (String key : credValues.getAttrValues().keySet()) {
            WalletLogger.getInstance().d("key: " + key);
            sv = sv.multiply(r.get(key).modPow(credValues.get(key).getValue(), n)).mod(n);
        }

        if (sv.bitLength() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_INVALID_SIGNATURE_CALCULATION);
        }
        // Check if e and v are within the given range and if e is prime
        // Ae ≡ Z Sv ⋅ ∏i R mi i (mod n)
        BigInteger q = z.multiply(sv.modInverse(n)).mod(n);
        BigInteger q_cap = a.modPow(e, n);

        // Signature verification
        if (q.equals(q_cap)) {
            BigInteger exp = credSignProof.getC().add(credSignProof.getSe().multiply(e));
            BigInteger a_cap = a.modPow(exp, n);

            BigInteger c_cap = new ChallengeBuilder()
                    .add(q)
                    .add(a)
                    .add(a_cap)
                    .add(nonce)
                    .buildWithHashing();

            if (c_cap != null && c_cap.equals(credSignProof.getC())) {
                WalletLogger.getInstance().d("Credential primary [c_cap, c] match");
                return true;
            } else {
                WalletLogger.getInstance().d("Credential primary [c_cap, c] not match");
                return false;
            }
        } else {
            WalletLogger.getInstance().d("Credential primary [q, q_cap] not match (s, n, z, rctxt, vPrime)");
            return false;
        }
    }
}
