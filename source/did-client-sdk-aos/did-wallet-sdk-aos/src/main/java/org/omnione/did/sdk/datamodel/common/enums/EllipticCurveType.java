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

public class EllipticCurveType {
    public enum ELLIPTIC_CURVE_TYPE implements StringEnum {
        SECP256K1("Secp256k1"),
        SECP256R1("Secp256r1");

        final private String value;
        ELLIPTIC_CURVE_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static ELLIPTIC_CURVE_TYPE fromValue(String value) {
            for (ELLIPTIC_CURVE_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case SECP256K1:
                    return "Secp256k1";
                case SECP256R1:
                    return "Secp256r1";
                default:
                    return "Unknown";
            }
        }

        public static ELLIPTIC_CURVE_TYPE convertFrom(AlgorithmType.ALGORITHM_TYPE type){
            switch (type) {
                case SECP256K1:
                    return ELLIPTIC_CURVE_TYPE.SECP256K1;
                case SECP256R1:
                    return ELLIPTIC_CURVE_TYPE.SECP256R1;
                default:
                    throw new RuntimeException();
            }
        }
        public static AlgorithmType.ALGORITHM_TYPE convertTo(ELLIPTIC_CURVE_TYPE type){
            switch (type) {
                case SECP256K1:
                    return AlgorithmType.ALGORITHM_TYPE.SECP256K1;
                case SECP256R1:
                    return AlgorithmType.ALGORITHM_TYPE.SECP256R1;
                default:
                    throw new RuntimeException();
            }
        }
    }
}
