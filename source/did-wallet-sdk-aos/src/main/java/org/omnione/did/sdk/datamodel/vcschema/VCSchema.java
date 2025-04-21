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

package org.omnione.did.sdk.datamodel.vcschema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.enums.ClaimFormat;
import org.omnione.did.sdk.datamodel.common.enums.ClaimType;
import org.omnione.did.sdk.datamodel.common.enums.Location;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;

import java.util.List;

public class VCSchema {
    @SerializedName("@id")
    @Expose
    private String id;

    @SerializedName("@schema")
    @Expose
    private String schema;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("metadata")
    @Expose
    private VCMetadata metadata;

    @SerializedName("credentialSubject")
    @Expose
    private CredentialSubject credentialSubject;

    public static class VCMetadata {
        public String language;
        public String formatVersion;
    }
    public static class CredentialSubject {
        private List<Claim> claims;

        public List<Claim> getClaims() {
            return claims;
        }

        public void setClaims(List<Claim> claims) {
            this.claims = claims;
        }
    }
    public static class Claim {
        public Namespace namespace;
        public List<ClaimDef> items;
    }
    public static class Namespace {
        public String id;
        public String name;
        public String ref;
    }
    public static class ClaimDef {
        public String id;
        public String caption;
        public ClaimType.CLAIM_TYPE type;
        public ClaimFormat.CLAIM_FORMAT format;
        public boolean hideValue = false;
        public Location.LOCATION location;
        public boolean required = true;
        public String description = "";
        //public Map<String, String> i18n;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
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

    public VCMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(VCMetadata metadata) {
        this.metadata = metadata;
    }

    public CredentialSubject getCredentialSubject() {
        return credentialSubject;
    }

    public void setCredentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
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
