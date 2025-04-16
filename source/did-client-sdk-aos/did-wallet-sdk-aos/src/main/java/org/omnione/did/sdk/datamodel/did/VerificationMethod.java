/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.datamodel.did;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.enums.AuthType;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;

public class VerificationMethod {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private DIDKeyType.DID_KEY_TYPE type;

    @SerializedName("controller")
    @Expose
    private String controller;

    @SerializedName("publicKeyMultibase")
    @Expose
    private String publicKeyMultibase;

    @SerializedName("authType")
    @Expose
    private AuthType.AUTH_TYPE authType;

    @SerializedName("status")
    @Expose
    private String status;

    public VerificationMethod() {
    }

    public VerificationMethod(String id, DIDKeyType.DID_KEY_TYPE type, String DID, String publicKeyMultibase, AuthType.AUTH_TYPE authType, String status) {
        this.id = id;
        this.type = type;
        this.controller = DID;
        this.publicKeyMultibase = publicKeyMultibase;
        this.authType = authType;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DIDKeyType.DID_KEY_TYPE getType() {
        return type;
    }

    public void setType(DIDKeyType.DID_KEY_TYPE type) {
        this.type = type;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getPublicKeyMultibase() {
        return publicKeyMultibase;
    }

    public void setPublicKeyMultibase(String publicKeyMultibase) {
        this.publicKeyMultibase = publicKeyMultibase;
    }

    public AuthType.AUTH_TYPE getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType.AUTH_TYPE authType) {
        this.authType = authType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void fromJson(String val) {
        Gson gson = new Gson();
        VerificationMethod obj = gson.fromJson(val, VerificationMethod.class);

        this.id = obj.getId();
        this.type = obj.getType();
        this.controller = obj.getController();
        this.publicKeyMultibase = obj.getPublicKeyMultibase();
        this.authType = obj.getAuthType();
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                .registerTypeAdapterFactory(new StringEnumAdapterFactory())
                .create();

        String json = gson.toJson(this);
        return json;
    }
}
