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

package org.omnione.did.sdk.core.api;

import android.content.Context;

import org.omnione.did.sdk.core.common.KeystoreManager;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.keymanager.datamodel.DetailKeyInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyAccessMethod;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyGenRequest;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyGenWalletMethodType;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyGenerationInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.SecureKeyGenRequest;
import org.omnione.did.sdk.core.keymanager.datamodel.WalletKeyGenRequest;
import org.omnione.did.sdk.core.keymanager.supportalgorithm.Secp256K1Manager;
import org.omnione.did.sdk.core.keymanager.supportalgorithm.Secp256R1Manager;
import org.omnione.did.sdk.core.keymanager.supportalgorithm.SignableInterface;
import org.omnione.did.sdk.core.storagemanager.datamodel.FileExtension;
import org.omnione.did.sdk.core.storagemanager.datamodel.UsableInnerWalletItem;
import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.datamodel.common.enums.AuthType;
import org.omnione.did.sdk.datamodel.common.enums.VerifyAuthType;
import org.omnione.did.sdk.utility.CryptoUtils;
import org.omnione.did.sdk.utility.DataModels.CipherInfo;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.utility.MultibaseUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

class KeyManager<E extends BaseObject>{
    Context context;
    private StorageManager<KeyInfo, DetailKeyInfo> storageManager;
    private static String SIGNATURE_MANAGER_ALIAS_PREFIX = "opendid_wallet_signature_";
    public KeyManager(){}

    public KeyManager(String fileName, Context context) throws WalletCoreException {
        this.context = context;
        storageManager = new StorageManager<>(fileName, FileExtension.FILE_EXTENSION.KEY, true, context, DetailKeyInfo.class, KeyInfo.class);
    }

    /**
     * Authenticates a user's PIN by decrypting a stored private key and verifying it against the corresponding public key.
     * This method orchestrates the entire process: fetching the key information from storage using the provided ID,
     * deriving a symmetric key from the user's PIN and a stored salt, decrypting the private key,
     * and finally checking if the decrypted private key matches the stored public key.
     * It also securely clears sensitive byte arrays from memory after use.
     *
     * @param id The identifier for the key to be authenticated. It is used to retrieve key metadata
     *           and the encrypted private key from secure storage. Cannot be an empty string.
     * @param pin The user's PIN as a byte array, which is required to derive the decryption key.
     *            This parameter must not be null for PIN-based authentication.
     * @throws WalletCoreException If any part of the authentication process fails. This includes:
     *                             <ul>
     *                               <li>If the provided {@code id} or {@code pin} is invalid (e.g., empty or null).</li>
     *                               <li>If the key information cannot be found in storage for the given {@code id}.</li>
     *                               <li>If decoding of the private key, public key, or salt from Multibase format fails.</li>
     *                               <li>If the key's access method is not supported (not {@code WALLET_PIN}).</li>
     *                               <li>If the decrypted private key does not match the public key, indicating a PIN mismatch.</li>
     *                             </ul>
     * @throws UtilityException If a utility-related error occurs, such as during cryptographic operations
     *                          or Multibase decoding, although many are wrapped in {@code WalletCoreException}.
     */
    public void authenticatePin(String id, byte[] pin) throws WalletCoreException, UtilityException {

        if(id.length() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "id");
        }

        List<String> identifiers = new ArrayList<>();
        identifiers.add(id);
        List<UsableInnerWalletItem<KeyInfo, DetailKeyInfo>> walletItems = storageManager.getItems(identifiers);
        KeyInfo signkeyInfo = walletItems.get(0).getMeta();
        DetailKeyInfo signDetailKeyInfo = walletItems.get(0).getItem();

        byte[] publicKey = MultibaseUtils.decode(signkeyInfo.getPublicKey());

