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

package org.omnione.did.sdk.communication.exception;

public enum CommunicationErrorCode {

    /* COMMON ERROR */
    // ModuleCode(1) + StackCode(3) + ComponentCode(3) + ErrorCode(5)
    // ModuleCode - Mobile (M)
    // StackCode - SDK (SDK)
    // ComponentCode - Communication (CMM)

    COMMUNICATION_BASE("00"),
    ERR_CODE_COMMUNICATION_UNKNOWN (COMMUNICATION_BASE, 000, "unknown error"),
    ERR_CODE_COMMUNICATION_INVALID_PARAMETER          (COMMUNICATION_BASE,  001, "Invalid parameter : "),
    ERR_CODE_COMMUNICATION_INCORRECT_URL_CONNECTION (COMMUNICATION_BASE, 002, "Incorrect url connection : "),
    ERR_CODE_COMMUNICATION_SERVER_FAIL (COMMUNICATION_BASE, 003, "ServerFail : ");

    private final String communicationCode = "MSDKCMM";
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
    public String getCommunicationCode() {
        return communicationCode;
    }
    CommunicationErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    CommunicationErrorCode(String feature){
        this.feature = feature;
    }
    CommunicationErrorCode(CommunicationErrorCode feature, CommunicationErrorCode code){
        this.feature = feature.getFeature();
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    CommunicationErrorCode(CommunicationErrorCode feature, int addCode, String msg){
        this.feature = feature.getFeature();
        this.code = addCode;
        this.msg = msg;
    }


    CommunicationErrorCode(String feature, int addCode, String msg){
        this.feature = feature;
        this.code = addCode;
    }

    public static CommunicationErrorCode getEnumByCode(int code) {
        CommunicationErrorCode[] type = CommunicationErrorCode.values();
        for (CommunicationErrorCode odiError : type) {
            if (odiError.getCode() == code) {
                return odiError;
            }
        }
        throw new AssertionError("Unknown Enum Code");
    }

}
