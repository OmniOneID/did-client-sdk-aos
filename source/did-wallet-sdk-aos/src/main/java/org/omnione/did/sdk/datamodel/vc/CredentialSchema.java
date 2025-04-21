/*
 * Copyright 2024-2025 OmniOne.
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

package org.omnione.did.sdk.datamodel.vc;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.enums.CredentialSchemaType;

public class CredentialSchema {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private CredentialSchemaType.CREDENTIAL_SCHEMA_TYPE type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CredentialSchemaType.CREDENTIAL_SCHEMA_TYPE getType() {
        return type;
    }

    public void setType(CredentialSchemaType.CREDENTIAL_SCHEMA_TYPE type) {
        this.type = type;
    }

    public void fromJson(String val) {
        Gson gson = new Gson();
        CredentialSchema obj = gson.fromJson(val, CredentialSchema.class);

        this.id = obj.getId();
        this.type = obj.getType();
    }
}
