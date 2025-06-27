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

package org.omnione.did.sdk.core.storagemanager.datamodel;

public class FileExtension {
    public enum FILE_EXTENSION {
        KEY("key"),
        DID("did"),
        VC("vc"),
        ZKP("zkp");

        private String value;

        FILE_EXTENSION(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static FILE_EXTENSION fromValue(String value) {
            for (FILE_EXTENSION type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case KEY:
                    return "key";
                case DID:
                    return "did";
                case VC:
                    return "vc";
                case ZKP:
                    return "zkp";
                default:
                    return "Unknown";
            }
        }
    }

}
