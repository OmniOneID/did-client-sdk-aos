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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.core.storagemanager.datamodel.Meta;
import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.datamodel.common.enums.AuthType;

public class KeyInfo extends Meta {

    @SerializedName("authType")
    @Expose
    private AuthType.AUTH_TYPE authType; // alg

    @SerializedName("algorithm")
    @Expose
    private AlgorithmType.ALGORITHM_TYPE algorithm; // alg

    @SerializedName("publicKey")
    @Expose
    private String publicKey;

    @SerializedName("accessMethod")
    @Expose
    private KeyAccessMethod.KEY_ACCESS_METHOD accessMethod;

    public KeyInfo(){}
    public KeyInfo(String id, AuthType.AUTH_TYPE authType, AlgorithmType.ALGORITHM_TYPE algorithm, String publicKey, KeyAccessMethod.KEY_ACCESS_METHOD accessMethod) {
        this.id = id;
        this.authType = authType;
        this.algorithm = algorithm;
        this.publicKey = publicKey;
        this.accessMethod = accessMethod;
    }

    public AuthType.AUTH_TYPE getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType.AUTH_TYPE authType) {
        this.authType = authType;
    }

    public AlgorithmType.ALGORITHM_TYPE getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmType.ALGORITHM_TYPE algorithm) {
        this.algorithm = algorithm;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public KeyAccessMethod.KEY_ACCESS_METHOD getAccessMethod() {
        return accessMethod;
    }

    public void setAccessMethod(KeyAccessMethod.KEY_ACCESS_METHOD accessMethod) {
        this.accessMethod = accessMethod;
    }
}
