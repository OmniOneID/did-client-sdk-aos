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

package org.omnione.did.sdk.wallet.walletservice.exception;

public class WalletException extends Exception {
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

    public WalletException(WalletErrorCode WalletErrorCode){
        super("[" + WalletErrorCode.getWalletCoreCode() + WalletErrorCode.getFeature() + String.format("%03d", WalletErrorCode.getCode()) +"] " + WalletErrorCode.getMsg());
        this.errorCode = WalletErrorCode.getCode();
        this.errMsg = WalletErrorCode.getMsg();
    }
    public WalletException(WalletErrorCode WalletErrorCode, String paramName){
        super("["+ WalletErrorCode.getWalletCoreCode() + WalletErrorCode.getFeature() + String.format("%03d", WalletErrorCode.getCode()) +"] " + WalletErrorCode.getMsg() + paramName);
        this.errorCode = WalletErrorCode.getCode();
        this.errMsg = WalletErrorCode.getMsg() + paramName;
    }
    public WalletException(WalletErrorCode WalletErrorCode, Throwable throwable){
        super("[" + WalletErrorCode.getWalletCoreCode() + WalletErrorCode.getFeature() + String.format("%03d", WalletErrorCode.getCode()) +"] , [Message] " + throwable.getMessage());
        this.errorCode = WalletErrorCode.getCode();
        this.errMsg = throwable.getMessage();
    }
    public WalletException(int code){
        super("[ErrorCode " + WalletErrorCode.getEnumByCode(code).getCode() + "] , [Message] " + WalletErrorCode.getEnumByCode(code).getMsg());
        this.errorCode = WalletErrorCode.getEnumByCode(code).getCode();
        this.errorReason = WalletErrorCode.getEnumByCode(code).getMsg();
    }
    public WalletException(int code, Throwable throwable){
        super("[ErrorCode " + WalletErrorCode.getEnumByCode(code).getCode() + "] , [Message] " + WalletErrorCode.getEnumByCode(code).getMsg(), throwable);
        this.errorCode = WalletErrorCode.getEnumByCode(code).getCode();
        this.errorReason = WalletErrorCode.getEnumByCode(code).getMsg();
    }
}
