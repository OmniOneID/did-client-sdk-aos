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

import org.omnione.did.sdk.datamodel.util.IntEnum;

public class StorageOption {
    public enum STORAGE_OPTION implements IntEnum {
        WALLET(0),
        KEYSTORE(1);

        private int value;

        STORAGE_OPTION(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }

        public static STORAGE_OPTION fromValue(int value){
            for(STORAGE_OPTION option : values()){
                if(option.getValue() == value){
                    return option;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case WALLET:   		return "WALLET";
                case KEYSTORE:   	    return "KEYSTORE";
                default:      		return "Unknown";
            }
        }
    }



}
