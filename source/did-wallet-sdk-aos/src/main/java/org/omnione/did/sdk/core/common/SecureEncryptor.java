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

package org.omnione.did.sdk.core.common;

import android.content.Context;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;

public class SecureEncryptor {
    private static final String SECURE_ENCRPTOR_ALIAS_PREFIX = "opendid_wallet_encryption_";
    private static final String SECURE_ENCRPTOR_ALIAS = "secureEncryptor";

    public static byte[] encrypt(byte[] plainData, Context context) throws WalletCoreException {
       if (plainData == null || plainData.length == 0){
           throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SECURE_ENCRYPTOR_INVALID_PARAMETER, "plainData");
       }
       if (context == null){
           throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SECURE_ENCRYPTOR_INVALID_PARAMETER, "context");
       }
       byte[] cipher = KeystoreManager.encrypt(SECURE_ENCRPTOR_ALIAS_PREFIX + SECURE_ENCRPTOR_ALIAS, plainData, context);
       return cipher;
    }


    public static byte[] decrypt(byte[] cipherData) throws WalletCoreException {
        if (cipherData == null || cipherData.length == 0){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SECURE_ENCRYPTOR_INVALID_PARAMETER, "cipherData");
        }
        byte[] plain = KeystoreManager.decrypt(SECURE_ENCRPTOR_ALIAS_PREFIX + SECURE_ENCRPTOR_ALIAS, cipherData);
        return plain;
    }

}
