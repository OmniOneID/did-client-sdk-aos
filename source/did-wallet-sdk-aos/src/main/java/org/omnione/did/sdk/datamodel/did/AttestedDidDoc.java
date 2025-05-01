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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.token.Provider;

public class AttestedDidDoc {

    @SerializedName("walletId")
    @Expose
    private String walletId;

    @SerializedName("ownerDidDoc")
    @Expose
    private String ownerDidDoc;

    @SerializedName("provider")
    @Expose
    private Provider provider;

    @SerializedName("nonce")
    @Expose
    private String nonce;

    @SerializedName("proof")
    @Expose
    private Proof proof;


    public AttestedDidDoc() {
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getOwnerDidDoc() {
        return ownerDidDoc;
    }

    public void setOwnerDidDoc(String ownerDidDoc) {
        this.ownerDidDoc = ownerDidDoc;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }
}
