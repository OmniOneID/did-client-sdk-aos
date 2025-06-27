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

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class ProveCredential {

    @SerializedName("credId")
    private String credentialId;

    private String timestemp;

    @SerializedName("revealedAttrs")
    private List<ProveRevealedAttribute> revealedAttrs;
    private List<ProveUnrevealedAttribute> unrevealedAttrs;
    private List<ProvePredicate> predicates;

    private CredentialSchema schema;
    private CredentialDefinition credentialDefinition;
    public ProveCredential() {

    }

    public ProveCredential(Builder builder) {
        this.credentialId = builder.credentialId;
        this.timestemp = builder.timestemp;
        this.revealedAttrs = builder.revealedAttrs;
        this.unrevealedAttrs = builder.unrevealedAttrs;
        this.predicates = builder.predicates;
        this.schema = builder.schema;
        this.credentialDefinition = builder.credentialDefinition;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public String getTimestemp() {
        return timestemp;
    }

    public List<ProveRevealedAttribute> getRevealedAttrs() {
        return revealedAttrs;
    }
    public List<ProveUnrevealedAttribute> getUnrevealedAttrs() {
        return unrevealedAttrs;
    }

    public List<ProvePredicate> getPredicates() {
        return predicates;
    }

    public CredentialSchema getSchema() {
        return schema;
    }

    public CredentialDefinition getCredentialDefinition() {
        return credentialDefinition;
    }

    public static class Builder {

        private String credentialId;
        private String timestemp;
        private List<ProveRevealedAttribute> revealedAttrs = new LinkedList<ProveRevealedAttribute>();
        private List<ProveUnrevealedAttribute> unrevealedAttrs = new LinkedList<ProveUnrevealedAttribute>();
        private List<ProvePredicate> predicates = new LinkedList<ProvePredicate>();
        private CredentialSchema schema;
        private CredentialDefinition credentialDefinition;

        public Builder() {

        }

        public Builder setCredentialId(String credentialId) {
            this.credentialId = credentialId;
            return this;
        }

        public Builder setTimestemp(String timestemp) {
            this.timestemp = timestemp;
            return this;
        }

        public Builder setRevealedAttrs(List<ProveRevealedAttribute> revealedAttrs) {
            this.revealedAttrs = revealedAttrs;
            return this;
        }
        public Builder setUnrevealedAttrs(List<ProveUnrevealedAttribute> unrevealedAttrs) {
            this.unrevealedAttrs = unrevealedAttrs;
            return this;
        }

        public Builder setPredicates(List<ProvePredicate> predicates) {
            this.predicates = predicates;
            return this;
        }

        public Builder setSchema(CredentialSchema schema) {
            this.schema = schema;
            return this;
        }
        public Builder setCredentialDefinition(CredentialDefinition credentialDefinition) {
            this.credentialDefinition = credentialDefinition;
            return this;
        }

        public ProveCredential build() {
            return new ProveCredential(this);
        }
    }
}