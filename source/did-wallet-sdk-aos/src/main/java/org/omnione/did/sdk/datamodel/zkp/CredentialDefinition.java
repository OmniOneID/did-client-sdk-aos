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

import org.omnione.did.sdk.core.zkp.other.type.CredentialType;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

public class CredentialDefinition {

    private String id;
    private String schemaId;
    private String ver;
    private CredentialType type;
    private CredentialDefinitionValue value = new CredentialDefinitionValue();
    private String tag;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getSchemaId() {
        return schemaId;
    }
    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
    }

    public String getVer() {
        return ver;
    }
    public void setVer(String ver) {
        this.ver = ver;
    }

    public CredentialType getType() {
        return type;
    }
    public void setType(CredentialType type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public CredentialDefinitionValue getValue() {
        return value;
    }
    public void setValue(CredentialDefinitionValue value) {
        this.value = value;
    }
    public void setValue(CredentialPrimaryPublicKey publicKey) {
        this.setPrimaryKey(publicKey);
    }

    // -- utils
    public void setPrimaryKey(CredentialPrimaryPublicKey publicKey) {
        this.value.setPrimary(publicKey);
    }

    public String toJson() {
        GsonWrapper gson = new GsonWrapper();
        return gson.toJson(this);
    }
}