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

package org.omnione.did.sdk.utility.DataModels;

public class EcType {
    public enum EC_TYPE {
        SECP256_K1("Secp256k1"),
        SECP256_R1("Secp256r1");

        private String value;
        EC_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static EC_TYPE fromValue(String value) {
            for (EC_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case SECP256_K1:	    return "Secp256k1";
                case SECP256_R1:	    return "Secp256r1";
                default:      		return "Unknown";
            }
        }
    }

}
