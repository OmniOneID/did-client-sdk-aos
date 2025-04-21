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

package org.omnione.did.sdk.core.didmanager.datamodel;

import org.omnione.did.sdk.datamodel.util.IntEnum;

public class DIDMethodType {
    public enum DID_METHOD_TYPE implements IntEnum {
        assertionMethod(1),
        authentication(1<<1),
        keyAgreement(1<<2),
        capabilityInvocation(1<<3),
        capabilityDelegation(1<<4);

        final private int value;
        DID_METHOD_TYPE(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }

        public static DID_METHOD_TYPE fromValue(int value) {
            for (DID_METHOD_TYPE type : values()) {
                if (type.getValue()== value) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case assertionMethod:
                    return "assertionMethod";
                case authentication:
                    return "authentication";
                case keyAgreement:
                    return "keyAgreement";
                case capabilityInvocation:
                    return "capabilityInvocation";
                case capabilityDelegation:
                    return "capabilityDelegation";
                default:
                    return "Unknown";
            }
        }

    }
}
