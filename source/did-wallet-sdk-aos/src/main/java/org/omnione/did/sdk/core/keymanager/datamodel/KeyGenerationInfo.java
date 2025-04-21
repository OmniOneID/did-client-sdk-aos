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

package org.omnione.did.sdk.core.keymanager.datamodel;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;

public class KeyGenerationInfo {
    @SerializedName("algoType")
    private AlgorithmType.ALGORITHM_TYPE algoType;

    @SerializedName("publicKey")
    private String publicKey;

    @SerializedName("privateKey")
    private String privateKey;

    public KeyGenerationInfo() {
    }

    public KeyGenerationInfo(String keyId, AlgorithmType.ALGORITHM_TYPE algoType, int storeType, String publicKey, String privateKey) {
        this.algoType = algoType;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public AlgorithmType.ALGORITHM_TYPE getAlgoType() {
        return algoType;
    }

    public void setAlgoType(AlgorithmType.ALGORITHM_TYPE algoType) {
        this.algoType = algoType;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
