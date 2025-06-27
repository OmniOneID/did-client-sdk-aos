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
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.omnione.did.sdk.core.common.KeystoreManager;
import org.omnione.did.sdk.core.common.SecureEncryptor;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.storagemanager.datamodel.ExternalWallet;
import org.omnione.did.sdk.core.storagemanager.datamodel.FileExtension;
import org.omnione.did.sdk.core.storagemanager.datamodel.Meta;
import org.omnione.did.sdk.core.storagemanager.datamodel.StorableInnerWalletItem;
import org.omnione.did.sdk.core.storagemanager.datamodel.UsableInnerWalletItem;
import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.util.MessageUtil;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.utility.MultibaseUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

class StorageManager<M extends Meta, T extends BaseObject> {
    boolean isEncrypted;
    private ExternalWallet<T> tmp = new ExternalWallet<>();
    private String filePath;
    private String walletPath = "/opendid_omnione/";
    private Context context;
    private Class<T> targetClass;
    private Class<M> metaClass;
    private static final String SIGNATURE_MANAGER_ALIAS_PREFIX = "opendid_wallet_signature_";
    private static final String STORAGE_MANAGER_ALIAS = "signatureKey";

    public StorageManager(@NonNull String fileName, @NonNull FileExtension.FILE_EXTENSION fileExtension, boolean isEncrypted, Context context, Class<T> targetClass, Class<M> metaClass) {
        this.isEncrypted = isEncrypted;
        filePath = context.getFilesDir().getPath() + walletPath + fileName + "." + fileExtension.getValue();
        this.context = context;
        this.targetClass = targetClass;
        this.metaClass = metaClass;
    }

    public boolean isSaved() {
        File file = new File(filePath);
        return file.exists();
    }

