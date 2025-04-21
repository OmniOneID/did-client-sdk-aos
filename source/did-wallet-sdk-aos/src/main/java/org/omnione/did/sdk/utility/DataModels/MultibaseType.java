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

public class MultibaseType {
    public enum MULTIBASE_TYPE {
        BASE_16("f"),
        BASE_16_UPPER("F"),
        BASE_58_BTC("z"),
        BASE_64("m"),
        BASE_64_URL("u");

        private String value;

        MULTIBASE_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static MULTIBASE_TYPE fromValue(String value) {
            for (MULTIBASE_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case BASE_16:
                    return "f";
                case BASE_16_UPPER:
                    return "F";
                case BASE_58_BTC:
                    return "z";
                case BASE_64:
                    return "m";
                case BASE_64_URL:
                    return "u";
                default:
                    return "Unknown";
            }
        }
    }

}
