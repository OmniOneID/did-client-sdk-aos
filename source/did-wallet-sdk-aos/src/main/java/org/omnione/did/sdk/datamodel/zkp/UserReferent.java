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

public class UserReferent {

    @SerializedName("credId")
    private String credentialId;
    private String raw;
    private String referentKey;
    private String referentName;
    private boolean isRevealed;

    public UserReferent(Builder builder) {
        credentialId = builder.credentialId;
        raw = builder.raw;
        referentKey = builder.referentKey;
        referentName = builder.referentName;
        isRevealed = builder.isRevealed;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public String getRaw() {
        return raw;
    }

    public String getReferentKey() {
        return referentKey;
    }

    public String getReferentName() {
        return referentName;
    }

    public boolean isRevealed() {
        return isRevealed;
    }


    public static class Builder {
        private String credentialId;
        private String raw;
        private String referentKey;
        private String referentName;
        private boolean isRevealed;

        public Builder setCredentialId(String credentialId) {
            this.credentialId = credentialId;
            return this;
        }

        public Builder setRaw(String raw) {
            this.raw = raw;
            return this;
        }

        public Builder setReferentKey(String referentKey) {
            this.referentKey = referentKey;
            return this;
        }

        public Builder setReferentName(String referentName) {
            this.referentName = referentName;
            return this;
        }

        public Builder setRevealed(boolean revealed) {
            isRevealed = revealed;
            return this;
        }

        public UserReferent build() {
            return new UserReferent(this);
        }
    }
}