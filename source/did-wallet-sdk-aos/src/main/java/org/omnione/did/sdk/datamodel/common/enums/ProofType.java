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

public class ProofType {
    public enum PROOF_TYPE implements StringEnum {
        rsaSignature2018("RsaSignature2018"),
        secp256k1Signature2018("Secp256k1Signature2018"),
        secp256r1Signature2018("Secp256r1Signature2018");

        final private String value;
        PROOF_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static PROOF_TYPE fromValue(String value) {
            for (PROOF_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case rsaSignature2018:
                    return "RsaSignature2018";
                case secp256k1Signature2018:
                    return "Secp256k1Signature2018";
                case secp256r1Signature2018:
                    return "Secp256r1Signature2018";
                default:
                    return "Unknown";
            }
        }

        public static PROOF_TYPE convertFrom(AlgorithmType.ALGORITHM_TYPE type){
            switch (type) {
                case RSA:
                    return PROOF_TYPE.rsaSignature2018;
                case SECP256K1:
                    return PROOF_TYPE.secp256k1Signature2018;
                case SECP256R1:
                    return PROOF_TYPE.secp256r1Signature2018;
                default:
                    throw new RuntimeException();
            }
        }
        public static AlgorithmType.ALGORITHM_TYPE convertTo(PROOF_TYPE type){
            switch (type) {
                case rsaSignature2018:
                    return AlgorithmType.ALGORITHM_TYPE.RSA;
                case secp256k1Signature2018:
                    return AlgorithmType.ALGORITHM_TYPE.SECP256K1;
                case secp256r1Signature2018:
                    return AlgorithmType.ALGORITHM_TYPE.SECP256R1;
                default:
                    throw new RuntimeException();
            }
        }

    }
}
