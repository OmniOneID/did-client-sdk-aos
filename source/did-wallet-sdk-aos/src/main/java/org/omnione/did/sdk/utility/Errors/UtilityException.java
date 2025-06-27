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

public class UtilityException extends Exception {
    protected int errorCode;
    protected String errMsg;
    protected String errorReason;

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public UtilityException(UtilityErrorCode UtilityErrorCode){
        super("[" + UtilityErrorCode.getUtilityCode() + UtilityErrorCode.getFeature() + String.format("%03d", UtilityErrorCode.getCode()) +"] " + UtilityErrorCode.getMsg());
        this.errorCode = UtilityErrorCode.getCode();
        this.errMsg = UtilityErrorCode.getMsg();
    }
    public UtilityException(UtilityErrorCode UtilityErrorCode, String paramName){
        super("[" + UtilityErrorCode.getUtilityCode() + UtilityErrorCode.getFeature() + String.format("%03d", UtilityErrorCode.getCode()) +"] " + UtilityErrorCode.getMsg() + paramName);
        this.errorCode = UtilityErrorCode.getCode();
        this.errMsg = UtilityErrorCode.getMsg() + paramName;
    }
    public UtilityException(UtilityErrorCode UtilityErrorCode, Throwable throwable){
        super("[" + UtilityErrorCode.getUtilityCode() + UtilityErrorCode.getFeature() + String.format("%03d", UtilityErrorCode.getCode()) +"] , [Message] " + throwable.getMessage());
        this.errorCode = UtilityErrorCode.getCode();
        this.errMsg = throwable.getMessage();
    }
    public UtilityException(int code){
        super("[ErrorCode " + UtilityErrorCode.getEnumByCode(code).getCode() + "] , [Message] " + UtilityErrorCode.getEnumByCode(code).getMsg());
        this.errorCode = UtilityErrorCode.getEnumByCode(code).getCode();
        this.errorReason = UtilityErrorCode.getEnumByCode(code).getMsg();
    }
    public UtilityException(int code, Throwable throwable){
        super("[ErrorCode " + UtilityErrorCode.getEnumByCode(code).getCode() + "] , [Message] " + UtilityErrorCode.getEnumByCode(code).getMsg(), throwable);
        this.errorCode = UtilityErrorCode.getEnumByCode(code).getCode();
        this.errorReason = UtilityErrorCode.getEnumByCode(code).getMsg();
    }
}
