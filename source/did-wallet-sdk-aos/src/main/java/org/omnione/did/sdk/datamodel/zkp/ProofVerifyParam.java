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

public class ProofVerifyParam {
    private CredentialSchema schema;
    private CredentialDefinition credentialDefinition;
    public ProofVerifyParam(Builder builder) {
        schema = builder.schema;
        credentialDefinition = builder.credentialDefinition;
    }

    public CredentialSchema getSchema() {
        return schema;
    }

    public CredentialDefinition getCredentialDefinition() {
        return credentialDefinition;
    }

    public static class Builder {

        private CredentialSchema schema;
        private CredentialDefinition credentialDefinition;
        public Builder setSchema(CredentialSchema schema) {
            this.schema = schema;
            return this;
        }
        public Builder setCredentialDefinition(CredentialDefinition credentialDefinition) {
            this.credentialDefinition = credentialDefinition;
            return this;
        }

        public ProofVerifyParam build() {
            return new ProofVerifyParam(this);
        }
    }
}
