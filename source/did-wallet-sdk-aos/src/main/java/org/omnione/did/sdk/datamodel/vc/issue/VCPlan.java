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

package org.omnione.did.sdk.datamodel.vc.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.profile.LogoImage;
import org.omnione.did.sdk.datamodel.vc.CredentialSchema;
import org.omnione.did.sdk.datamodel.zkp.CredentialDefinition;

import java.util.List;

public class VCPlan {
    @SerializedName("vcPlanId")
    @Expose
    private String vcPlanId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("ref")
    @Expose
    private String ref;

    @SerializedName("logo")
    @Expose
    private LogoImage logo;

    @SerializedName("validFrom")
    @Expose
    private String validFrom;

    @SerializedName("validUntil")
    @Expose
    private String validUntil;
    // private tags

    @SerializedName("credentialDefinition")
    @Expose
    private CredentialDefinition credentialDefinition;

    @SerializedName("credentialSchema")
    @Expose
    private CredentialSchema credentialSchema;

    @SerializedName("option")
    @Expose
    private Option option;

    @SerializedName("allowedIssuers")
    @Expose
    private List<String> allowedIssuers;

    @SerializedName("manager")
    @Expose
    private String manager;

    public CredentialDefinition getCredentialDefinition() {
        return credentialDefinition;
    }

    public void setCredentialDefinition(CredentialDefinition credentialDefinition) {
        this.credentialDefinition = credentialDefinition;
    }

    public static class Option {
        public boolean allowUserInit;
        public boolean allowIssuerInit;
        public boolean delegatedIssuance;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public LogoImage getLogo() {
        return logo;
    }

    public void setLogo(LogoImage logo) {
        this.logo = logo;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public CredentialSchema getCredentialSchema() {
        return credentialSchema;
    }

    public void setCredentialSchema(CredentialSchema credentialSchema) {
        this.credentialSchema = credentialSchema;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public List<String> getAllowedIssuers() {
        return allowedIssuers;
    }

    public void setAllowedIssuers(List<String> allowedIssuers) {
        this.allowedIssuers = allowedIssuers;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
