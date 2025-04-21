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

public class CredentialRequestContainer {

    private CredentialRequest credentialRequest;
    private CredentialRequestMeta credentialRequestMeta;

    private CredentialRequestContainer(Builder builder) {
        credentialRequest = builder.credentialRequest;
        credentialRequestMeta = builder.credentialRequestMeta;
    }

    public CredentialRequest getCredentialRequest() {
        return credentialRequest;
    }

    public CredentialRequestMeta getCredentialRequestMeta() {
        return credentialRequestMeta;
    }

    public static class Builder {
        private CredentialRequest credentialRequest;
        private CredentialRequestMeta credentialRequestMeta;

        public Builder() {

        }

        public Builder setCredentialRequest(CredentialRequest credentialRequest) {
            this.credentialRequest = credentialRequest;
            return this;
        }

        public Builder setCredentialRequestMeta(CredentialRequestMeta credentialRequestMeta) {
            this.credentialRequestMeta = credentialRequestMeta;
            return this;
        }

        public CredentialRequestContainer build() {
            return new CredentialRequestContainer(this);
        }
    }

}