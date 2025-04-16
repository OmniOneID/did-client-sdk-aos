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

import java.util.List;

public class PredicateReferent {
    private String name;

    @SerializedName("checkRevealed")
    private boolean checkRevealed;

    @SerializedName("referent")
    private List<SubReferent> predicateSubReferent;

    public PredicateReferent(Builder builder) {
        name = builder.name;
        checkRevealed = builder.checkRevealed;
        predicateSubReferent = builder.predicateSubReferent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheckRevealed() {
        return checkRevealed;
    }

    public void setCheckRevealed(boolean checkRevealed) {
        this.checkRevealed = checkRevealed;
    }

    public List<SubReferent> getPredicateSubReferent() {
        return predicateSubReferent;
    }

    public void setAttrSubReferent(List<SubReferent> predicateSubReferent) {
        this.predicateSubReferent = predicateSubReferent;
    }

    public static class Builder {

        private String name;
        private boolean checkRevealed;
        private List<SubReferent> predicateSubReferent;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCheckRevealed(boolean checkRevealed) {
            this.checkRevealed = checkRevealed;
            return this;
        }

        public Builder setPredicateReferent(List<SubReferent> predicateSubReferent) {
            this.predicateSubReferent = predicateSubReferent;
            return this;
        }

        public PredicateReferent build() {
            return new PredicateReferent(this);
        }

    }
}

