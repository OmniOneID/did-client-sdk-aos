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

public class AttrReferent {

    private String name;

    @SerializedName("checkRevealed")
    private boolean checkRevealed;

    @SerializedName("referent")
    private List<SubReferent> attrSubReferent;

    public AttrReferent(Builder builder) {
        name = builder.name;
        checkRevealed = builder.checkRevealed;
        attrSubReferent = builder.attrSubReferent;
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

    public List<SubReferent> getAttrSubReferent() {
        return attrSubReferent;
    }

    public void setAttrSubReferent(List<SubReferent> attrSubReferent) {
        this.attrSubReferent = attrSubReferent;
    }

    public static class Builder {

        private String name;
        private boolean checkRevealed;
        private List<SubReferent> attrSubReferent;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCheckRevealed(boolean checkRevealed) {
            this.checkRevealed = checkRevealed;
            return this;
        }

        public Builder setAttrSubReferent(List<SubReferent> attrSubReferent) {
            this.attrSubReferent = attrSubReferent;
            return this;
        }
        public AttrReferent build() {
            return new AttrReferent(this);
        }

    }


}