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

import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.JsonSortUtil;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;

import java.util.List;

public class DIDDocument extends BaseObject implements ProofContainer {

    @SerializedName("@context")
    @Expose
    private List<String> context;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("controller")
    @Expose
    private String controller;

    @SerializedName("verificationMethod")
    @Expose
    private List<VerificationMethod> verificationMethod;

    @SerializedName("assertionMethod")
    @Expose
    private List<String> assertionMethod;

    @SerializedName("authentication")
    @Expose
    private List<String> authentication;

    @SerializedName("keyAgreement")
    @Expose
    private List<String> keyAgreement;

    @SerializedName("capabilityInvocation")
    @Expose
    private List<String> capabilityInvocation;

    @SerializedName("capabilityDelegation")
    @Expose
    private List<String> capabilityDelegation;

    @SerializedName("service")
    @Expose
    private List<Service> service;
    @SerializedName("created")
    @Expose
    private String created; //utcDateTime

    @SerializedName("updated")
    @Expose
    private String updated; //utcDateTime

    @SerializedName("versionId")
    @Expose
    private String versionId; //DIDVersionId

    @SerializedName("deactivated")
    @Expose
    private Boolean deactivated;

    @SerializedName("proof")
    @Expose
    private Proof proof;

    @SerializedName("proofs")
    @Expose
    private List<Proof> proofs;

    public DIDDocument() {}
    public DIDDocument(String id, String controller, String created){
        this.context = List.of("https://www.w3.org/ns/did/v1");
        this.id = id;
        this.controller = controller;
        if(controller == null || controller.length() == 0)
            this.controller = id;
        this.created = created;
        this.updated = created;
        this.versionId = "1";
        this.deactivated = false;
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

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public List<VerificationMethod> getVerificationMethod() {
        return verificationMethod;
    }

    public void setVerificationMethod(List<VerificationMethod> verificationMethod) {
        this.verificationMethod = verificationMethod;
    }

    public List<String> getAssertionMethod() {
        return assertionMethod;
    }

    public void setAssertionMethod(List<String> assertionMethod) {
        this.assertionMethod = assertionMethod;
    }

    public List<String> getAuthentication() {
        return authentication;
    }

    public void setAuthentication(List<String> authentication) {
        this.authentication = authentication;
    }

    public List<String> getKeyAgreement() {
        return keyAgreement;
    }

    public void setKeyAgreement(List<String> keyAgreement) {
        this.keyAgreement = keyAgreement;
    }

    public List<String> getCapabilityInvocation() {
        return capabilityInvocation;
    }

    public void setCapabilityInvocation(List<String> capabilityInvocation) {
        this.capabilityInvocation = capabilityInvocation;
    }

    public List<String> getCapabilityDelegation() {
        return capabilityDelegation;
    }

    public void setCapabilityDelegation(List<String> capabilityDelegation) {
        this.capabilityDelegation = capabilityDelegation;
    }

    public List<Service> getService() {
        return service;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public boolean isDeactivated() {
        return deactivated;
    }

    public void setDeactivated(boolean deactivated) {
        this.deactivated = deactivated;
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

    public void fromJson(String val) {
        Gson gson = new Gson();
        DIDDocument obj = gson.fromJson(val, DIDDocument.class);

        this.context = obj.getContext();
        this.id = obj.getId();
        this.controller = obj.getController();
        this.verificationMethod = obj.getVerificationMethod();
        this.assertionMethod = obj.getAssertionMethod();
        this.authentication = obj.getAuthentication();
        this.keyAgreement = obj.getKeyAgreement();
        this.capabilityInvocation = obj.getCapabilityInvocation();
        this.capabilityDelegation = obj.getCapabilityDelegation();
        this.service = obj.getService();
        this.created = obj.getCreated();
        this.updated = obj.getUpdated();
        this.versionId = obj.getVersionId();
        this.deactivated = obj.isDeactivated();
        this.proof = obj.getProof();
        this.proofs = obj.getProofs();
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                .registerTypeAdapterFactory(new StringEnumAdapterFactory())
                .disableHtmlEscaping()
                .create();

        String json = gson.toJson(this);
        //return json;
        return JsonSortUtil.sortJsonString(gson, json);
    }

}
