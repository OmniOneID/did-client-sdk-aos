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

import java.util.LinkedHashMap;

public class Referent {
    @SerializedName("schemaId")
    private String schemaId;
    @SerializedName("credDefId")
    private String credDefId;
    @SerializedName("attrs")
    private LinkedHashMap<String, ReferentAttributeValue> attributes;
    public Referent() {}
    public String getSchemaId() {
        return schemaId;
    }
    public String getCredDefId() {
        return credDefId;
    }
    public LinkedHashMap<String, ReferentAttributeValue> getAttributes() {
        return attributes;
    }
    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }
    public void setCredDefId(String credDefId) {
        this.credDefId = credDefId;
    }
    public void setAttributes(LinkedHashMap<String, ReferentAttributeValue> attributes) {
        this.attributes = attributes;
    }
}