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

package org.omnione.did.sdk.core.exception;

public enum WalletCoreErrorCode {

    /* COMMON ERROR */
    // ModuleCode(1) + StackCode(3) + ComponentCode(3) + ErrorCode(4)
    // ModuleCode - Mobile (M)
    // StackCode - SDK (SDK)
    // ComponentCode - WalletService (WLS)

//    WALLET_CORE_PUBLIC("0"),
//    WALLET_CORE_PRIVATE("1"),
    ERR_CODE_INVALID_PARAMETER          (000, "Invalid parameter : "),
    ERR_CODE_DUPLICATED_PARAMETER       (001, "Duplicated parameter : "),
    ERR_CODE_FAIL_TO_DECODE             (002, "Fail to decode : "),


    /* [00] KEY_MANAGER  */
    //PUBLIC
    KEY_MANAGER_BASE("00"),
    ERR_CODE_KEY_MANAGER_INVALID_PARAMETER          (KEY_MANAGER_BASE,  ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_KEY_MANAGER_DUPLICATED_PARAMETER       (KEY_MANAGER_BASE, ERR_CODE_DUPLICATED_PARAMETER),
    ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE     (KEY_MANAGER_BASE, ERR_CODE_FAIL_TO_DECODE),
    ERR_CODE_KEY_MANAGER_EXIST_KEY_ID               (KEY_MANAGER_BASE, 100, "Given key id already exists"),
    ERR_CODE_KEY_MANAGER_NOT_CONFORM_TO_KEY_GEN_REQUEST   (KEY_MANAGER_BASE, 101, "Given keyGenRequest does not conform to {Wallet/Secure}KeyGenRequest"),
    ERR_CODE_KEY_MANAGER_NEW_PIN_EQUALS_OLD_PIN               (KEY_MANAGER_BASE, 200, "Given new pin is equal to old pin"),
    ERR_CODE_KEY_MANAGER_NOT_PIN_AUTH_TYPE          (KEY_MANAGER_BASE, 201,"Given key id is not pin auth type"),
    ERR_CODE_KEY_MANAGER_FOUND_NO_KEY_BY_KEY_TYPE       (KEY_MANAGER_BASE, 400,"Found no key by given keyType"),
    ERR_CODE_KEY_MANAGER_INSUFFICIENT_RESULT_BY_KEY_TYPE       (KEY_MANAGER_BASE, 401,"Insufficient result by given keyType"),
    ERR_CODE_KEY_MANAGER_UNSUPPORTED_ALGORITHM          (KEY_MANAGER_BASE, 900,"Given algorithm is unsupported"),


    /*  [01] DIDManager  */
    DID_MANAGER_BASE("01"),
    ERR_CODE_DID_MANAGER_INVALID_PARAMETER          (DID_MANAGER_BASE, ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_DID_MANAGER_DUPLICATED_PARAMETER       (DID_MANAGER_BASE,ERR_CODE_DUPLICATED_PARAMETER),
    ERR_CODE_DID_MANAGER_FAILED_TO_DECODE           (DID_MANAGER_BASE,ERR_CODE_FAIL_TO_DECODE),
    ERR_CODE_DID_MANAGER_FAIL_TO_GENERATE_RANDOM    (DID_MANAGER_BASE, 100, "Fail to generate random"),
    ERR_CODE_DID_MANAGER_DOCUMENT_IS_ALREADY_EXISTS    (DID_MANAGER_BASE, 200, "Document is already exists"),
    ERR_CODE_DID_MANAGER_DUPLICATE_KEY_ID_EXISTS_IN_VERIFICATION_METHOD    (DID_MANAGER_BASE, 300, "Duplicate key ID exists in verification method"),
    ERR_CODE_DID_MANAGER_NOT_FOUND_KEY_ID_IN_VERIFICATION_METHOD    (DID_MANAGER_BASE, 301, "Not found key ID in verification method"),
    ERR_CODE_DID_MANAGER_DUPLICATE_SERVICE_ID_EXISTS_IN_SERVICE    (DID_MANAGER_BASE, 302, "Duplicate service ID exists in service"),
    ERR_CODE_DID_MANAGER_NOT_FOUND_SERVICE_ID_IN_SERVICE    (DID_MANAGER_BASE, 303, "Not found service ID in service"),
    ERR_CODE_DID_MANAGER_DONT_CALL_RESET_CHANGES_IF_NO_DOCUMENT_SAVED    (DID_MANAGER_BASE, 304, "Don't call 'resetChanges' if no document saved"),
    ERR_CODE_DID_MANAGER_UNEXPECTED_CONDITION    (DID_MANAGER_BASE, 900, "Unexpected condition occurred"),


    /*  [02] VCManager  */
    VC_MANAGER_BASED("02"),
    ERR_CODE_VC_MANAGER_INVALID_PARAMETER                            (VC_MANAGER_BASED, ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_VC_MANAGER_DUPLICATED_PARAMETER                         (VC_MANAGER_BASED, ERR_CODE_DUPLICATED_PARAMETER),
    ERR_CODE_VC_MANAGER_FAIL_TO_DECODER                              (VC_MANAGER_BASED, ERR_CODE_FAIL_TO_DECODE),
    ERR_CODE_VC_MANAGER_NO_CLAIM_CODE_IN_CREDENTIAL_FOR_PRESENTATION (VC_MANAGER_BASED, 100, "No claim code in credential(VC) for presentation"),

    /*  [03] SecureEncryptor  */
    SECURE_ENCRYPTOR_BASE("03"),
    ERR_CODE_SECURE_ENCRYPTOR_INVALID_PARAMETER                      (SECURE_ENCRYPTOR_BASE, ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_SECURE_ENCRYPTOR_DUPLICATED_PARAMETER                   (SECURE_ENCRYPTOR_BASE,ERR_CODE_DUPLICATED_PARAMETER),
    ERR_CODE_SECURE_ENCRYPTOR_FAILED_TO_DECODE                       (SECURE_ENCRYPTOR_BASE,ERR_CODE_FAIL_TO_DECODE),

    // [04] ZKPManager
    ZKP_MANAGER_BASE("04"),

    ERR_CODE_ZKP_PARAMETER_VALID_FAIL                                    (ZKP_MANAGER_BASE, 000, "Invalid parameter"),
    ERR_CODE_ZKP_DUPLICATED_PARAMETER                                    (ZKP_MANAGER_BASE, 001, "Duplicated parameter"),
    ERR_CODE_ZKP_FAIL_TO_DECODE                                          (ZKP_MANAGER_BASE, 002, "Fail to decode"),

    // common
    ERR_CODE_ZKP_NULL                                                    (ZKP_MANAGER_BASE,100,"Data is null"),
    ERR_CODE_ZKP_NEGATIVE_DELTA                                          (ZKP_MANAGER_BASE,101,"Delta value must be positive"),
    ERR_CODE_ZKP_BIG_NUMBER_COMPARE_FAIL                                 (ZKP_MANAGER_BASE,102,"Failed to compare big numbers"),
    ERR_CODE_ZKP_BIG_NUMBER_FROM_BYTE_FAIL                               (ZKP_MANAGER_BASE,104,"Failed to convert byte to big number"),
    ERR_CODE_ZKP_BIG_NUMBER_TO_BYTE_FAIL                                 (ZKP_MANAGER_BASE,105,"Failed to convert big number to byte"),

    ERR_CODE_ZKP_IO                                                      (ZKP_MANAGER_BASE,106,"Error IO"),
    ERR_CODE_ZKP_NOT_SUPPORTED_TYPE                                      (ZKP_MANAGER_BASE,107,"Not supported type"),

    // master secret
    ERR_CODE_ZKP_PROVER_SELECT_MASTER_SECRET_FROM_WALLET_FAIL            (ZKP_MANAGER_BASE,200,"Master secret not found"),
    ERR_CODE_ZKP_PROVER_GENERATE_MASTER_SECRET_FAIL                      (ZKP_MANAGER_BASE,201,"Failed to generate master secret"),

    // issue credential
    ERR_CODE_ZKP_PROVER_CHECK_SIGNATURE_CORRECTNESS_PROOF_FAIL           (ZKP_MANAGER_BASE,300,"Failed to verify signature correctness proof"),

    // get credential
    ERR_CODE_ZKP_PROVER_NOT_FOUND_CREDENTIAL_STORED                      (ZKP_MANAGER_BASE,400,"Credential not found in storage"),
    ERR_CODE_ZKP_PROVER_NOT_FOUND_CREDENTIAL_BY_IDENTIFIERS              (ZKP_MANAGER_BASE,401,"Credential not found by identifiers in storage"),

    // search credential
    ERR_CODE_ZKP_PROVER_PREDICATE_MUST_HAVE_RESTRICTIONS                 (ZKP_MANAGER_BASE,500,"Proof request's predicate must have restrictions"),
    ERR_CODE_ZKP_PROVER_NOT_FOUND_AVAILABLE_REQUEST_ATTRIBUTE            (ZKP_MANAGER_BASE,501,"Not found available request attribute"),
    ERR_CODE_ZKP_PROVER_NOT_FOUND_AVAILABLE_PREDICATE_ATTRIBUTE          (ZKP_MANAGER_BASE,502,"Not found available predicate attribute"),

    // proof
    ERR_CODE_ZKP_PROVER_BUILD_CREDENTIAL_FOR_PROVING_FAIL                (ZKP_MANAGER_BASE,602,"Failed to build credential for proving"),
    ERR_CODE_ZKP_PROVER_NOT_FOUND_SCHEMA_FROM_LIST                       (ZKP_MANAGER_BASE,603,"Not found schema from list"),
    ERR_CODE_ZKP_PROVER_INSUFFICIENT_PROOF_REQUEST_REFERENTS             (ZKP_MANAGER_BASE,604,"Insufficient referents for proof request"),
    ERR_CODE_ZKP_DUPLICATED                                              (ZKP_MANAGER_BASE,605,"Duplicated key"),

    // integrated
    ERR_CODE_ZKP_CALCULATE_TEQ_FAIL                                      (ZKP_MANAGER_BASE,900, "Failed to calculate TEQ proof"),
    ERR_CODE_ZKP_CALCULATE_TNE_FAIL                                      (ZKP_MANAGER_BASE,901, "Failed to calculate TNE proof"),
    ERR_CODE_ZKP_PROVER_INITIALIZE_PRIMARY_EQ_PROOF_FAIL                 (ZKP_MANAGER_BASE,902,"Failed to initialize primary equal proof"),
    ERR_CODE_ZKP_PROVER_INITIALIZE_PRIMARY_NE_PROOF_FAIL                 (ZKP_MANAGER_BASE,903,"Failed to initialize primary non-equal proof"),
    ERR_CODE_ZKP_PROVER_FINALIZE_PRIMARY_EQ_PROOF_FAIL                   (ZKP_MANAGER_BASE,904,"Failed to finalize primary equal proof"),
    ERR_CODE_ZKP_PROVER_FINALIZE_PRIMARY_NE_PROOF_FAIL                   (ZKP_MANAGER_BASE,905,"Failed to finalize primary non-equal proof"),


    /*  [10] StorageManager  */
    STORAGE_MANAGER_BASE("10"),
    ERR_CODE_STORAGE_MANAGER_INVALID_PARAMETER          (STORAGE_MANAGER_BASE,  ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_STORAGE_MANAGER_DUPLICATED_PARAMETER       (STORAGE_MANAGER_BASE, ERR_CODE_DUPLICATED_PARAMETER),
    ERR_CODE_STORAGE_MANAGER_FAILED_TO_DECODE           (STORAGE_MANAGER_BASE, ERR_CODE_FAIL_TO_DECODE),
    ERR_CODE_STORAGE_MANAGER_FAIL_TO_SAVE_WALLET        (STORAGE_MANAGER_BASE, 100, "Fail to save wallet : "),
    ERR_CODE_STORAGE_MANAGER_ITEM_DUPLICATED_WITH_IT_IN_WALLET  (STORAGE_MANAGER_BASE, 101, "Item duplicated with it in wallet"),
    ERR_CODE_STORAGE_MANAGER_NO_ITEM_TO_UPDATE  (STORAGE_MANAGER_BASE, 200, "No item to update in wallet"),
    ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_REMOVE (STORAGE_MANAGER_BASE, 300, "No items to remove in wallet"),
    ERR_CODE_STORAGE_MANAGER_FAIL_TO_REMOVE_ITEMS   (STORAGE_MANAGER_BASE, 301, "Fail to remove items from wallet"),
    ERR_CODE_STORAGE_MANAGER_NO_ITEMS_SAVED (STORAGE_MANAGER_BASE, 400, "No items saved in wallet"),
    ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND   (STORAGE_MANAGER_BASE, 401, "No items to find in wallet"),
    ERR_CODE_STORAGE_MANAGER_FAIL_TO_READ_WALLET_FILE   (STORAGE_MANAGER_BASE, 402, "Fail to read wallet file : "),
    ERR_CODE_STORAGE_MANAGER_MALFORMED_EXTERNAL_WALLET(STORAGE_MANAGER_BASE, 500, "Malformed external wallet format : "),
    ERR_CODE_STORAGE_MANAGER_MALFORMED_WALLET_SIGNATURE(STORAGE_MANAGER_BASE, 501, "Malformed wallet signature"),
    ERR_CODE_STORAGE_MANAGER_MALFORMED_INNER_WALLET(STORAGE_MANAGER_BASE, 502, "Malformed inner wallet format : "),
    ERR_CODE_STORAGE_MANAGER_MALFORMED_ITEM_TYPE(STORAGE_MANAGER_BASE, 503, "Malformed item object type about item of inner wallet : "),
    ERR_CODE_STORAGE_MANAGER_UNEXPECTED_ERROR(STORAGE_MANAGER_BASE, 900, "Unexpected error occurred : "),

    /*  [11] Signable  */
    SIGNABLE_BASE("11"),
    ERR_CODE_SIGNABLE_INVALID_PARAMETER                     (SIGNABLE_BASE, ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_SIGNABLE_DUPLICATED_PARAMETER                  (SIGNABLE_BASE,ERR_CODE_DUPLICATED_PARAMETER),
    ERR_CODE_SIGNABLE_FAILED_TO_DECODE                       (SIGNABLE_BASE,ERR_CODE_FAIL_TO_DECODE),
    ERR_CODE_SIGNABLE_INVALID_PUBLIC_KEY    (SIGNABLE_BASE, 100, "Not proper public key format"),
    ERR_CODE_SIGNABLE_INVALID_PRIVATE_KEY   (SIGNABLE_BASE, 101, "Not proper private key format"),
    ERR_CODE_SIGNABLE_NOT_DERIVED_KEY_FROM_PRIVATE_KEY      (SIGNABLE_BASE, 102,"Private and public keys are not pair"),
    ERR_CODE_SIGNABLE_FAIL_TO_CONVERT_COMPACT_REPRESENTATION(SIGNABLE_BASE, 200, "Converting failed to compact representation"),
    ERR_CODE_SIGNABLE_CREATE_SIGNATURE(SIGNABLE_BASE, 201, "Signing failed : "),
    ERR_CODE_SIGNABLE_VERIFY_SIGNATURE_FAILED(SIGNABLE_BASE, 202, "Failed to verify signature : "),


    /*  [12] KeystoreManager  */
    KEYSTORE_MANAGER_BASE("12"),
    ERR_CODE_KEYSTORE_MANAGER_INVALID_PARAMETER          (KEYSTORE_MANAGER_BASE,  ERR_CODE_INVALID_PARAMETER),
    ERR_CODE_KEYSTORE_MANAGER_DUPLICATED_PARAMETER       (KEYSTORE_MANAGER_BASE, ERR_CODE_DUPLICATED_PARAMETER),
    ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_DECODE           (KEYSTORE_MANAGER_BASE, ERR_CODE_FAIL_TO_DECODE),
    ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_CREATE_SECURE_KEY           (KEYSTORE_MANAGER_BASE, 100, "Failed to create secure key : "),
    ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_COPY_PUBLIC_KEY           (KEYSTORE_MANAGER_BASE, 101, "Failed to copy public key"),
    ERR_CODE_KEYSTORE_MANAGER_PUBLIC_KEY_REPRESENTATION           (KEYSTORE_MANAGER_BASE, 102, "Failed to get public key representation : "),
    ERR_CODE_KEYSTORE_MANAGER_CANNOT_FIND_SECURE_KEY_BY_CONDITION           (KEYSTORE_MANAGER_BASE, 103, "Cannot find secure key by given conditions"),
    ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_DELETE_SECURE_KEY          (KEYSTORE_MANAGER_BASE, 104, "Failed to delete secure key"),
    ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_SIGN          (KEYSTORE_MANAGER_BASE, 200, "Signing failed : "),
    ERR_CODE_KEYSTORE_MANAGER_CANNOT_CREATE_ENCRYPTED_DATA          (KEYSTORE_MANAGER_BASE, 300, "Cannot create encrypted data : "),
    ERR_CODE_KEYSTORE_MANAGER_CANNOT_CREATE_DECRYPTED_DATA          (KEYSTORE_MANAGER_BASE, 301, "Cannot create decrypted data : "),
    ERR_CODE_KEYSTORE_MANAGER_NO_REGISTERED_BIO_AUTH_INFO            (KEYSTORE_MANAGER_BASE, 400, "No registered bio authentication information");



    private final String walletCoreCode = "MSDKWLT";
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
    public String getWalletCoreCode() {
        return walletCoreCode;
    }
    WalletCoreErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    WalletCoreErrorCode(String feature){
        this.feature = feature;
    }
    WalletCoreErrorCode(WalletCoreErrorCode feature, WalletCoreErrorCode code){
        this.feature = feature.getFeature();
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    WalletCoreErrorCode(WalletCoreErrorCode feature, int addCode, String msg){
        this.feature = feature.getFeature();
        this.code = addCode;
        this.msg = msg;
    }


    WalletCoreErrorCode(String feature, int addCode, String msg){
        this.feature = feature;
        this.code = addCode;
    }

    public static WalletCoreErrorCode getEnumByCode(int code) {
        WalletCoreErrorCode[] type = WalletCoreErrorCode.values();
        for (WalletCoreErrorCode errorCode : type) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        throw new AssertionError("Unknown Enum Code");
    }

}
