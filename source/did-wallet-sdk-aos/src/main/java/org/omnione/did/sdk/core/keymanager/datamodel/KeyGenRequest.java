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
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.datamodel.util.JsonSortUtil;

public class KeyGenRequest {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("algorithmType")
    @Expose
    private AlgorithmType.ALGORITHM_TYPE algorithmType;
    @SerializedName("storageOption")
    @Expose
    private StorageOption.STORAGE_OPTION storage;

    public KeyGenRequest() {
    }

    public KeyGenRequest(String id, AlgorithmType.ALGORITHM_TYPE algorithmType, StorageOption.STORAGE_OPTION storage) {
        this.id = id;
        this.algorithmType = algorithmType;
        this.storage = storage;
    }

//    public KeyGenRequestProtocol(String keyId, AlgorithmType.ALGORITHM_TYPE algoType, StoreType.OPTION storeType, byte[] pin) {
//        this.keyId = keyId;
//        this.algoType = algoType;
//        this.storeType = storeType;
//        this.pin = pin;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AlgorithmType.ALGORITHM_TYPE getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType.ALGORITHM_TYPE algorithmType) {
        this.algorithmType = algorithmType;
    }

    public StorageOption.STORAGE_OPTION getStorage() {
        return storage;
    }

    public void setStorage(StorageOption.STORAGE_OPTION storage) {
        this.storage = storage;
    }

    public void fromJson(String val) {
        Gson gson = new Gson();
        KeyGenRequest obj = gson.fromJson(val, KeyGenRequest.class);

        this.id = obj.getId();
        this.algorithmType = obj.getAlgorithmType();
        this.storage = obj.getStorage();

    }

    public String toJson(){
        GsonWrapper gson = new GsonWrapper();
        String json = gson.toJson(this);
        return JsonSortUtil.sortJsonString(gson, json);
    }

//    public byte[] getPin() {
//        return pin;
//    }
//
//    public void setPin(byte[] pin) {
//        this.pin = pin;
//    }
}
