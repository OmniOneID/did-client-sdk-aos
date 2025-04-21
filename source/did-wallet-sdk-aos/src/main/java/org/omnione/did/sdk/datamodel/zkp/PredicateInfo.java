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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PredicateInfo {
    private String name;

    @SerializedName("pType")
    private PredicateType pType;

    @SerializedName("pValue")
    private int pValue;
    private List<Map<String, String>> restrictions = new ArrayList<Map<String, String>>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public PredicateType getPType() {
        return pType;
    }
    public void setPType(PredicateType pType) {
        this.pType = pType;
    }

    public int getPValue() {
        return pValue;
    }
    public void setPValue(int pValue) {
        this.pValue = pValue;
    }

    public List<Map<String, String>> getRestrictions() {
        return restrictions;
    }
    public void setRestrictions(List<Map<String, String>> restrictions) {
        this.restrictions = restrictions;
    }

    public void addRestriction(Map<String, String> restriction) {
        this.restrictions.add(restriction);
    }
}
