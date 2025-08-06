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

import org.omnione.did.sdk.core.didmanager.datamodel.DIDKeyInfo;
import org.omnione.did.sdk.core.didmanager.datamodel.DIDMeta;
import org.omnione.did.sdk.core.didmanager.datamodel.DIDMethodType;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.storagemanager.datamodel.FileExtension;
import org.omnione.did.sdk.core.storagemanager.datamodel.UsableInnerWalletItem;
import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.did.DIDKeyType;
import org.omnione.did.sdk.datamodel.did.Service;
import org.omnione.did.sdk.datamodel.did.VerificationMethod;
import org.omnione.did.sdk.utility.CryptoUtils;
import org.omnione.did.sdk.utility.Encodings.Base58;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

class DIDManager<E extends BaseObject> {
    private StorageManager<DIDMeta, DIDDocument> storageManager;
    private DIDDocument didDocument;
    public DIDManager(){}
    public DIDManager(String fileName, Context context) {
        this.storageManager = new StorageManager<>(fileName, FileExtension.FILE_EXTENSION.DID, true, context, DIDDocument.class, DIDMeta.class);
    }

    /**
     * Generates a decentralized identifier (DID) based on the provided method name.
     *
     * @param methodName The method name to be used in the DID generation. It should not be empty or exceed 20 characters in length.
     * @return The generated DID string.
     * @throws Exception if the method name is invalid (empty or exceeds 20 characters).
     */
    public static String genDID(String methodName) throws WalletCoreException, UtilityException {
        if(methodName.isEmpty() || methodName.length() > 20 || !(Pattern.matches("^[a-zA-Z]*$", methodName))) {
//        if (methodName.isEmpty() || methodName.length() > 20) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_INVALID_PARAMETER, "methodName");
        }
        String da = Base58.encode(CryptoUtils.generateNonce(20));
        return "did" + ":" + methodName + ":" + da;
    }

