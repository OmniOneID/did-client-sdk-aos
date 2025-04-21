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

public class InitProof {

    private PrimaryInitProof primaryInitProof;
    private CredentialValues credentialValues;
    private SubProofRequest subProofRequest;
    private CredentialSchema credentialSchema;
    private NonCredentialSchema nonCredentialSchema;

    public InitProof(PrimaryInitProof primaryInitProof,
                     CredentialValues credentialValues,
                     SubProofRequest subProofRequest,
                     CredentialSchema credentialSchema,
                     NonCredentialSchema nonCredentialSchema) {

        this.primaryInitProof = primaryInitProof;
        this.credentialValues = credentialValues;
        this.subProofRequest = subProofRequest;
        this.credentialSchema = credentialSchema;
        this.nonCredentialSchema = nonCredentialSchema;
    }

    public PrimaryInitProof getPrimaryInitProof() {
        return primaryInitProof;
    }

    public CredentialValues getCredentialValues() {
        return credentialValues;
    }

    public SubProofRequest getSubProofRequest() {
        return subProofRequest;
    }

    public CredentialSchema getCredentialSchema() {
        return credentialSchema;
    }

    public NonCredentialSchema getNonCredentialSchema() {
        return nonCredentialSchema;
    }
}