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
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.CredentialSchema;
import org.omnione.did.sdk.datamodel.zkp.CredentialSignature;
import org.omnione.did.sdk.datamodel.zkp.CredentialValues;
import org.omnione.did.sdk.datamodel.zkp.Identifiers;
import org.omnione.did.sdk.datamodel.zkp.InitProof;
import org.omnione.did.sdk.datamodel.zkp.NonCredentialSchema;
import org.omnione.did.sdk.datamodel.zkp.PrimaryInitProof;
import org.omnione.did.sdk.datamodel.zkp.Proof;
import org.omnione.did.sdk.datamodel.zkp.RequestedProof;
import org.omnione.did.sdk.datamodel.zkp.SubProofRequest;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ProofBuilder {
    private HashMap<String, BigInteger> commonAttributes = new HashMap<String, BigInteger>();
    private Vector<InitProof> initProof = new Vector<InitProof>();
    private Vector<byte[]> cList = new Vector<byte[]>();
    private Vector<byte[]> tauList = new Vector<byte[]>();
    public ProofBuilder(String commonAttrName) {
        this.addCommonAttribute(commonAttrName);
    }

    protected ProofBuilder addCommonAttribute(String attrName) {
        commonAttributes.put(attrName, new BigIntegerUtil().createRandomBigInteger(ZkpConstants.LARGE_MVECT));
        return this;
    }

    public ProofBuilder addSubProofRequest(SubProofRequest subProofRequest,
                                           CredentialSchema credSchema,
                                           NonCredentialSchema nonCredSchema,
                                           CredentialValues credValues,
                                           CredentialSignature credentialSignature,
                                           CredentialPrimaryPublicKey publicKey) throws WalletCoreException {

        this.checkAddSubProofRequestParams(credValues, subProofRequest, credSchema, nonCredSchema);
//      BigInteger m2_tilde = BigIntegerUtil.fromBytes(new GroupOrderElement().toBytes());
        BigInteger m2_tilde = new BigIntegerUtil().createRandomBigInteger(ZkpConstants.LARGE_NONCE);

        PrimaryInitProof primaryInitProof = ProofInitiator.initPrimaryProof(publicKey,
                credSchema,
                nonCredSchema,
                subProofRequest,
                credValues,
                credentialSignature.getPrimaryCredential(),
                m2_tilde,
                this.commonAttributes);

        // A′ to C.
        this.cList.addAll(primaryInitProof.getCommonValue());

        // Add t to T (primary, non_revoc)
        this.tauList.addAll(primaryInitProof.getTValue());

        this.initProof.add(new InitProof(primaryInitProof, credValues, subProofRequest, credSchema, nonCredSchema));
        return this;
    }

    public Proof build(BigInteger nonce, RequestedProof requestedProof, List<Identifiers> identifiers) throws WalletCoreException, UtilityException {
        return ProofFinalizer.finalize(this.initProof, this.cList, this.tauList, nonce, requestedProof, identifiers);
    }

    protected void checkAddSubProofRequestParams(CredentialValues credValues, SubProofRequest subProofReq, CredentialSchema credSchema, NonCredentialSchema nonCredSchema) {

    }
}