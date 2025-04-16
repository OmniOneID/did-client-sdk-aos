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

public class WalletCoreException extends Exception {
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

    public WalletCoreException(WalletCoreErrorCode WalletCoreErrorCode){
        super("[" + WalletCoreErrorCode.getWalletCoreCode() + WalletCoreErrorCode.getFeature() + String.format("%03d", WalletCoreErrorCode.getCode()) +"] " + WalletCoreErrorCode.getMsg());
        this.errorCode = WalletCoreErrorCode.getCode();
        this.errMsg = WalletCoreErrorCode.getMsg();
    }
    public WalletCoreException(WalletCoreErrorCode WalletCoreErrorCode, String paramName){
        super("["+ WalletCoreErrorCode.getWalletCoreCode() + WalletCoreErrorCode.getFeature() + String.format("%03d", WalletCoreErrorCode.getCode()) +"] " + WalletCoreErrorCode.getMsg() + paramName);
        this.errorCode = WalletCoreErrorCode.getCode();
        this.errMsg = WalletCoreErrorCode.getMsg() + paramName;
    }
    public WalletCoreException(WalletCoreErrorCode WalletCoreErrorCode, Throwable throwable){
        super("[" + WalletCoreErrorCode.getWalletCoreCode() + WalletCoreErrorCode.getFeature() + String.format("%03d", WalletCoreErrorCode.getCode()) +"] , [Message] " + throwable.getMessage());
        this.errorCode = WalletCoreErrorCode.getCode();
        this.errMsg = throwable.getMessage();
    }
    public WalletCoreException(int code){
        super("[ErrorCode " + WalletCoreErrorCode.getEnumByCode(code).getCode() + "] , [Message] " + WalletCoreErrorCode.getEnumByCode(code).getMsg());
        this.errorCode = WalletCoreErrorCode.getEnumByCode(code).getCode();
        this.errorReason = WalletCoreErrorCode.getEnumByCode(code).getMsg();
    }
    public WalletCoreException(int code, Throwable throwable){
        super("[ErrorCode " + WalletCoreErrorCode.getEnumByCode(code).getCode() + "] , [Message] " + WalletCoreErrorCode.getEnumByCode(code).getMsg(), throwable);
        this.errorCode = WalletCoreErrorCode.getEnumByCode(code).getCode();
        this.errorReason = WalletCoreErrorCode.getEnumByCode(code).getMsg();
    }
}
