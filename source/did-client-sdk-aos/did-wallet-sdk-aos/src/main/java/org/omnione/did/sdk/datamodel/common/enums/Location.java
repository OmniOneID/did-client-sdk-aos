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

public class Location {
    public enum LOCATION {
        inline("inline"),
        remote("remote"),
        attach("attach");

        final private String value;
        LOCATION(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static LOCATION fromValue(String value) {
            for (LOCATION type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case inline:
                    return "inline";
                case remote:
                    return "remote";
                case attach:
                    return "attach";
                default:
                    return "Unknown";
            }
        }
    }
}
