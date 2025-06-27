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

package org.omnione.did.sdk.utility.Errors;

public enum UtilityErrorCode {

    /* COMMON ERROR */
    // ModuleCode(1) + StackCode(3) + ComponentCode(3) + ErrorCode(5)
    // ModuleCode - Mobile (M)
    // StackCode - SDK (SDK)
    // ComponentCode - Utility (UTL)
    ERR_CODE_INVALID_PARAMETER          (000, "Invalid parameter : "),


    /*  [00] CryptoUtils  */
    CRYPTO_UTILS_BASE("00"),
    ERR_CODE_CRYPTO_UTILS_INVALID_PARAMETER          (CRYPTO_UTILS_BASE,  ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_CREATE_RANDOM_KEY (CRYPTO_UTILS_BASE, 100, "Fail to create random key : "),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_DERIVE_PUBLIC_KEY (CRYPTO_UTILS_BASE, 101, "Fail to derive public key"),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_GENERATE_SHARED_SECRET_USING_ECDH (CRYPTO_UTILS_BASE, 102, "Fail to generate shared secret using ECDH : "),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_DERIVE_KEY_USING_PBKDF2 (CRYPTO_UTILS_BASE, 103, "Fail to derive key using PBKDF2"),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_CONVERT_PUBLIC_KEY_TO_EXTERNAL_REPRESENTATION (CRYPTO_UTILS_BASE, 200, "Fail to convert public key to external representation : "),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_CONVERT_PRIVATE_KEY_TO_EXTERNAL_REPRESENTATION (CRYPTO_UTILS_BASE, 201, "Fail to convert private key to external representation : "),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_CONVERT_PRIVATE_KEY_TO_OBJECT (CRYPTO_UTILS_BASE, 202, "Fail to convert private key to object : "),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_CONVERT_PUBLIC_KEY_TO_OBJECT (CRYPTO_UTILS_BASE, 203, "Fail to convert public key to object : "),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_ENCRYPT_USING_AES (CRYPTO_UTILS_BASE, 300, "Fail to encrypt using AES"),
    ERR_CODE_CRYPTO_UTILS_FAIL_TO_DECRYPT_USING_AES (CRYPTO_UTILS_BASE, 301, "Fail to decrypt using AES"),

    ERR_CODE_CRYPTO_UTILS_UNSUPPORTED_EC_TYPE (CRYPTO_UTILS_BASE, 900, "Unsupported ECType : "),

    /*  [01] MultibaseUtils  */
    MULTIBASE_UTILS_BASE("01"),
    ERR_CODE_MULTIBASE_UTILS_INVALID_PARAMETER          (MULTIBASE_UTILS_BASE,  ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_MULTIBASE_UTILS_FAIL_TO_DECODE (MULTIBASE_UTILS_BASE, 100, "Fail to decode"),
    ERR_CODE_MULTIBASE_UTILS_UNSUPPORTED_ENCODING_TYPE (MULTIBASE_UTILS_BASE, 900, "Unsupported encoding type : "),

    DIGEST_UTILS_BASE("02"),
    ERR_CODE_DIGEST_UTILS_INVALID_PARAMETER          (DIGEST_UTILS_BASE,  ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_DIGEST_UTILS_UNSUPPORTED_ALGORITHM_TYPE (MULTIBASE_UTILS_BASE, 900, "Unsupported algorithm type : ");

    private final String utilityCode = "MSDKUTL";
    private int code;
    private String msg;

    private String feature;

    public String getFeature(){
        return feature;
    }
    public String getMsg() {
        return msg;
    }
    public int getCode() {
        return code;
    }
    public String getUtilityCode() {
        return utilityCode;
    }
    UtilityErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    UtilityErrorCode(String feature){
        this.feature = feature;
    }
    UtilityErrorCode(UtilityErrorCode feature, UtilityErrorCode code){
        this.feature = feature.getFeature();
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    UtilityErrorCode(UtilityErrorCode feature, int addCode, String msg){
        this.feature = feature.getFeature();
        this.code = addCode;
        this.msg = msg;
    }


    UtilityErrorCode(String feature, int addCode, String msg){
        this.feature = feature;
        this.code = addCode;
    }

    public static UtilityErrorCode getEnumByCode(int code) {
        UtilityErrorCode[] type = UtilityErrorCode.values();
        for (UtilityErrorCode odiError : type) {
            if (odiError.getCode() == code) {
                return odiError;
            }
        }
        throw new AssertionError("Unknown Enum Code");
    }

}
