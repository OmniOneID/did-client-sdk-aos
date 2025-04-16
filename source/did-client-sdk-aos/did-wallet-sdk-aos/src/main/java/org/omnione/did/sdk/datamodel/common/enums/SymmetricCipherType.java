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

package org.omnione.did.sdk.datamodel.common.enums;

import org.omnione.did.sdk.datamodel.util.StringEnum;

public class SymmetricCipherType {
    public enum SYMMETRIC_CIPHER_TYPE implements StringEnum {
        AES128CBC("AES-128-CBC"),
        AES128ECB("AES-128-ECB"),
        AES256CBC("AES-256-CBC"),
        AES256ECB("AES-256-ECB");
        final private String value;
        SYMMETRIC_CIPHER_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static SYMMETRIC_CIPHER_TYPE fromValue(String value) {
            for (SYMMETRIC_CIPHER_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case AES128CBC:
                    return "AES-128-CBC";
                case AES128ECB:
                    return "AES-128-ECB";
                case AES256CBC:
                    return "AES-256-CBC";
                case AES256ECB:
                    return "AES-256-ECB";
                default:
                    return "Unknown";
            }
        }
    }
}
