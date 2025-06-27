/*
 * Copyright 2025 OmniOne.
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

package org.omnione.did.sdk.datamodel.zkp;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;

import java.math.BigInteger;

public class CredentialRequestMeta {
    private MasterSecretBlindingData masterSecretBlindingData;
    @SerializedName("nonce")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger nonce;

    private String masterSecretName;

    public CredentialRequestMeta(MasterSecretBlindingData masterSecretBlindingData, BigInteger nonce, String masterSecretName) {
        this.masterSecretBlindingData = masterSecretBlindingData;
        this.nonce = nonce;
        this.masterSecretName = masterSecretName;
    }

    public MasterSecretBlindingData getMasterSecretBlindingData() {
        return masterSecretBlindingData;
    }

    public void setMasterSecretBlindingData(MasterSecretBlindingData masterSecretBlindingData) {
        this.masterSecretBlindingData = masterSecretBlindingData;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getMasterSecretName() {
        return masterSecretName;
    }
}