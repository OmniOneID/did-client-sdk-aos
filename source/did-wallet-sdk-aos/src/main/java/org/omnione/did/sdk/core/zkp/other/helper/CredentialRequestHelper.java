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

package org.omnione.did.sdk.core.zkp.other.helper;

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.zkp.BlindedCredentialSecrets;
import org.omnione.did.sdk.datamodel.zkp.BlindedCredentialSecretsCorrectnessProof;
import org.omnione.did.sdk.datamodel.zkp.CredentialOffer;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryKeyPair;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.CredentialRequest;
import org.omnione.did.sdk.datamodel.zkp.MasterSecret;
import org.omnione.did.sdk.core.zkp.other.gen.BlindedSecretsGenerator;
import org.omnione.did.sdk.core.zkp.other.gen.BlindedSecretsProofGenerator;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.math.BigInteger;

public class CredentialRequestHelper {

    public static CredentialRequest generateCredentialRequest(CredentialPrimaryPublicKey credentialPublicKey,
                                                              String did,
                                                              MasterSecret masterSecret,
                                                              CredentialOffer credOffer,
                                                              BigInteger nonce, BigInteger v_prime) throws WalletCoreException, UtilityException {

        final BlindedCredentialSecrets blindedCredSecret = generateCredentialSecret(credentialPublicKey, masterSecret, v_prime);
        WalletLogger.getInstance().d("generateCredentialRequest prover nonce: "+nonce);
        final BlindedCredentialSecretsCorrectnessProof blindedCredSecretProof =
                generateCredentialSecretProof(credentialPublicKey, blindedCredSecret, credOffer, masterSecret, v_prime);

        CredentialRequest credReq = new CredentialRequest();
        credReq.setBlindedMs(blindedCredSecret);
        credReq.setBlindedMsProof(blindedCredSecretProof);
        credReq.setCredDefId(credOffer.getCredDefId());
        credReq.setProverDid(did);
        credReq.setNonce(nonce);
        return credReq;
    }


    private static BlindedCredentialSecrets generateCredentialSecret(CredentialPrimaryPublicKey credentialPublicKey,
                                                                     MasterSecret masterSecret, BigInteger v_prime) {
        return BlindedSecretsGenerator.generateBlindedSecrets(credentialPublicKey, masterSecret, v_prime);
    }

    //Wrapper Method
    private static BlindedCredentialSecretsCorrectnessProof generateCredentialSecretProof(CredentialPrimaryKeyPair credentialKeyPair,
                                                                                          BlindedCredentialSecrets credSecret,
                                                                                          CredentialOffer credOffer,
                                                                                          MasterSecret masterSecret,
                                                                                          BigInteger v_prime) throws WalletCoreException, UtilityException {

        return generateCredentialSecretProof(credentialKeyPair.getPublicKey(), credSecret, credOffer.getNonce(), masterSecret, v_prime);
    }

    private static BlindedCredentialSecretsCorrectnessProof generateCredentialSecretProof(
            CredentialPrimaryPublicKey publicKey,
            BlindedCredentialSecrets credSecret,
            CredentialOffer credOffer,
            MasterSecret masterSecret, BigInteger v_prime) throws WalletCoreException, UtilityException {

        return generateCredentialSecretProof(publicKey, credSecret, credOffer.getNonce(), masterSecret, v_prime);
    }

    private static BlindedCredentialSecretsCorrectnessProof generateCredentialSecretProof(
            CredentialPrimaryPublicKey publicKey, BlindedCredentialSecrets credSecret, BigInteger nonce, MasterSecret masterSecret, BigInteger v_prime) throws WalletCoreException, UtilityException {

        return BlindedSecretsProofGenerator.generateBlindedSecretsProof(publicKey, credSecret, masterSecret, nonce, v_prime);
    }
}