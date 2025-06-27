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

public class ProofParam {

    private ReferentInfo referentInfo;
    private CredentialSchema schema;
    private CredentialDefinition credDef;

    public ProofParam(Builder builder) {

        referentInfo = builder.referentInfo;
        schema = builder.schema;
        credDef = builder.credDef;
    }

    public ReferentInfo getReferentInfo() {
        return referentInfo;
    }

    public CredentialSchema getSchema() {
        return schema;
    }

    public CredentialDefinition getCredDef() {
        return credDef;
    }

    public static class Builder {

        private ReferentInfo referentInfo;
        private CredentialSchema schema;
        private CredentialDefinition credDef;

        public Builder() {

        }

        public Builder setReferentInfo(ReferentInfo referentInfo) {
            this.referentInfo = referentInfo;
            return this;
        }

        public Builder setSchema(CredentialSchema schema) {
            this.schema = schema;
            return this;
        }

        public Builder setCredDef(CredentialDefinition credDef) {
            this.credDef = credDef;
            return this;
        }

        public ProofParam build() {
            return new ProofParam(this);
        }
    }
}