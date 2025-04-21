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

package org.omnione.did.sdk.datamodel.vp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.JsonSortUtil;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;

import java.util.List;
import java.util.UUID;

public class VerifiablePresentation extends BaseObject implements ProofContainer {

    @SerializedName("@context")
    @Expose
    private List<String> context;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private List<String> type;

    @SerializedName("holder")
    @Expose
    private String holder;

    @SerializedName("validFrom")
    @Expose
    private String validFrom; //utcDateTime

    @SerializedName("validUntil")
    @Expose
    private String validUntil; //utcDateTime

    @SerializedName("verifierNonce")
    @Expose
    private String verifierNonce;

    @SerializedName("verifiableCredential")
    @Expose
    private List<VerifiableCredential> verifiableCredential;

    @SerializedName("proof")
    @Expose
    private Proof proof;

    @SerializedName("proofs")
    @Expose
    private List<Proof> proofs;

    public VerifiablePresentation() {}
    public VerifiablePresentation(String holder, String validFrom, String validUntil, String verifierNonce, List<VerifiableCredential> verifiableCredential){
        this.context = List.of("https://www.w3.org/ns/credentials/v2");
        this.id =  UUID.randomUUID().toString();
        this.type = List.of("VerifiablePresentation");
        this.holder = holder;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.verifierNonce = verifierNonce;
        this.verifiableCredential = verifiableCredential;
    }


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

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
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

    public String getVerifierNonce() {
        return verifierNonce;
    }

    public void setVerifierNonce(String verifierNonce) {
        this.verifierNonce = verifierNonce;
    }

    public List<VerifiableCredential> getVerifiableCredential() {
        return verifiableCredential;
    }

    public void setVerifiableCredential(List<VerifiableCredential> verifiableCredential) {
        this.verifiableCredential = verifiableCredential;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    public List<Proof> getProofs() {
        return proofs;
    }

    public void setProofs(List<Proof> proofs) {
        this.proofs = proofs;
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
        VerifiablePresentation obj = gson.fromJson(val, VerifiablePresentation.class);

        context = obj.getContext();
        id = obj.getId();
        type = obj.getType();
        holder = obj.getHolder();
        validFrom = obj.getValidFrom();
        validUntil = obj.getValidUntil();
        verifierNonce = obj.getVerifierNonce();
        verifiableCredential = obj.getVerifiableCredential();
        proof = obj.getProof();
        proofs = obj.getProofs();

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
