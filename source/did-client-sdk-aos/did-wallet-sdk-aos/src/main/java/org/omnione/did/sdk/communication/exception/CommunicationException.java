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

public class CommunicationException extends Exception {
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

    public CommunicationException(CommunicationErrorCode CommunicationErrorCode){
        super("[" + CommunicationErrorCode.getCommunicationCode() + CommunicationErrorCode.getFeature() + String.format("%03d", CommunicationErrorCode.getCode()) +"] " + CommunicationErrorCode.getMsg());
        this.errorCode = CommunicationErrorCode.getCode();
        this.errMsg = CommunicationErrorCode.getMsg();
    }
    public CommunicationException(CommunicationErrorCode CommunicationErrorCode, String paramName){
        super("[" + CommunicationErrorCode.getCommunicationCode() + CommunicationErrorCode.getFeature() + String.format("%03d", CommunicationErrorCode.getCode()) +"] " + CommunicationErrorCode.getMsg() + paramName);
        this.errorCode = CommunicationErrorCode.getCode();
        this.errMsg = CommunicationErrorCode.getMsg() + paramName;
    }
    public CommunicationException(CommunicationErrorCode CommunicationErrorCode, Throwable throwable){
        super("[" + CommunicationErrorCode.getCommunicationCode() + CommunicationErrorCode.getFeature() + String.format("%03d", CommunicationErrorCode.getCode()) +"] , [Message] " + throwable.getMessage());
        this.errorCode = CommunicationErrorCode.getCode();
        this.errMsg = throwable.getMessage();
    }
    public CommunicationException(int code){
        super("[ErrorCode " + CommunicationErrorCode.getEnumByCode(code).getCode() + "] , [Message] " + CommunicationErrorCode.getEnumByCode(code).getMsg());
        this.errorCode = CommunicationErrorCode.getEnumByCode(code).getCode();
        this.errorReason = CommunicationErrorCode.getEnumByCode(code).getMsg();
    }
    public CommunicationException(int code, Throwable throwable){
        super("[ErrorCode " + CommunicationErrorCode.getEnumByCode(code).getCode() + "] , [Message] " + CommunicationErrorCode.getEnumByCode(code).getMsg(), throwable);
        this.errorCode = CommunicationErrorCode.getEnumByCode(code).getCode();
        this.errorReason = CommunicationErrorCode.getEnumByCode(code).getMsg();
    }
}
