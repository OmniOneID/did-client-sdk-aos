/*
 * Copyright 202024-202524 OmniOne.
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

import java.util.HashMap;

public class Evidence {
    @SerializedName("id")
    @Expose
    private String id; //option

    @SerializedName("type")
    @Expose
    private EvidenceType.EVIDENCE_TYPE type;

    @SerializedName("verifier")
    @Expose
    private String verifier;
    @SerializedName("evidenceDocument")
    @Expose
    private String evidenceDocument;
    @SerializedName("subjectPresence")
    @Expose
    private Presence.PRESENCE subjectPresence;
    @SerializedName("documentPresence")
    @Expose
    private Presence.PRESENCE documentPresence;
    @SerializedName("attribute")
    @Expose	
    private HashMap<String, String> attribute;

    public Evidence(){
        this.type = EvidenceType.EVIDENCE_TYPE.documentVerification;
    }
    public Evidence(String verifier, String evidenceDocument, Presence.PRESENCE subjectPresence, Presence.PRESENCE documentPresence){
        this.type = EvidenceType.EVIDENCE_TYPE.documentVerification;
        this.verifier = verifier;
        this.evidenceDocument = evidenceDocument;
        this.subjectPresence = subjectPresence;
        this.documentPresence = documentPresence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EvidenceType.EVIDENCE_TYPE getType() {
        return type;
    }

    public void setType(EvidenceType.EVIDENCE_TYPE type) {
        this.type = type;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String getEvidenceDocument() {
        return evidenceDocument;
    }

    public void setEvidenceDocument(String evidenceDocument) {
        this.evidenceDocument = evidenceDocument;
    }

    public Presence.PRESENCE getSubjectPresence() {
        return subjectPresence;
    }

    public void setSubjectPresence(Presence.PRESENCE subjectPresence) {
        this.subjectPresence = subjectPresence;
    }

    public Presence.PRESENCE getDocumentPresence() {
        return documentPresence;
    }

    public void setDocumentPresence(Presence.PRESENCE documentPresence) {
        this.documentPresence = documentPresence;
    }

    public HashMap<String, String> getAttribute() {
        return attribute;
    }

    public void setAttribute(HashMap<String, String> attribute) {
        this.attribute = attribute;
    }

    public void fromJson(String val) {
        Gson gson = new Gson();
        Evidence obj = gson.fromJson(val, Evidence.class);

//        this.id = obj.getId();
//        this.name = obj.getName();
//        this.certVcRef = obj.getCertVcRef();
    }

}
