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

package org.omnione.did.sdk.datamodel.did;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Service {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private DIDServiceType.DID_SERVICE_TYPE type;

    @SerializedName("serviceEndpoint")
    @Expose
    private List<String> serviceEndpoint;

    public Service() {
    }

    public Service(String id, DIDServiceType.DID_SERVICE_TYPE type, List<String> serviceEndpoint) {
        this.id = id;
        this.type = type;
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DIDServiceType.DID_SERVICE_TYPE getType() {
        return type;
    }

    public void setType(DIDServiceType.DID_SERVICE_TYPE type) {
        this.type = type;
    }

    public List<String> getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(List<String> serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public void fromJson(String val) {
        Gson gson = new Gson();
        Service obj = gson.fromJson(val, Service.class);

        this.id = obj.getId();
        this.type = obj.getType();
        this.serviceEndpoint = obj.getServiceEndpoint();
    }
}
