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

package org.omnione.did.sdk.datamodel.profile;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderDetail {
    @SerializedName("did")
    @Expose
    private String did;
    @SerializedName("certVcRef")
    @Expose
    private String certVcRef;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("logo")
    @Expose
    private LogoImage logo;
    @SerializedName("ref")
    @Expose
    private String ref;

    public String getDID() {
        return did;
    }

    public void setDID(String did) {
        this.did = did;
    }

    public String getCertVcRef() {
        return certVcRef;
    }

    public void setCertVcRef(String certVcRef) {
        this.certVcRef = certVcRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LogoImage getLogo() {
        return logo;
    }

    public void setLogo(LogoImage logo) {
        this.logo = logo;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void fromJson(String val) {
        Gson gson = new Gson();
        ProviderDetail obj = gson.fromJson(val, ProviderDetail.class);

        this.did = obj.getDID();
        this.certVcRef = obj.getCertVcRef();
        this.name = obj.getName();
        this.description = obj.getDescription();
        this.logo = obj.getLogo();
        this.ref = obj.getRef();
    }
}
