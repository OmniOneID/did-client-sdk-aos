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

import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.datamodel.common.enums.SymmetricPaddingType;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.JsonSortUtil;

public class AccEcdh {

    String server;
    String serverNonce;
    String publicKey;
    SymmetricCipherType.SYMMETRIC_CIPHER_TYPE cipher;
    SymmetricPaddingType.SYMMETRIC_PADDING_TYPE padding;
    Proof proof; //KeyAgreeProof

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerNonce() {
        return serverNonce;
    }

    public void setServerNonce(String serverNonce) {
        this.serverNonce = serverNonce;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public SymmetricCipherType.SYMMETRIC_CIPHER_TYPE getCipher() {
        return cipher;
    }

    public void setCipher(SymmetricCipherType.SYMMETRIC_CIPHER_TYPE cipher) {
        this.cipher = cipher;
    }

    public SymmetricPaddingType.SYMMETRIC_PADDING_TYPE getPadding() {
        return padding;
    }

    public void setPadding(SymmetricPaddingType.SYMMETRIC_PADDING_TYPE padding) {
        this.padding = padding;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    public String toJson() {
               Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                .create();
        String json = gson.toJson(this);
        return JsonSortUtil.sortJsonString(gson, json);

    }
}
