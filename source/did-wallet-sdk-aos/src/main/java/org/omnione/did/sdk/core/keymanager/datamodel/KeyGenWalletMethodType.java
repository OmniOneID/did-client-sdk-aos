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

public class KeyGenWalletMethodType {
    @SerializedName("walletMethodType")
    @Expose
    private WALLET_METHOD_TYPE walletMethodType;
    @SerializedName("pin")
    @Expose
    private String pin;
    // 생성자
    public KeyGenWalletMethodType() {
        walletMethodType = WALLET_METHOD_TYPE.NONE;
    }

    public KeyGenWalletMethodType(String pin) {
        walletMethodType = WALLET_METHOD_TYPE.PIN;
        this.pin = pin;
    }


    public static KeyGenWalletMethodType pin(String pin) {
        KeyGenWalletMethodType walletMethodType = new KeyGenWalletMethodType();
        walletMethodType.pin = pin;
        return walletMethodType;
    }

    public String getPin() {
        return pin;
    }

    public WALLET_METHOD_TYPE getWalletMethodType() {
        return walletMethodType;
    }

    public void setWalletMethodType(WALLET_METHOD_TYPE walletMethodType) {
        this.walletMethodType = walletMethodType;
    }

    public enum WALLET_METHOD_TYPE {

        NONE("NONE"),
        PIN("PIN");

        final private String value;
        WALLET_METHOD_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static WALLET_METHOD_TYPE fromValue(String value) {
            for (WALLET_METHOD_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case NONE:
                    return "NONE";
                case PIN:
                    return "PIN";
                default:
                    return "Unknown";
            }
        }
    }
}
