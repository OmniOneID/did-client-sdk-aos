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

package org.omnione.did.sdk.datamodel.did;

import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.datamodel.util.StringEnum;

public class DIDKeyType {
    public enum DID_KEY_TYPE implements StringEnum {
        rsaVerificationKey2018("RsaVerificationKey2018"),
        secp256k1VerificationKey2018("Secp256k1VerificationKey2018"),
        secp256r1VerificationKey2018("Secp256r1VerificationKey2018");

        final private String value;
        DID_KEY_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static DID_KEY_TYPE fromValue(String value) {
            for (DID_KEY_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case rsaVerificationKey2018:
                    return "RsaVerificationKey2018";
                case secp256k1VerificationKey2018:
                    return "Secp256k1VerificationKey2018";
                case secp256r1VerificationKey2018:
                    return "Secp256r1VerificationKey2018";
                default:
                    return "Unknown";
            }
        }

        public static DID_KEY_TYPE convertFrom(AlgorithmType.ALGORITHM_TYPE type){
            switch (type) {
                case RSA:
                    return DID_KEY_TYPE.rsaVerificationKey2018;
                case SECP256K1:
                    return DID_KEY_TYPE.secp256k1VerificationKey2018;
                case SECP256R1:
                    return DID_KEY_TYPE.secp256r1VerificationKey2018;
                default:
                    throw new RuntimeException();
            }
        }
        public static AlgorithmType.ALGORITHM_TYPE convertTo(DID_KEY_TYPE type){
            switch (type) {
                case rsaVerificationKey2018:
                    return AlgorithmType.ALGORITHM_TYPE.RSA;
                case secp256k1VerificationKey2018:
                    return AlgorithmType.ALGORITHM_TYPE.SECP256K1;
                case secp256r1VerificationKey2018:
                    return AlgorithmType.ALGORITHM_TYPE.SECP256R1;
                default:
                    throw new RuntimeException();
            }
        }
    }
}
