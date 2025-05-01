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

import org.omnione.did.sdk.datamodel.did.DIDKeyType;
import org.omnione.did.sdk.datamodel.util.StringEnum;

public class AlgorithmType {
    public enum ALGORITHM_TYPE implements StringEnum {
        RSA("Rsa"),
        SECP256K1("Secp256k1"),
        SECP256R1("Secp256r1");

        private String value;
        ALGORITHM_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static ALGORITHM_TYPE fromValue(String value) {
            for (ALGORITHM_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case RSA:   		return "Rsa";
                case SECP256K1:	    return "Secp256k1";
                case SECP256R1:	    return "Secp256r1";
                default:      		return "Unknown";
            }
        }

        public static DIDKeyType.DID_KEY_TYPE convertTo(ALGORITHM_TYPE type){
            switch (type) {
                case RSA:
                    return DIDKeyType.DID_KEY_TYPE.rsaVerificationKey2018;
                case SECP256K1:
                    return DIDKeyType.DID_KEY_TYPE.secp256k1VerificationKey2018;
                case SECP256R1:
                    return DIDKeyType.DID_KEY_TYPE.secp256r1VerificationKey2018;
                default:
                    throw new RuntimeException();
            }
        }
    }

}
