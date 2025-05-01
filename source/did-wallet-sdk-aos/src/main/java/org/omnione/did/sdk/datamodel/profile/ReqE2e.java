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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.enums.EllipticCurveType;
import org.omnione.did.sdk.datamodel.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.datamodel.common.enums.SymmetricPaddingType;

public class ReqE2e {
    @SerializedName("nonce")
    @Expose
    private String nonce;

    @SerializedName("curve")
    @Expose
    private EllipticCurveType.ELLIPTIC_CURVE_TYPE curve;

    @SerializedName("publicKey")
    @Expose
    private String publicKey;

    @SerializedName("cipher")
    @Expose
    private SymmetricCipherType.SYMMETRIC_CIPHER_TYPE cipher;

    @SerializedName("padding")
    @Expose
    private SymmetricPaddingType.SYMMETRIC_PADDING_TYPE padding;

    @SerializedName("proof")
    @Expose
    private Proof proof; //keyagree proof

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
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
}