    /**
     * Creates a DID Document with the specified parameters.
     *
     * @param did The decentralized identifier (DID) to be used in the document.
     * @param keyInfos A list of DIDKeyInfo objects containing the keys to be included in the document.
     * @param controller The controller of the DID document.
     * @param service A list of Service objects to be included in the document.
     * @throws Exception if a DID document is already saved or if any parameter is invalid.
     */
    public void createDocument(String did, List<DIDKeyInfo> keyInfos, String controller, List<Service> service) throws WalletCoreException {
        if(isSaved()){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_DOCUMENT_ALREADY_EXISTS);
        }
        if(keyInfos == null){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_INVALID_PARAMETER, "keyInfos");
        }
        if(keyInfos.size() != new HashSet<DIDKeyInfo>(keyInfos).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_DUPLICATED_PARAMETER, "keyInfos");
        }
        didDocument = new DIDDocument(did, controller, getDate());

        ArrayList<VerificationMethod> verificationMethod = new ArrayList<VerificationMethod>();
        for(DIDKeyInfo keyInfo : keyInfos){
            VerificationMethod method = new VerificationMethod();
            method.setId(keyInfo.getKeyInfo().getId());
            method.setType(DIDKeyType.DID_KEY_TYPE.secp256r1VerificationKey2018);
            method.setController(controller);
            method.setPublicKeyMultibase(keyInfo.getKeyInfo().getPublicKey());
            method.setAuthType(keyInfo.getKeyInfo().getAuthType());
            verificationMethod.add(method);
        }
        didDocument.setVerificationMethod(verificationMethod);
        addMethodType(keyInfos);
    }

    public void updateDIDDoc(List<DIDKeyInfo> keyInfos, String controller, List<Service> service) throws WalletCoreException, UtilityException {

        DIDDocument didDoc = getDocument();
        ArrayList<VerificationMethod> verificationMethod = new ArrayList<VerificationMethod>();
        for(DIDKeyInfo keyInfo : keyInfos){
            VerificationMethod method = new VerificationMethod();
            method.setId(keyInfo.getKeyInfo().getId());
            method.setType(DIDKeyType.DID_KEY_TYPE.secp256r1VerificationKey2018);
            method.setController(controller);
            method.setPublicKeyMultibase(keyInfo.getKeyInfo().getPublicKey());
            method.setAuthType(keyInfo.getKeyInfo().getAuthType());
            verificationMethod.add(method);
        }
        didDocument.setVerificationMethod(verificationMethod);
        replaceDocument(didDoc, true);
    }

    /**
     * Gets the DID Document.
     *
     * @return The DID Document.
     * @throws Exception if the DID Document is not saved or cannot be gotten.
     */
    public DIDDocument getDocument() throws WalletCoreException, UtilityException {
        if(didDocument == null) {
            if(!isSaved())
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_NOT_FOUND_DOCUMENT);
            didDocument = storageManager.getAllItems().get(0).getItem();
        }
        return didDocument;
    }

    /**
     * Replaces the current DID Document with the provided one.
     *
     * @param didDocument The new DID Document to replace the current one.
     * @param needUpdate  Whether to update the document's timestamp.
     */
    public void replaceDocument(DIDDocument didDocument, boolean needUpdate) {
        this.didDocument = didDocument;
        if(needUpdate){
            updateVersionId();
            updateTime();
        }
    }

    /**
     * Saves the DID Document.
     *
     * @throws Exception if the DID Document is null or cannot be saved.
     */
    public void saveDocument() throws WalletCoreException, UtilityException {
        if(didDocument == null)
            return;
        UsableInnerWalletItem<DIDMeta, DIDDocument> item = new UsableInnerWalletItem<>();
        item.setItem(didDocument);
        DIDMeta didMeta = new DIDMeta();
        didMeta.setId(didDocument.getId());
        item.setMeta(didMeta);
        if(isSaved())
            storageManager.updateItem(item);
        else
            storageManager.addItem(item, true);
        didDocument = null;
    }

    /**
     * Deletes the DID Document.
     *
     * @throws Exception if the document cannot be deleted.
     */
    public void deleteDocument() throws WalletCoreException {
        storageManager.removeAllItems();
        didDocument = null;
    }

    /**
     * Adds a verification method to the DID Document.
     *
     * @param keyInfo The DIDKeyInfo object containing the key information.
     * @throws Exception if the keyInfo is null or invalid.
     */
    public void addVerificationMethod(DIDKeyInfo keyInfo) throws WalletCoreException, UtilityException {
        prepareToEditDocument();
        if (keyInfo == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_INVALID_PARAMETER, "keyInfo");
        }

        String controller = keyInfo.getController();
        String id = keyInfo.getKeyInfo().getId();
        // set verification object
        VerificationMethod verificationMethod = new VerificationMethod();
        verificationMethod.setId(keyInfo.getKeyInfo().getId());
        verificationMethod.setType(DIDKeyType.DID_KEY_TYPE.secp256r1VerificationKey2018);
        verificationMethod.setAuthType(keyInfo.getKeyInfo().getAuthType());

        // strController == null 경우, controller == DA
        String strController;
        if (controller == null || controller.equals("")) {
            strController = id;
        } else {
            strController = controller;
        }

        verificationMethod.setController(strController);
        verificationMethod.setPublicKeyMultibase(keyInfo.getKeyInfo().getPublicKey());
        verificationMethod.setAuthType(keyInfo.getKeyInfo().getAuthType());

        List<VerificationMethod> verificationMethodList = new ArrayList<>();
        if (didDocument.getVerificationMethod() != null) {
            verificationMethodList = didDocument.getVerificationMethod();
            for (VerificationMethod vm : verificationMethodList) {
                if (vm.getId().equals(id)) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_DUPLICATE_KEY_ID_EXISTS_IN_VERIFICATION_METHOD);
                }
            }

        }
        verificationMethodList.add(verificationMethod);
        didDocument.setVerificationMethod(verificationMethodList);

        addMethodType(List.of(keyInfo));

        updateTime();
    }

    /**
     * Removes a verification method from the DID Document.
     *
     * @param keyId The key ID of the verification method to be removed.
     * @throws Exception if the key ID is invalid or not found.
     */
    public void removeVerificationMethod(String keyId) throws WalletCoreException, UtilityException {
        prepareToEditDocument();
        if (keyId.length() < 1) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_INVALID_PARAMETER, "keyId");
        }
        if (!isRegisteredKey(keyId)) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_NOT_FOUND_KEY_ID_IN_VERIFICATION_METHOD);
        }

        List<VerificationMethod> verificationMethodList = didDocument.getVerificationMethod();
        int size = verificationMethodList.size();
        for (int i = 0;i<size; i++) {
            if (verificationMethodList.get(i).getId().equals(keyId)) {
                verificationMethodList.remove(i);
                size--;
                i--;
            }
        }
        updateTime();
    }

    /**
     * Adds a service to the DID Document.
     *
     * @param service The Service object to be added.
     * @throws Exception if the service is null or already exists.
     */
    public void addService(Service service) throws WalletCoreException, UtilityException {
        prepareToEditDocument();
        List<Service> newServices = new ArrayList<>();
        if(didDocument.getService() != null) {
            for (Service oldService : didDocument.getService()) {
                if (oldService.getId().equals(service.getId()))
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_DUPLICATE_SERVICE_ID_EXISTS_IN_SERVICE);
                newServices.add(oldService);
            }
        }
        newServices.add(service);
        didDocument.setService(newServices);
        updateTime();
    }
    /**
     * Removes a service from the DID Document.
     *
     * @param serviceId The ID of the service to be removed.
     * @throws Exception if the service ID is not found.
     */
    public void removeService(String serviceId) throws WalletCoreException, UtilityException {
        prepareToEditDocument();
        if (serviceId.length() < 1) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_INVALID_PARAMETER, "serviceId");
        }
        //일치하는것 없으면 에러
        int size = didDocument.getService().size();
        if(size == 0 ){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_NOT_FOUND_SERVICE_ID_IN_SERVICE);
        }
        boolean isServiceId = false;
        for(int i = 0; i < size; i++){
            if(didDocument.getService().get(i).getId().equals(serviceId)){
                didDocument.getService().remove(i);
                size--;
                i--;
                isServiceId = true;
            }
        }
        if(!isServiceId){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_NOT_FOUND_SERVICE_ID_IN_SERVICE);
        }
        if(didDocument.getService().size() == 0) {
            didDocument.setService(null);
        }
        updateTime();
    }
    /**
     * Resets the temporary DID Document changes.
     *
     * @throws Exception if the DID Document is not saved.
     */
    public void resetChanges() {
        didDocument = null;
    }

    private void prepareToEditDocument() throws WalletCoreException, UtilityException {
        if(didDocument == null){
            if(!isSaved())
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_NOT_FOUND_DOCUMENT);
            else
                didDocument = storageManager.getAllItems().get(0).getItem();
        }
    }

    private void addMethodType(List<DIDKeyInfo> keyInfos) throws WalletCoreException{
        List<String> assertionMethod = new ArrayList<>();
        List<String> authentication = new ArrayList<>();
        List<String> keyAgreement = new ArrayList<>();
        List<String> capabilityInvocation = new ArrayList<>();
        List<String> capabilityDelegation = new ArrayList<>();

        for(DIDKeyInfo keyInfo : keyInfos){
            List<DIDMethodType.DID_METHOD_TYPE> methodTypes = keyInfo.getMethodType();
            for (DIDMethodType.DID_METHOD_TYPE methodType : methodTypes) {
                switch (methodType) {
                    case assertionMethod:
                        assertionMethod.add(keyInfo.getKeyInfo().getId());
                        break;
                    case authentication:
                        authentication.add(keyInfo.getKeyInfo().getId());
                        break;
                    case keyAgreement:
                        keyAgreement.add(keyInfo.getKeyInfo().getId());
                        break;
                    case capabilityInvocation:
                        capabilityInvocation.add(keyInfo.getKeyInfo().getId());
                        break;
                    case capabilityDelegation:
                        capabilityDelegation.add(keyInfo.getKeyInfo().getId());
                        break;
                    default:
                        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_DID_MANAGER_INVALID_PARAMETER, "didMethodType");
                    }
                }
            }
            if(assertionMethod.size() > 0)
                didDocument.setAssertionMethod(assertionMethod);
            if(authentication.size() > 0)
                didDocument.setAuthentication(authentication);
            if(keyAgreement.size() > 0)
                didDocument.setKeyAgreement(keyAgreement);
            if(capabilityInvocation.size() > 0)
                didDocument.setCapabilityInvocation(capabilityInvocation);
            if(capabilityDelegation.size() > 0)
                didDocument.setCapabilityDelegation(capabilityDelegation);
        }

    private boolean isRegisteredKey(String keyId) {
        List<VerificationMethod> vmList = didDocument.getVerificationMethod();
        List<String> temp = new ArrayList<>();
        for (VerificationMethod vm : vmList) {
            temp.add(vm.getId());
        }
        return temp.contains(keyId);
    }
    private void updateTime() {
        didDocument.setUpdated(getDate());
    }

    private void updateVersionId() {
        // ++ versionId
        didDocument.setVersionId(String.valueOf((Integer.parseInt(didDocument.getVersionId()))+1));
    }

    public boolean isSaved(){
        return storageManager.isSaved();
    }

    private String getDate(){
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(today);
    }
}
