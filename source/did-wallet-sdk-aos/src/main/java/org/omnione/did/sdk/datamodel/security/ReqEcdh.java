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

package org.omnione.did.sdk.datamodel.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.common.enums.EllipticCurveType;
import org.omnione.did.sdk.datamodel.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.JsonSortUtil;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;

import java.util.List;

public class ReqEcdh implements ProofContainer {
    @SerializedName("client")
    @Expose
    String client;
    @SerializedName("clientNonce")
    @Expose
    String clientNonce;
    @SerializedName("curve")
    @Expose
    EllipticCurveType.ELLIPTIC_CURVE_TYPE curve;
    @SerializedName("publicKey")
    @Expose
    String publicKey;

    @SerializedName("candidate")
    @Expose
    Ciphers candidate;

    @SerializedName("proof")
    @Expose
    Proof proof; //KeyAgreeProof

    @SerializedName("proofs")
    @Expose
    private List<Proof> proofs;

    public static class Ciphers{
        List<SymmetricCipherType.SYMMETRIC_CIPHER_TYPE> ciphers;
        //default : "AES-256-CBC"

        public Ciphers(List<SymmetricCipherType.SYMMETRIC_CIPHER_TYPE> ciphers) {
            this.ciphers = ciphers;
        }
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientNonce() {
        return clientNonce;
    }

    public void setClientNonce(String clientNonce) {
        this.clientNonce = clientNonce;
    }

    public EllipticCurveType.ELLIPTIC_CURVE_TYPE getCurve() {
        return curve;
    }

    public void setCurve(EllipticCurveType.ELLIPTIC_CURVE_TYPE curve) {
        this.curve = curve;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
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
    public Ciphers getCandidate() {
        return candidate;
    }

    public void setCandidate(Ciphers candidate) {
        this.candidate = candidate;
    }

    public String toJson() {
               Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                       .registerTypeAdapterFactory(new StringEnumAdapterFactory())
                       .disableHtmlEscaping()
                .create();
        String json = gson.toJson(this);
        return JsonSortUtil.sortJsonString(gson, json);

    }

    @Override
    public void fromJson(String val) {

    }
}
