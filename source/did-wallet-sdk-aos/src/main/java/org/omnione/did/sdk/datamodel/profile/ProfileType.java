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

package org.omnione.did.sdk.datamodel.profile;

import org.omnione.did.sdk.datamodel.util.StringEnum;

public class ProfileType {
    public enum PROFILE_TYPE implements StringEnum {
        issueProfile("IssueProfile"),
        verifyProfile("VerifyProfile");

        final private String value;
        PROFILE_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static PROFILE_TYPE fromValue(String value) {
            for (PROFILE_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case issueProfile:
                    return "IssueProfile";
                case verifyProfile:
                    return "VerifyProfile";
                default:
                    return "Unknown";
            }
        }
    }
}
