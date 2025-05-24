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
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.common.enums.CredentialSchemaType;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.zkp.CredentialOffer;

import java.util.List;

public class IssueProfile implements ProofContainer {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private ProfileType.PROFILE_TYPE type;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("logo")
    @Expose
    private LogoImage logo;

    @SerializedName("encoding")
    @Expose
    private String encoding;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("profile")
    @Expose
    private Profile profile;

    @SerializedName("proof")
    @Expose
    private Proof proof;

    @SerializedName("proofs")
    @Expose
    private List<Proof> proofs;

    public static class Profile {
        public ProviderDetail issuer;
        public CredentialSchema credentialSchema;
        public CredentialOffer credentialOffer;

        public Process process;

        public static class CredentialSchema {
            public String id;
            public CredentialSchemaType.CREDENTIAL_SCHEMA_TYPE type;
            public String value;
        }

        public static class Process {
            public List<String> endpoints;
            public ReqE2e reqE2e;
            public String issuerNonce;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProfileType.PROFILE_TYPE getType() {
        return type;
    }

    public void setType(ProfileType.PROFILE_TYPE type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    @Override
    public List<Proof> getProofs() {
        return proofs;
    }

    @Override
    public void setProofs(List<Proof> proofs) {
        this.proofs = proofs;
    }

    public void fromJson(String val) {
        Gson gson = new Gson();
        IssueProfile obj = gson.fromJson(val, IssueProfile.class);

        this.id = obj.getId();
        this.type = obj.getType();
        this.title = obj.getTitle();
        this.description = obj.getDescription();
        this.logo = obj.getLogo();
        this.encoding = obj.getEncoding();
        this.language = obj.getLanguage();
        this.profile = obj.getProfile();
        this.proof = obj.getProof();
        this.proofs = obj.getProofs();

    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                .registerTypeAdapterFactory(new StringEnumAdapterFactory())
                .create();

        String json = gson.toJson(this);
        return json;
        //return ODIJsonSortUtil.sortJsonString(gson, json);
    }

}