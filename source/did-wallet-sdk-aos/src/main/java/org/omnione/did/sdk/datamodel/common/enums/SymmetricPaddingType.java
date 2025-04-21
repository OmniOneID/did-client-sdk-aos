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

public class SymmetricPaddingType {
    public enum SYMMETRIC_PADDING_TYPE implements StringEnum {
        NOPAD("NOPAD"),
        PKCS5("PKCS5");
        final private String value;
        SYMMETRIC_PADDING_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static SYMMETRIC_PADDING_TYPE fromValue(String value) {
            for (SYMMETRIC_PADDING_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case NOPAD:
                    return "NOPAD";
                case PKCS5:
                    return "PKCS5";
                default:
                    return "Unknown";
            }
        }
    }
}
