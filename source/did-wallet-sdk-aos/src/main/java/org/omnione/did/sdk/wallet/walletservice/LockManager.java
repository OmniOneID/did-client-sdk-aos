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

package org.omnione.did.sdk.wallet.walletservice;

import android.content.Context;

import org.omnione.did.sdk.utility.CryptoUtils;
import org.omnione.did.sdk.utility.DataModels.CipherInfo;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Encodings.Base16;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.core.api.WalletApi;
import org.omnione.did.sdk.wallet.walletservice.db.DBManager;
import org.omnione.did.sdk.wallet.walletservice.db.Preference;
import org.omnione.did.sdk.wallet.walletservice.db.User;
import org.omnione.did.sdk.wallet.walletservice.db.UserDao;
import org.omnione.did.sdk.core.common.SecureEncryptor;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.util.Arrays;

public class LockManager {
    //set lock, get lock, status
    private Context context;
    WalletLogger walletLogger;
    public LockManager(Context context){
        this.context = context;
        walletLogger = WalletLogger.getInstance();
    }

    public boolean registerLock(String passCode, boolean isLock) throws UtilityException, WalletCoreException {
        if(isLock){
            byte[] pwd = passCode.getBytes();
            byte[] walletId = DigestUtils.getDigest(Preference.loadWalletId(context).getBytes(), DigestEnum.DIGEST_ENUM.SHA_384);

            byte[] cek = CryptoUtils.generateNonce(32);
            int dk_keySize = 32;
            int iterator = 2048;
            byte[] salt = Arrays.copyOfRange(walletId, 0, 32);

            byte[] kek = CryptoUtils.pbkdf2(pwd, salt, iterator, dk_keySize);

            byte[] key = Arrays.copyOfRange(kek, 0, 32);
            byte[] iv = Arrays.copyOfRange(walletId, 32, walletId.length);
            byte[] encCek = CryptoUtils.encrypt(
                    cek,
                    new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                    key,
                    iv);

            byte[] finalEncCek = SecureEncryptor.encrypt(encCek, context);
            walletLogger.d("cek : " + Base16.toHex(cek) + " / " + cek.length);
            updateFinalEncCek(Base16.toHex(finalEncCek));

        } else {
            updateFinalEncCek("");
        }
        WalletApi.isLock = false;

        return true;
    }
    public void authenticateLock(String passCode) throws WalletCoreException, UtilityException {
        byte[] pwd = passCode.getBytes();
        byte[] walletId = DigestUtils.getDigest(Preference.loadWalletId(context).getBytes(), DigestEnum.DIGEST_ENUM.SHA_384);
        byte[] finalEncCek = Base16.toBytes(getFinalEncCek());
        byte[] encCek = SecureEncryptor.decrypt(finalEncCek);

        int dk_keySize = 32;
        int iterator = 2048;
        byte[] salt = Arrays.copyOfRange(walletId, 0, 32);

        byte[] kek = CryptoUtils.pbkdf2(pwd, salt, iterator, dk_keySize);
        byte[] key = Arrays.copyOfRange(kek, 0, 32);
        byte[] iv = Arrays.copyOfRange(walletId, 32, walletId.length);
        byte[] cek = CryptoUtils.decrypt(
                encCek,
                new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                key,
                iv);
        walletLogger.d("dec cek : " + Base16.toHex(cek) + " / " + cek.length);

        // unlock status
        WalletApi.isLock = false;
    }
    public boolean isLock(){
        boolean status = false;
        if(getFinalEncCek().length() == 0){
            walletLogger.d("wallet type is unlock");
            WalletApi.isLock = false;
            status = false;
        } else {
            walletLogger.d("wallet type is lock");
            WalletApi.isLock = true;
            status = true;
        }
        return status;
    }

    private void updateFinalEncCek(String finalEncCek){
        DBManager walletDB = DBManager.getDatabases(context);
        UserDao userDao = walletDB.userDao();
        User user = userDao.getAll().get(0);
        user.fek = finalEncCek;
        userDao.update(user);
    }

    private String getFinalEncCek(){
        DBManager walletDB = DBManager.getDatabases(context);
        UserDao userDao = walletDB.userDao();
        String fek = "";
        if(userDao.getAll().size() != 0) {
            if(userDao.getAll().get(0).fek != null)
                fek = userDao.getAll().get(0).fek;
        }
        return fek;
    }

    public void changeLock(String oldPassCode, String newPassCode) throws UtilityException, WalletCoreException, WalletException {
        // lock type setting
        if(getFinalEncCek().length() != 0){
            if(oldPassCode.equals(newPassCode))
                throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_NEW_PIN_EQUALS_OLD_PIN);
            byte[] pwd = oldPassCode.getBytes();
            byte[] walletId = DigestUtils.getDigest(Preference.loadWalletId(context).getBytes(), DigestEnum.DIGEST_ENUM.SHA_384);
            byte[] finalEncCek = Base16.toBytes(getFinalEncCek());
            byte[] encCek = SecureEncryptor.decrypt(finalEncCek);

            int dk_keySize = 32;
            int iterator = 2048;
            byte[] salt = Arrays.copyOfRange(walletId, 0, 32);

            byte[] kek = CryptoUtils.pbkdf2(pwd, salt, iterator, dk_keySize);
            byte[] key = Arrays.copyOfRange(kek, 0, 32);
            byte[] iv = Arrays.copyOfRange(walletId, 32, walletId.length);
            byte[] cek = CryptoUtils.decrypt(
                    encCek,
                    new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                    key,
                    iv);
            byte[] newPwd = newPassCode.getBytes();
            byte[] newKek = CryptoUtils.pbkdf2(newPwd, salt, iterator, dk_keySize);
            byte[] newKey = Arrays.copyOfRange(newKek, 0, 32);
            byte[] newEncCek = CryptoUtils.encrypt(
                    cek,
                    new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                    newKey,
                    iv);

            byte[] newFinalEncCek = SecureEncryptor.encrypt(newEncCek, context);
            updateFinalEncCek(Base16.toHex(newFinalEncCek));

        } else {
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_NOT_LOCK_TYPE);
        }
        WalletApi.isLock = false;
    }

}
