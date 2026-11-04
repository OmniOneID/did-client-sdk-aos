/*
 * Copyright 2025-2026 OmniOne.
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

public class ReferentAttributeValue {

    @SerializedName("referentKey")
    private String referentKey;

    @SerializedName("isRevealed")
    private Boolean isRevealed;

    @SerializedName("attrName")
    private String attrName;

    public ReferentAttributeValue(String referentKey, Boolean isRevealed, String attrName) {
        this.referentKey = referentKey;
        this.isRevealed = isRevealed;
        this.attrName = attrName;
    }

    public String getReferentKey() {
        return referentKey;
    }

    public void setReferentKey(String referentKey) {
        this.referentKey = referentKey;
    }

    public Boolean getRevealed() {
        return isRevealed;
    }

    public void setRevealed(Boolean revealed) {
        isRevealed = revealed;
    }

    public String getAttrName() {
        return attrName;
    }
}