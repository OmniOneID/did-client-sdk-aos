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

package org.omnione.did.sdk.wallet.walletservice.exception;

public enum WalletErrorCode {

    /* COMMON ERROR */
    // ModuleCode(1) + StackCode(3) + ComponentCode(3) + ErrorCode(4)
    // ModuleCode - Mobile (M)
    // StackCode - SDK (SDK)
    // ComponentCode - WalletService (WLS)

    /* [05] Wallet API  */
    WALLET_BASE("05"),

    ERR_CODE_WALLET_UNKNOWN(WALLET_BASE, 0, "Unknown error"),
    ERR_CODE_WALLET_FAIL(WALLET_BASE, 1, "General failure"),
    ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL(WALLET_BASE, 2, "Failed to verify parameter"),
    ERR_CODE_WALLET_SERIALIZE_FAIL(WALLET_BASE, 3, "Failed to serialize"),
    ERR_CODE_WALLET_DESERIALIZE_FAIL(WALLET_BASE, 4, "Failed to deserialize"),
    ERR_CODE_WALLET_CREATE_PROOF_FAIL(WALLET_BASE, 5, "Failed to create proof"),
    ERR_CODE_WALLET_ROLE_MATCH_FAIL(WALLET_BASE, 6, "Failed to match role"),
    ERR_CODE_WALLET_VERIFY_CERT_VC_FAIL(WALLET_BASE, 7, "Failed to verify certVC"),

    ERR_CODE_WALLET_VERIFY_TOKEN_FAIL(WALLET_BASE, 10, "Failed to verify token"),
    ERR_CODE_WALLET_CREATE_WALLET_TOKEN_FAIL(WALLET_BASE, 11, "Failed to create wallet token"),

    ERR_CODE_WALLET_LOCKED_WALLET(WALLET_BASE, 20, "Wallet is locked"),
    ERR_CODE_WALLET_NEW_PIN_EQUALS_OLD_PIN(WALLET_BASE, 21, "Given new pin is equal to old pin"),
    ERR_CODE_WALLET_NOT_LOCK_TYPE(WALLET_BASE, 22, "Wallet is not a lock-type"),

    ERR_CODE_WALLET_INSERT_QUERY_FAIL(WALLET_BASE, 30, "Failed to execute insert query"),
    ERR_CODE_WALLET_SELECT_QUERY_FAIL(WALLET_BASE, 31, "Failed to execute select query"),
    ERR_CODE_WALLET_DELETE_QUERY_FAIL(WALLET_BASE, 32, "Failed to execute delete query"),

    ERR_CODE_WALLET_CREATE_WALLET_FAIL(WALLET_BASE, 40, "Failed to create wallet"),
    ERR_CODE_WALLET_PERSONALIZED_FAIL(WALLET_BASE, 41, "Failed to personalize"),
    ERR_CODE_WALLET_DEPERSONALIZED_FAIL(WALLET_BASE, 42, "Failed to depersonalize"),
    ERR_CODE_WALLET_SAVE_KEYSTORE_FAIL(WALLET_BASE, 43, "Failed to save keystore"),
    ERR_CODE_WALLET_INCORRECT_PASSCODE(WALLET_BASE, 44, "Incorrect passcode"),
    ERR_CODE_WALLET_NOT_FOUND_WALLET_ID(WALLET_BASE, 45, "Wallet ID not found"),

    ERR_CODE_WALLET_REGISTER_USER_FAIL(WALLET_BASE, 50, "Failed to register user"),
    ERR_CODE_WALLET_RESTORE_USER_FAIL(WALLET_BASE, 51, "Failed to restore user"),
    ERR_CODE_WALLET_DID_MATCH_FAIL(WALLET_BASE, 52, "Failed to match DID"),
    ERR_CODE_WALLET_UPDATE_USER_FAIL(WALLET_BASE, 53, "Failed to update user"),

    ERR_CODE_WALLET_ISSUE_CREDENTIAL_FAIL(WALLET_BASE, 60, "Failed to issue credential"),
    ERR_CODE_WALLET_REVOKE_CREDENTIAL_FAIL(WALLET_BASE, 61, "Failed to revoke credential"),

    ERR_CODE_WALLET_SUBMIT_CREDENTIAL_FAIL(WALLET_BASE, 70, "Failed to submit credential");


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
    WalletErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    WalletErrorCode(String feature){
        this.feature = feature;
    }
    WalletErrorCode(WalletErrorCode feature, WalletErrorCode code){
        this.feature = feature.getFeature();
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    WalletErrorCode(WalletErrorCode feature, int addCode, String msg){
        this.feature = feature.getFeature();
        this.code = addCode;
        this.msg = msg;
    }


    WalletErrorCode(String feature, int addCode, String msg){
        this.feature = feature;
        this.code = addCode;
    }

    public static WalletErrorCode getEnumByCode(int code) {
        WalletErrorCode[] type = WalletErrorCode.values();
        for (WalletErrorCode errorCode : type) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        throw new AssertionError("Unknown Enum Code");
    }

}