    public void addItem(UsableInnerWalletItem<M,T> walletItem, boolean asFirst) throws WalletCoreException, UtilityException {
        try {
            if (walletItem.toJson().length() < 1) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_INVALID_PARAMETER, "walletItem");
            }
            StorableInnerWalletItem<M> storableInnerWalletItem = new StorableInnerWalletItem<>();
            List<String> itemList = new ArrayList<>();
            if (isSaved()) {
                List<UsableInnerWalletItem<M, T>> list = getAllItems();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getMeta().getId().equals(walletItem.getMeta().getId())) {
                        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_DUPLICATED_PARAMETER);
                    } else {
                        storableInnerWalletItem.setMeta(list.get(i).getMeta());
                        storableInnerWalletItem.setItem(encodeItem(list.get(i)));
                        itemList.add(storableInnerWalletItem.toJson());
                    }
                }
            }
            if (asFirst) {
                Deque<String> deque = new LinkedList<>(itemList);
                storableInnerWalletItem.setMeta(walletItem.getMeta());
                storableInnerWalletItem.setItem(encodeItem(walletItem));
                deque.addFirst(storableInnerWalletItem.toJson());
                tmp.setData(deque.toString());
            } else {
                storableInnerWalletItem.setMeta(walletItem.getMeta());
                storableInnerWalletItem.setItem(encodeItem(walletItem));
                itemList.add(storableInnerWalletItem.toJson());
                tmp.setData(itemList.toString());
            }
            writeFile();
        } catch (InvalidKeySpecException | InvalidAlgorithmParameterException | UnrecoverableEntryException | NoSuchPaddingException | CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | InvalidParameterSpecException | BadPaddingException | NoSuchProviderException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_FAIL_TO_SAVE_WALLET);
        }
    }

    public void updateItem(UsableInnerWalletItem<M,T> walletItem) throws WalletCoreException, UtilityException {
        if (walletItem.toJson().length() < 1) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_INVALID_PARAMETER, "walletItem");
        }
        List<String> identifiers = new ArrayList<>();
        List<UsableInnerWalletItem<M, T>> itemList = getAllItems();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getMeta().getId().equals(walletItem.getMeta().getId())) {
                identifiers.add(itemList.get(i).getMeta().getId());
                removeItems(identifiers);
                addItem(walletItem, false);
                return;
            }
        }
        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEM_TO_UPDATE);
    }

    public void removeItems(List<String> identifiers) throws WalletCoreException, UtilityException {
        try {
            if (identifiers.size() < 1) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_INVALID_PARAMETER, "identifiers");
            }
            List<UsableInnerWalletItem<M, T>> walletItems = getAllItems();
            List<UsableInnerWalletItem<M, T>> removeItems = walletItems;
            UsableInnerWalletItem<M, T> removeItem = new UsableInnerWalletItem<>();
            boolean isIdentifiers = false;
            for (String id : identifiers) {
                for (int i = 0; i < walletItems.size(); i++) {
                    if (walletItems.get(i).getMeta().getId().equals(id)) {
                        removeItem = walletItems.get(i);
                        removeItems.remove(removeItem);
                        isIdentifiers = true;
                    }
                }
                if (!isIdentifiers)
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_REMOVE);
            }

            StorableInnerWalletItem<M> storableInnerWalletItem = new StorableInnerWalletItem<>();
            List<String> itemList = new ArrayList<>();
            for (int i = 0; i < removeItems.size(); i++) {
                storableInnerWalletItem.setMeta(removeItems.get(i).getMeta());
                storableInnerWalletItem.setItem(encodeItem(removeItems.get(i)));
                itemList.add(storableInnerWalletItem.toJson());
            }
            tmp.setData(itemList.toString());
            writeFile();
        } catch (InvalidKeySpecException | InvalidAlgorithmParameterException | UnrecoverableEntryException | NoSuchPaddingException | CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | InvalidParameterSpecException | BadPaddingException | NoSuchProviderException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_REMOVE);
        }
    }

    public void removeAllItems() throws WalletCoreException {
        if (isSaved()) {
            File file = new File(filePath);
            boolean result = file.delete();
            if (!result) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_FAIL_TO_REMOVE_ITEMS);
            }
        }
    }

    public <M extends Meta> List<M> getMetas(List<String> identifiers) throws WalletCoreException, UtilityException {
        try {
            List<M> list = new ArrayList<>();
            if (!isSaved()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_SAVED);
            }
            readFile();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(tmp.toJson());
            JsonObject jsonObject = element.getAsJsonObject();

            if (jsonObject.has("data")) {
                verifyFile();

                JSONArray array = new JSONArray(tmp.getData());
                for (int i = 0; i < array.length(); i++) {
                    JsonElement jsonElement = JsonParser.parseString(array.get(i).toString());

                    jsonObject = jsonElement.getAsJsonObject();
                    for (String id : identifiers) {
                        if (MessageUtil.deserializeFromJsonElement(jsonObject.get("meta"), metaClass).getId().equals(id))
                            list.add((M) MessageUtil.deserializeFromJsonElement(jsonObject.get("meta"), metaClass));
                    }

                }
            } else {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
            }
            return list;
        } catch (JSONException | IOException | GeneralSecurityException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
        }
    }

    public <M extends Meta> List<M> getAllMetas() throws WalletCoreException, UtilityException {
        try {
            List<M> list = new ArrayList<>();
            if (!isSaved()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_SAVED);
            }

            readFile();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(tmp.toJson());
            JsonObject jsonObject = element.getAsJsonObject();

            if (jsonObject.has("data")) {
                verifyFile();
                JSONArray array = new JSONArray(tmp.getData());
                for (int i = 0; i < array.length(); i++) {
                    JsonElement jsonElement = JsonParser.parseString(array.get(i).toString());
                    jsonObject = jsonElement.getAsJsonObject();
                    list.add((M) MessageUtil.deserializeFromJsonElement(jsonObject.get("meta"), metaClass));
                }
            } else {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
            }
            return list;
        } catch (JSONException | IOException | GeneralSecurityException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
        }
    }

    public List<UsableInnerWalletItem<M,T>> getItems(List<String> identifiers) throws WalletCoreException, UtilityException {
        try {
            List<UsableInnerWalletItem<M, T>> list = new ArrayList<>();
            if (!isSaved()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_SAVED);
            }
            readFile();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(tmp.toJson());
            JsonObject jsonObject = element.getAsJsonObject();

            if (jsonObject.has("data")) {
                verifyFile();
                JSONArray array = new JSONArray(tmp.getData());
                for (int i = 0; i < array.length(); i++) {
                    UsableInnerWalletItem<M, T> target = new UsableInnerWalletItem<>();
                    JsonElement jsonElement = JsonParser.parseString(array.get(i).toString());

                    jsonObject = jsonElement.getAsJsonObject();
                    target.setMeta((M) MessageUtil.deserializeFromJsonElement(jsonObject.get("meta"), metaClass));
                    String encodedItem = jsonObject.get("item").getAsString();
                    byte[] decodeData = Base64.decode(encodedItem, Base64.DEFAULT);
                    String item = "";
                    if (tmp.isEncrypted()) {
                        byte[] decDataByte = SecureEncryptor.decrypt(decodeData);
                        item = new String(decDataByte);
                    } else {
                        item = new String(decodeData);
                    }
                    for (String id : identifiers) {
                        if (MessageUtil.deserializeFromJsonElement(jsonObject.get("meta"), metaClass).getId().equals(id)) {
                            target.setItem((T) MessageUtil.deserialize(item, targetClass));
                            list.add(target);
                        }
                    }
                }
                if (list.size() == 0)
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
            } else {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
            }
            return list;
        } catch (GeneralSecurityException | JSONException | IOException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
        }
    }
    public List<UsableInnerWalletItem<M,T>> getAllItems() throws WalletCoreException, UtilityException {
        try {
            List<UsableInnerWalletItem<M, T>> list = new ArrayList<>();
            if (!isSaved()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_SAVED);
            }

            readFile();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(tmp.toJson());
            JsonObject jsonObject = element.getAsJsonObject();

            if (jsonObject.has("data")) {
                verifyFile();
                JSONArray array = new JSONArray(tmp.getData());
                for (int i = 0; i < array.length(); i++) {
                    UsableInnerWalletItem<M, T> target = new UsableInnerWalletItem<>();
                    JsonElement jsonElement = JsonParser.parseString(array.get(i).toString());

                    jsonObject = jsonElement.getAsJsonObject();
                    target.setMeta((M) MessageUtil.deserializeFromJsonElement(jsonObject.get("meta"), metaClass));
                    String encodedItem = jsonObject.get("item").getAsString();
                    byte[] decodeData = Base64.decode(encodedItem, Base64.DEFAULT);
                    String item = "";
                    if (tmp.isEncrypted()) {
                        byte[] decDataByte = SecureEncryptor.decrypt(decodeData);
                        item = new String(decDataByte);
                    } else {
                        item = new String(decodeData);
                    }
                    target.setItem((T) MessageUtil.deserialize(item, targetClass));

                    list.add(target);
                }
            } else {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
            }
            return list;
        }  catch (GeneralSecurityException | JSONException | IOException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_NO_ITEMS_TO_FIND);
        }
    }

    private String encodeItem(UsableInnerWalletItem<M, T> item) throws WalletCoreException, InvalidAlgorithmParameterException, UnrecoverableEntryException, NoSuchPaddingException, IllegalBlockSizeException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, InvalidParameterSpecException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        String itemJson = item.getItem().toJson();
        if (isEncrypted) {
            byte[] encData = SecureEncryptor.encrypt(itemJson.getBytes(), context);
            return Base64.encodeToString(encData, Base64.DEFAULT);
        } else {
            return Base64.encodeToString(itemJson.getBytes(), Base64.DEFAULT);
        }
    }


    private void writeFile() throws WalletCoreException, IOException, InvalidAlgorithmParameterException, CertificateException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, UtilityException {
        File directory = new File(context.getFilesDir().getPath() + walletPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(filePath);
        FileWriter fw = null;
        if (tmp.getData() != null) {
            if (isEncrypted) {
                tmp.setEncryption(isEncrypted);
                tmp.setSignature(getSignature());
            } else {
                tmp.setSignature(getSignature());
                tmp.setEncryption(isEncrypted);
            }
        }
        try {
            fw = new FileWriter(file);
            fw.write(tmp.toJson());
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            fw.close();
        }
    }

    private void readFile() throws WalletCoreException, IOException {
        FileReader fr = null;
        BufferedReader br = null;
        StringBuffer buffer;
        try {
            File file = new File(filePath);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            buffer = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            tmp = new ExternalWallet<T>();
            tmp.fromJson(buffer.toString());
            br.close();
            fr.close();
        } catch (IOException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_FAIL_TO_READ_WALLET_FILE, e.getMessage());
        } finally {
            br.close();
            fr.close();
        }
    }
    private String getSignature() throws WalletCoreException, UtilityException {
        if(!KeystoreManager.isKeySaved(SIGNATURE_MANAGER_ALIAS_PREFIX, STORAGE_MANAGER_ALIAS)) {
            KeystoreManager.generateKey(context, SIGNATURE_MANAGER_ALIAS_PREFIX, STORAGE_MANAGER_ALIAS);
        }
        byte[] signature = KeystoreManager.sign(STORAGE_MANAGER_ALIAS, DigestUtils.getDigest(tmp.getData().getBytes(), DigestEnum.DIGEST_ENUM.SHA_256));
        return MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, signature);
    }

    private void verifyFile() throws WalletCoreException, GeneralSecurityException, IOException, UtilityException {
        byte[] signature = MultibaseUtils.decode(tmp.getSignature());
        if(signature == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_FAIL_TO_DECODE, "externalWallet.signature");
        }
        byte[] digest = DigestUtils.getDigest(tmp.getData().getBytes(), DigestEnum.DIGEST_ENUM.SHA_256);
        if(digest == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_FAIL_TO_DECODE, "externalWallet.signature");
        }
        boolean result = KeystoreManager.verify(KeystoreManager.getPublicKey(SIGNATURE_MANAGER_ALIAS_PREFIX, STORAGE_MANAGER_ALIAS), digest, signature);
        if (!result) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_STORAGE_MANAGER_MALFORMED_WALLET_SIGNATURE);
        }
    }
}