        switch (signkeyInfo.getAccessMethod()) {
            case WALLET_PIN:
                if (pin == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "pin");
                }
                byte[] multibaseDecoded = MultibaseUtils.decode(signDetailKeyInfo.getPrivateKey());
                if (multibaseDecoded == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE, "Data(R)");

                }
                byte[] salt = MultibaseUtils.decode(signDetailKeyInfo.getSalt());
                if (salt == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE, "Data(A)");

                }

                int iteration = 2048;
                byte[] symmetricKey = CryptoUtils.pbkdf2(pin, salt, iteration, 48);
                byte[] key = Arrays.copyOfRange(symmetricKey, 0, 32);
                byte[] iv = Arrays.copyOfRange(symmetricKey, 32, symmetricKey.length);
                byte[] PINDecoded = CryptoUtils.decrypt(
                        multibaseDecoded,
                        new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                        key,
                        iv
                );

                Secp256R1Manager keyAlgorithm = new Secp256R1Manager();
                keyAlgorithm.checkKeyPairMatch(PINDecoded, publicKey);
                Arrays.fill(pin, (byte) 0x00);
                Arrays.fill(multibaseDecoded, (byte) 0x00);
                Arrays.fill(salt, (byte) 0x00);
                Arrays.fill(symmetricKey, (byte) 0x00);
                Arrays.fill(PINDecoded, (byte) 0x00);
                break;
            default:
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_UNSUPPORTED_ALGORITHM);

        }
    }
    /**
     * isKeySaved: Checks if a key with the given ID is saved.
     *
     * This method verifies if a key with the specified ID is stored. It first checks if the ID is valid,
     * then confirms if any key is saved, and finally checks if the key with the provided ID exists in the storage.
     *
     * @param id The identifier of the key to be checked.
     * @return Returns true if the key with the given ID is saved, false otherwise.
     * @throws Exception Throws an exception if the ID parameter is null or if an error occurs during the check.
     */
    public boolean isKeySaved(String id) throws WalletCoreException, UtilityException {
        if(id.length() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "id");
        }
        if(!isAnyKeySaved()){
            return false;
        }
        if(storageManager.getMetas(List.of(id)).size() == 0){
            return false;
        }
        return true;
    }

    /**
     * Generate a key based on the provided key generation request.
     *
     * This method generates a key based on the type of KeyGenRequest provided.
     * It first validates the ID from the request and checks if a key with the same ID already exists.
     * Depending on the type of KeyGenRequest (WalletKeyGenRequest or SecureKeyGenRequest), it delegates
     * the key generation process to the appropriate method.
     *
     * @param keyGenRequest The request object containing key generation details.
     * @throws Exception If the keyGenRequest ID is null, if a key with the same ID already exists,
     *                   or if the keyGenRequest does not conform to a recognized key generation request type.
     */
    public void generateKey(KeyGenRequest keyGenRequest) throws WalletCoreException, UtilityException {
        if(keyGenRequest.getId() == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "keyGenRequest.getId()");
        }
        if (storageManager.isSaved()) {
            List<KeyInfo> keyInfos = storageManager.getAllMetas();
            for(KeyInfo keyInfo : keyInfos){
                if(keyGenRequest.getId().equals(keyInfo.getId()))
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_EXIST_KEY_ID);
            }
        }
        if (keyGenRequest instanceof WalletKeyGenRequest) {
           generateKeyForWallet((WalletKeyGenRequest) keyGenRequest);
        } else if (keyGenRequest instanceof SecureKeyGenRequest) {
            generateKeyForSecure((SecureKeyGenRequest) keyGenRequest);
        } else {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_NOT_CONFORM_TO_KEY_GEN_REQUEST);
        }

    }

    /**
     * Generates a key for the wallet.
     *
     * This method generates a key for the wallet using the provided WalletKeyGenRequest object.
     *
     * @param keyGenRequest the request object containing parameters for key generation
     * @throws Exception if an error occurs during key generation or encryption
     */
    private void generateKeyForWallet(WalletKeyGenRequest keyGenRequest) throws WalletCoreException, UtilityException {
        KeyInfo keyInfo;
        DetailKeyInfo detailKeyInfo;
        KeyGenerationInfo genKeyInfo = new KeyGenerationInfo();
        if(keyGenRequest.getAlgorithmType().getValue().equals(AlgorithmType.ALGORITHM_TYPE.SECP256R1.getValue())){
            Secp256R1Manager keyAlgorithm = getKeyAlgorithm(keyGenRequest.getAlgorithmType());
            genKeyInfo = keyAlgorithm.generateKey();
        }

        String encodedPubKey;
        String encodedPriKey;
        if(genKeyInfo.getPrivateKey() == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "genKeyInfo");
        byte[] priKey = MultibaseUtils.decode(genKeyInfo.getPrivateKey());
        if(keyGenRequest.getWalletMethodType().getWalletMethodType() == KeyGenWalletMethodType.WALLET_METHOD_TYPE.PIN){
            if(keyGenRequest.getWalletMethodType().getPin().length() == 0){
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "pin");
            }
            byte[] salt = CryptoUtils.generateNonce(32);
            int iteration = 2048;
            byte[] symmetricKey = CryptoUtils.pbkdf2(MultibaseUtils.decode(keyGenRequest.getWalletMethodType().getPin()), salt, iteration, 48);
            byte[] key = Arrays.copyOfRange(symmetricKey, 0, 32);
            byte[] iv = Arrays.copyOfRange(symmetricKey, 32, symmetricKey.length);
            byte[] ePriKeyByte = CryptoUtils.encrypt(
                    priKey,
                    new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                    key,
                    iv
            );

            encodedPubKey = genKeyInfo.getPublicKey();
            encodedPriKey = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, ePriKeyByte);

            keyInfo = new KeyInfo(
                    keyGenRequest.getId(),
                    AuthType.AUTH_TYPE.PIN,
                    keyGenRequest.getAlgorithmType(),
                    encodedPubKey,
                    KeyAccessMethod.KEY_ACCESS_METHOD.WALLET_PIN
            );
            detailKeyInfo = new DetailKeyInfo(
                    keyGenRequest.getId(),
                    encodedPriKey,
                    MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, salt)
            );

            Arrays.fill(symmetricKey, (byte) 0x00);
            Arrays.fill(priKey, (byte) 0x00);
            Arrays.fill(ePriKeyByte, (byte) 0x00);
            genKeyInfo.setPrivateKey(null);

            storageManager.addItem(getUsableInnerWalletItem(keyInfo, detailKeyInfo), false);
        } else {
            encodedPubKey = genKeyInfo.getPublicKey();
            encodedPriKey = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, priKey);

            keyInfo = new KeyInfo(
                    keyGenRequest.getId(),
                    AuthType.AUTH_TYPE.FREE,
                    keyGenRequest.getAlgorithmType(),
                    encodedPubKey,
                    KeyAccessMethod.KEY_ACCESS_METHOD.WALLET_NONE
            );
            detailKeyInfo = new DetailKeyInfo(
                    keyGenRequest.getId(),
                    encodedPriKey
            );

            Arrays.fill(priKey, (byte) 0x00);
            genKeyInfo.setPrivateKey(null);

            storageManager.addItem(getUsableInnerWalletItem(keyInfo, detailKeyInfo), true);
        }

    }

    /**
     * Generates a secure key for the keystore.
     *
     * This method generates a key for the keystore using the provided SecureKeyGenRequest object. It checks if a key
     * with the given ID already exists and throws an exception if it does. It supports different access methods, including
     * biometric access via the keystore.
     *
     * @param keyGenRequest the request object containing parameters for secure key generation
     * @throws Exception if an error occurs during key generation or if a key with the given ID already exists
     */
    private void generateKeyForSecure(SecureKeyGenRequest keyGenRequest) throws WalletCoreException, UtilityException {
        KeyInfo keyInfo;
        DetailKeyInfo detailKeyInfo;
        if(KeystoreManager.isKeySaved(SIGNATURE_MANAGER_ALIAS_PREFIX, keyGenRequest.getId())){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "alias");
        }

        String encodedPubKey = KeystoreManager.generateKey(context, SIGNATURE_MANAGER_ALIAS_PREFIX, keyGenRequest.getId()).getPublicKey();

        keyInfo = new KeyInfo(
                keyGenRequest.getId(),
                AuthType.AUTH_TYPE.BIO,
                keyGenRequest.getAlgorithmType(),
                encodedPubKey,
                KeyAccessMethod.KEY_ACCESS_METHOD.KEYSTORE_BIOMETRY
        );
        detailKeyInfo = new DetailKeyInfo(
                keyGenRequest.getId()
        );

        storageManager.addItem(getUsableInnerWalletItem(keyInfo, detailKeyInfo), false);
    }

    /**
     * Changes the PIN for a given ID.
     *
     * This method verifies the user's current PIN and then replaces it with a new one.
     * It ensures that the new PIN is different from the old PIN and that the keys are decrypted correctly.
     *
     * @param id The identifier of the key to be changed.
     * @param oldPin The current PIN.
     * @param newPin The new PIN.
     * @throws Exception Throws an exception if parameter validation fails or if an error occurs during encryption/decryption.
     */
    public void changePin(String id, byte[] oldPin, byte[] newPin) throws WalletCoreException, UtilityException {
        if (id.isEmpty()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "id");
        }
        List<String> identifiers = new ArrayList<>();
        identifiers.add(id);
        List<UsableInnerWalletItem<KeyInfo, DetailKeyInfo>> walletItems = storageManager.getItems(identifiers);
        if (oldPin.length < 1) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "oldPin");
        }
        if (newPin.length < 1) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "newPin");
        }
        if (Arrays.equals(oldPin, newPin)) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_NEW_PIN_EQUALS_OLD_PIN);
        }
        KeyInfo oldKeyInfo = walletItems.get(0).getMeta();
        DetailKeyInfo oldDetailKeyInfo = walletItems.get(0).getItem();

        if (oldKeyInfo.getAuthType() != AuthType.AUTH_TYPE.PIN) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_NOT_PIN_AUTH_TYPE);
        }

        byte[] ePrivateKey = MultibaseUtils.decode(oldDetailKeyInfo.getPrivateKey());
        if (ePrivateKey == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE, "Data(R)");
        }
        byte[] salt = MultibaseUtils.decode(oldDetailKeyInfo.getSalt());
        if (salt == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE, "Data(A)");
        }

        byte[] symmetricKey = CryptoUtils.pbkdf2(oldPin, salt, 2048, 48);
        byte[] key = Arrays.copyOfRange(symmetricKey, 0, 32);
        byte[] iv = Arrays.copyOfRange(symmetricKey, 32, symmetricKey.length);
        byte[] priKey = CryptoUtils.decrypt(
                ePrivateKey,
                new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                key,
                iv
        );
        byte[] pubKey = MultibaseUtils.decode(oldKeyInfo.getPublicKey());
        if (pubKey == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE, "Data(U)");
        }

        if(oldKeyInfo.getAlgorithm().getValue().equals(AlgorithmType.ALGORITHM_TYPE.SECP256R1.getValue())){
            Secp256R1Manager keyAlgorithm = getKeyAlgorithm(oldKeyInfo.getAlgorithm());
            keyAlgorithm.checkKeyPairMatch(priKey, pubKey);
        }

        byte[] newSalt = CryptoUtils.generateNonce(32);
        int newIteration = 2048;
        byte[] newSymmetricKey = CryptoUtils.pbkdf2(newPin, newSalt, newIteration, 48);
        byte[] newKey = Arrays.copyOfRange(newSymmetricKey, 0, 32);
        byte[] newIv = Arrays.copyOfRange(newSymmetricKey, 32, newSymmetricKey.length);
        ePrivateKey = CryptoUtils.encrypt(
                priKey,
                new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                newKey,
                newIv
        );

        String strEncodedPriKey = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, ePrivateKey);
        DetailKeyInfo newKeyPairInfo = new DetailKeyInfo(id, strEncodedPriKey, MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, newSalt));

        UsableInnerWalletItem<KeyInfo, DetailKeyInfo> newWalletItem = new UsableInnerWalletItem<>();
        newWalletItem.setItem(newKeyPairInfo);
        newWalletItem.setMeta(oldKeyInfo);
        storageManager.updateItem(newWalletItem);

        Arrays.fill(symmetricKey, (byte) 0x00);
        Arrays.fill(priKey, (byte) 0x00);
        Arrays.fill(newSymmetricKey, (byte) 0x00);
        Arrays.fill(ePrivateKey, (byte) 0x00);
    }


    /**
     * Returns key information for the given list of IDs.
     *
     * This method retrieves key information based on the provided list of IDs.
     * If the list of IDs is empty or contains duplicates, it throws an exception.
     *
     * @param ids List of IDs for which to retrieve key information.
     * @return A list of KeyInfo objects corresponding to the provided IDs.
     * @throws Exception If the list of IDs is empty or contains duplicates.
     */
    public List<KeyInfo> getKeyInfos(List<String> ids) throws WalletCoreException, UtilityException {
        if (ids.size() < 1) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "ids");
        }
        if(ids.size() != new HashSet<String>(ids).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_DUPLICATED_PARAMETER, "ids");
        }
        return storageManager.getMetas(ids);
    }

    /**
     * Retrieves key information based on the specified authentication type.
     *
     * This method fetches all key information and filters it based on the provided
     * authentication type. If the specified authentication type is 0, it returns all keys.
     * If no keys match the specified authentication type, or if the key type is invalid,
     * it throws an exception.
     *
     * @param keyType The type of authentication used to filter keys.
     * @return A list of KeyInfo objects that match the specified authentication type.
     * @throws Exception If no keys match the specified authentication type or if the key type is invalid.
     */
    public List<KeyInfo> getKeyInfos(VerifyAuthType.VERIFY_AUTH_TYPE keyType) throws WalletCoreException, UtilityException {
        List<KeyInfo> metas = storageManager.getAllMetas();
        if(keyType.getValue() == 0){
            return metas;
        }
        int checkKeyType = keyType.getValue();
        List<KeyInfo> keyInfos = new ArrayList<>();
        for(KeyInfo keyInfo : metas){
            if((keyInfo.getAuthType().getValue() & checkKeyType) != 0){
                checkKeyType -= keyInfo.getAuthType().getValue();
                keyInfos.add(keyInfo);
            }
        }
        if(checkKeyType == keyType.getValue()){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FOUND_NO_KEY_BY_KEY_TYPE);
        }
        if(checkKeyType > VerifyAuthType.VERIFY_AUTH_TYPE.PIN_AND_BIO.getValue()){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INSUFFICIENT_RESULT_BY_KEY_TYPE);
        }
        return keyInfos;
    }

    /**
     * Deletes the keys corresponding to the given list of IDs.
     *
     * This method deletes the keys corresponding to the provided list of IDs.
     * It throws an exception if the list is empty or contains duplicate IDs.
     * Additionally, it individually deletes biometric keys stored in the keystore.
     *
     * @param ids List of key IDs to be deleted.
     * @throws Exception If the list of IDs is empty, contains duplicate IDs, or an error occurs during key deletion.
     */
    public void deleteKeys(List<String> ids) throws WalletCoreException, UtilityException {
        if (ids.size() < 1) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "ids");
        }
        if(ids.size() != new HashSet<String>(ids).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_DUPLICATED_PARAMETER, "ids");
        }
        List<KeyInfo> metas = storageManager.getMetas(ids);
        for(KeyInfo meta : metas){
            if(meta.getAccessMethod().getValue() == KeyAccessMethod.KEY_ACCESS_METHOD.KEYSTORE_BIOMETRY.getValue())
                KeystoreManager.deleteKey(SIGNATURE_MANAGER_ALIAS_PREFIX, meta.getId());
        }
        storageManager.removeItems(ids);
    }

    /**
     * Deletes all keys stored in the system.
     *
     * This method removes all key items from the storage manager and, if any keys are
     * saved in the keystore under the specified alias prefix, deletes them as well.
     *
     * @throws Exception If an error occurs during the deletion of all keys from storage or keystore.
     */
    public void deleteAllKeys(boolean isKeystore) throws WalletCoreException {
        storageManager.removeAllItems();
        if(isKeystore && KeystoreManager.isKeySaved(SIGNATURE_MANAGER_ALIAS_PREFIX, null)){
            KeystoreManager.deleteKey(SIGNATURE_MANAGER_ALIAS_PREFIX, null);
        }
    }

    /**
     * Signs the given digest using the specified key identified by its ID and PIN.
     *
     * This method retrieves the key associated with the given ID and signs the provided digest.
     * The key may be accessed either directly, via PIN, or through a keystore. Depending on the
     * access method, it may use a different signing approach.
     *
     * @param id The identifier for the key to use for signing.
     * @param pin The PIN for unlocking the key if required (can be null if not using PIN access).
     * @param digest The digest to sign with the key.
     * @return The generated signature.
     * @throws Exception If any error occurs during the process, such as invalid parameters,
     *         failure to decode data, or unsupported algorithms.
     */
    public byte[] sign(String id, byte[] pin, byte[] digest) throws WalletCoreException, UtilityException {
        if(id.length() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "id");
        }
        if (digest == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "digest");
        }
        List<String> identifiers = new ArrayList<>();
        identifiers.add(id);
        List<UsableInnerWalletItem<KeyInfo, DetailKeyInfo>> walletItems = storageManager.getItems(identifiers);
        KeyInfo signkeyInfo = walletItems.get(0).getMeta();
        DetailKeyInfo signDetailKeyInfo = walletItems.get(0).getItem();

        byte[] signature = null;
        String ePriKey;
        byte[] decPrivateKey;
        byte[] privateKey;
        byte[] publicKey = MultibaseUtils.decode(signkeyInfo.getPublicKey());

        switch (signkeyInfo.getAccessMethod()) {
            case WALLET_NONE:
                decPrivateKey = MultibaseUtils.decode(signDetailKeyInfo.getPrivateKey());
                if (decPrivateKey == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE, "Data(R)");
                }

                if(signkeyInfo.getAlgorithm().getValue().equals("Secp256r1")){
                    Secp256R1Manager keyGen = new Secp256R1Manager();
                    signature = keyGen.sign(decPrivateKey, digest);
                }
                Arrays.fill(decPrivateKey, (byte) 0x00);
                break;
            case WALLET_PIN:
                if (pin == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "pin");
                }
                byte[] multibaseDecoded = MultibaseUtils.decode(signDetailKeyInfo.getPrivateKey());
                if (multibaseDecoded == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE, "Data(R)");

                }
                byte[] salt = MultibaseUtils.decode(signDetailKeyInfo.getSalt());
                if (salt == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_FAILED_TO_DECODE, "Data(A)");

                }

                int iteration = 2048;
                byte[] symmetricKey = CryptoUtils.pbkdf2(pin, salt, iteration, 48);
                byte[] key = Arrays.copyOfRange(symmetricKey, 0, 32);
                byte[] iv = Arrays.copyOfRange(symmetricKey, 32, symmetricKey.length);
                byte[] PINDecoded = CryptoUtils.decrypt(
                        multibaseDecoded,
                        new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5),
                        key,
                        iv
                );
                Secp256R1Manager keyAlgorithm = new Secp256R1Manager();
                keyAlgorithm.checkKeyPairMatch(PINDecoded, publicKey);

                if(signkeyInfo.getAlgorithm().getValue().equals("Secp256r1")){
                    signature = keyAlgorithm.sign(PINDecoded, digest);
                }
                Arrays.fill(pin, (byte) 0x00);
                Arrays.fill(multibaseDecoded, (byte) 0x00);
                Arrays.fill(salt, (byte) 0x00);
                Arrays.fill(symmetricKey, (byte) 0x00);
                Arrays.fill(PINDecoded, (byte) 0x00);
                break;
            case KEYSTORE_NONE:
                signature = KeystoreManager.sign(id, digest);
                break;

            case KEYSTORE_BIOMETRY:
                signature = KeystoreManager.sign(id, digest);
                break;
            default:
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_UNSUPPORTED_ALGORITHM);
        }
        return signature;
    }

    /**
     * Verifies the signature for the given digest using the specified public key and algorithm.
     *
     * This method checks the validity of a signature by using the given public key, digest, and algorithm type.
     * It validates the input parameters, selects the appropriate verification method based on the algorithm type,
     * and returns whether the signature is valid or not.
     *
     * @param algorithmType The algorithm type used for verification (e.g., SECP256R1).
     * @param publicKey The public key used to verify the signature. Expected length is 33 bytes.
     * @param digest The digest of the data that was signed. Should have a length of at least 1 byte.
     * @param signature The signature to be verified. Expected length is 65 bytes.
     * @return True if the signature is valid, false otherwise.
     * @throws Exception Throws an exception if any parameter is invalid or if the algorithm type is unsupported.
     */
    public boolean verify(AlgorithmType.ALGORITHM_TYPE algorithmType, byte[] publicKey, byte[] digest, byte[] signature) throws WalletCoreException {
        if (publicKey.length != 33){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "publicKey");
        }
        if (digest.length < 1) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "digest");
        }
        if (signature.length != 65) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_INVALID_PARAMETER, "signature");
        }

        boolean result = false;
        switch (algorithmType) {
            case SECP256R1:
                Secp256R1Manager secp256R1Manager = new Secp256R1Manager();
                result = secp256R1Manager.verify(publicKey, digest, signature);
                break;
            default:
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_UNSUPPORTED_ALGORITHM);
        }
        return result;
    }

    /**
     * Checks if there are any keys saved in the storage.
     *
     * This method uses the `storageManager` to determine if there are any keys currently stored in the storage.
     * It returns true if at least one key is saved in the storage; otherwise, it returns false.
     *
     * @return true if there are any keys saved in the storage; false otherwise.
     */
    public boolean isAnyKeySaved() {
        return storageManager.isSaved();
    }

    private UsableInnerWalletItem<KeyInfo, DetailKeyInfo> getUsableInnerWalletItem(KeyInfo keyInfo, DetailKeyInfo detailKeyInfo){
        UsableInnerWalletItem<KeyInfo, DetailKeyInfo> walletItem = new UsableInnerWalletItem<>();
        walletItem.setItem(detailKeyInfo);
        walletItem.setMeta(keyInfo);
        return walletItem;
    }

    private <T extends SignableInterface> T getKeyAlgorithm(AlgorithmType.ALGORITHM_TYPE algorithmType) throws WalletCoreException{
        switch (algorithmType){
            case RSA:
                throw new UnsupportedOperationException("RSA is not implemented yet");
            case SECP256K1:
                Secp256K1Manager secp256K1Manager = new Secp256K1Manager();
                return (T) secp256K1Manager;
            case SECP256R1:
                Secp256R1Manager secp256R1Manager = new Secp256R1Manager();
                return (T) secp256R1Manager;
            default:
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEY_MANAGER_UNSUPPORTED_ALGORITHM, algorithmType.getValue());
        }
    }
}
