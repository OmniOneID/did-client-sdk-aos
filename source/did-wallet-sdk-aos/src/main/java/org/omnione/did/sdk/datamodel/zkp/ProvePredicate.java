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

import org.omnione.did.sdk.core.zkp.other.type.PredicateType;

public class ProvePredicate extends Predicate {

    @SerializedName("referentkey")
    private String referentKey;

    public ProvePredicate(Builder builder) {
        super.setPType(builder.pType);
        super.setPValue(builder.pValue);
        super.setAttrName(builder.attrName);
        this.referentKey = builder.referentKey;
    }

    public String getReferentKey() {
        return referentKey;
    }

    public static class Builder {
        private String referentKey;
        private PredicateType pType;
        private int pValue;
        private String attrName;

        public Builder() {}

        public Builder setReferentKey(String referentKey) {
            this.referentKey = referentKey;
            return this;
        }

        public Builder setPType(PredicateType pType) {
            this.pType = pType;
            return this;
        }

        public Builder setPValue(int pValue) {
            this.pValue = pValue;
            return this;
        }

        public Builder setAttributeName(String attrName) {
            this.attrName = attrName;
            return this;
        }

        public ProvePredicate build() {
            return new ProvePredicate(this);
        }
    }
}
