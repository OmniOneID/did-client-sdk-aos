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
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.JsonSortUtil;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;

import java.util.List;

public class VerifiableCredential extends BaseObject {

    @SerializedName("@context")
    @Expose
    private List<String> context;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private List<String> type;

    @SerializedName("issuer")
    @Expose
    private Issuer issuer;

    @SerializedName("issuanceDate")
    @Expose
    private String issuanceDate;

    @SerializedName("validFrom")
    @Expose
    private String validFrom; //utcDateTime

    @SerializedName("validUntil")
    @Expose
    private String validUntil; //utcDateTime

    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("formatVersion")
    @Expose
    private String formatVersion;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("evidence")
    @Expose
    private List<Evidence> evidence;

    @SerializedName("credentialSchema")
    @Expose
    private CredentialSchema credentialSchema;

    @SerializedName("credentialSubject")
    @Expose
    private CredentialSubject credentialSubject;

    @SerializedName("proof")
    @Expose
    private VCProof proof;

    public List<String> getContext() {
        return context;
    }

    public void setContext(List<String> context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Evidence> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<Evidence> evidence) {
        this.evidence = evidence;
    }

    public CredentialSchema getCredentialSchema() {
        return credentialSchema;
    }

    public void setCredentialSchema(CredentialSchema credentialSchema) {
        this.credentialSchema = credentialSchema;
    }

    public CredentialSubject getCredentialSubject() {
        return credentialSubject;
    }

    public void setCredentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
    }

    public VCProof getProof() {
        return proof;
    }

    public void setProof(VCProof proof) {
        this.proof = proof;
    }

//    public void setContext() {
//        ArrayList<String> contextList = new ArrayList<String>();
//        contextList.add("https://www.w3.org/2018/credentials/v1");
//        this.context = contextList;
//
//    }
//
//
//    public void setType() {
//        ArrayList<String> typeList = new ArrayList<String>();
//        typeList.add("VerifiableCredential");
//        typeList.add("OmniOneCredential");
//        this.type = typeList;
//    }


    public void fromJson(String val) {
        Gson gson = new Gson();
        VerifiableCredential obj = gson.fromJson(val, VerifiableCredential.class);

        context = obj.getContext();
        id = obj.getId();
        type = obj.getType();
        issuer = obj.getIssuer();
        issuanceDate = obj.getIssuanceDate();
        validFrom = obj.getValidFrom();
        validUntil = obj.getValidUntil();
        encoding = obj.getEncoding();
        formatVersion = obj.getFormatVersion();
        language = obj.getLanguage();
        evidence = obj.getEvidence();
        credentialSchema = obj.getCredentialSchema();
        credentialSubject = obj.getCredentialSubject();
        proof = obj.getProof();

    }

//    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//
//    public static String dateToString(Date date) {
//        return DATE_FORMAT.format(date);
//    }
//
//    public static Date stringToDate(String date) {
//        try {
//            return DATE_FORMAT.parse(date);
//        } catch (ParseException e) {
//          e.printStackTrace();
//        }
//        return null;
//    }

    public String toJson(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                .registerTypeAdapterFactory(new StringEnumAdapterFactory())
                .disableHtmlEscaping()
                .create();

        String json = gson.toJson(this);
        return JsonSortUtil.sortJsonString(gson, json);
    }
}
