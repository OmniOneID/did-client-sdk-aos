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

package org.omnione.did.sdk.core.didmanager.datamodel;

import com.google.gson.Gson;

import org.omnione.did.sdk.core.keymanager.datamodel.KeyInfo;

import java.util.List;

public class DIDKeyInfo {
    private KeyInfo keyInfo;
    private List<DIDMethodType.DID_METHOD_TYPE> methodType;
    private String controller;
    public DIDKeyInfo() {}

    public DIDKeyInfo(KeyInfo keyInfo, List<DIDMethodType.DID_METHOD_TYPE> methodType, String controller) {
        this.keyInfo = keyInfo;
        this.methodType = methodType;
        this.controller = controller;
    }

    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(KeyInfo keyInfo) {
        this.keyInfo = keyInfo;
    }

    public List<DIDMethodType.DID_METHOD_TYPE> getMethodType() {
        return methodType;
    }

    public void setMethodType(List<DIDMethodType.DID_METHOD_TYPE> methodType) {
        this.methodType = methodType;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
