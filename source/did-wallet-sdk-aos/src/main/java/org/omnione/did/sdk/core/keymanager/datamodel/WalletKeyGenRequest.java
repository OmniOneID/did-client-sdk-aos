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

import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;

public class WalletKeyGenRequest extends KeyGenRequest {
    @SerializedName("walletMethodType")
    @Expose
    private KeyGenWalletMethodType walletMethodType;

    public WalletKeyGenRequest() {}

    public WalletKeyGenRequest(String id, AlgorithmType.ALGORITHM_TYPE algoType, StorageOption.STORAGE_OPTION storageOption, KeyGenWalletMethodType walletMethodType) {
        super(id, algoType, storageOption);
        this.walletMethodType = walletMethodType;
    }

    public KeyGenWalletMethodType getWalletMethodType() {
        return walletMethodType;
    }

    public void setWalletMethodType(KeyGenWalletMethodType walletMethodType) {
        this.walletMethodType = walletMethodType;
    }
}
