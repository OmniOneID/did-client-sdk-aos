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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class AttributeDef {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("type")
    @Expose
    private ATTR_TYPE type;
    @SerializedName("i18n")
    @Expose
    private Map<String, String> i18n;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Map<String, String> getI18n() {
        return i18n;
    }

    public void setI18n(Map<String, String> i18n) {
        this.i18n = i18n;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ATTR_TYPE getType() {
        return type;
    }

    public void setType(ATTR_TYPE type) {
        this.type = type;
    }

    public enum ATTR_TYPE {
        STRING("String"),
        NUMBER("Number");

        private String value;

        ATTR_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}