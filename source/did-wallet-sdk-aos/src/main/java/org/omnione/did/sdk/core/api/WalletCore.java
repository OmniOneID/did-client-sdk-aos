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

import org.omnione.did.sdk.core.bioprompthelper.BioPromptHelper;
import org.omnione.did.sdk.core.didmanager.datamodel.DIDKeyInfo;
import org.omnione.did.sdk.core.didmanager.datamodel.DIDMethodType;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.keymanager.datamodel.DetailKeyInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyGenWalletMethodType;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyInfo;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyStoreAccessMethod;
import org.omnione.did.sdk.core.keymanager.datamodel.SecureKeyGenRequest;
import org.omnione.did.sdk.core.keymanager.datamodel.StorageOption;
import org.omnione.did.sdk.core.keymanager.datamodel.WalletKeyGenRequest;
import org.omnione.did.sdk.core.vcmanager.datamodel.ClaimInfo;
import org.omnione.did.sdk.core.vcmanager.datamodel.PresentationInfo;
import org.omnione.did.sdk.core.zkp.datamodel.ZKPInfo;
import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;
import org.omnione.did.sdk.datamodel.vp.VerifiablePresentation;
import org.omnione.did.sdk.datamodel.zkp.AvailableReferent;
import org.omnione.did.sdk.datamodel.zkp.Credential;
import org.omnione.did.sdk.datamodel.zkp.CredentialOffer;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.CredentialRequestContainer;
import org.omnione.did.sdk.datamodel.zkp.CredentialRequestMeta;
import org.omnione.did.sdk.datamodel.zkp.Proof;
import org.omnione.did.sdk.datamodel.zkp.ProofParam;
import org.omnione.did.sdk.datamodel.zkp.ProofRequest;
import org.omnione.did.sdk.datamodel.zkp.ReferentInfo;
import org.omnione.did.sdk.datamodel.zkp.UserReferent;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.wallet.WalletCoreInterface;
import org.omnione.did.sdk.wallet.walletservice.config.Config;
import org.omnione.did.sdk.wallet.walletservice.config.Constants;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class WalletCore implements WalletCoreInterface {
    private Context context;
    KeyManager<DetailKeyInfo> deviceKeyManager;
    DIDManager<DIDDocument> deviceDIDManager;
    KeyManager<DetailKeyInfo> keyManager;
    DIDManager<DIDDocument> didManager;
    VCManager<VerifiableCredential> vcManager;
    BioPromptHelper bioPromptHelper;
    WalletLogger walletLogger;

    ZKPManager<ZKPInfo> zkpManager;

    public void setBioPromptListener(BioPromptHelper.BioPromptInterface bioPromptInterface){
        this.bioPromptInterface = bioPromptInterface;
    }
    public interface BioPromptInterface{
        void onSuccess(String result);
        void onFail(String result);
    }
    private BioPromptHelper.BioPromptInterface bioPromptInterface;

    public WalletCore(){}
    public WalletCore(Context context) throws WalletCoreException {
        // islock
        this.context = context;
        deviceKeyManager = new KeyManager<>(Constants.WALLET_DEVICE, context);
        deviceDIDManager = new DIDManager<>(Constants.WALLET_DEVICE, context);
        keyManager = new KeyManager<>(Constants.WALLET_HOLDER, context);
        didManager = new DIDManager<>(Constants.WALLET_HOLDER, context);
        vcManager = new VCManager<>(Constants.WALLET_HOLDER, context);
        try {
            zkpManager = new ZKPManager<>(Constants.WALLET_HOLDER, context);
        } catch (UtilityException e) {
            throw new RuntimeException(e);
        }
        bioPromptHelper = new BioPromptHelper(context);
        walletLogger = WalletLogger.getInstance();
    }

    public void authenticatePin(String id, byte[] pin) throws WalletCoreException, UtilityException {
        keyManager.authenticatePin(id, pin);
    }

    public DIDDocument updateHolderDIDDoc() throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

//        KeyGenWalletMethodType keyGenWalletMethodType = new KeyGenWalletMethodType();
//        WalletKeyGenRequest keyGenInfo = new WalletKeyGenRequest(
//                Constants.KEY_ID_KEY_AGREE,
//                AlgorithmType.ALGORITHM_TYPE.SECP256R1,
//                StorageOption.STORAGE_OPTION.WALLET,
//                keyGenWalletMethodType
//        );
//        keyManager.generateKey(keyGenInfo);

        String controller = Config.DID_CONTROLLER;
        List<KeyInfo> keyInfos = keyManager.getKeyInfos(List.of(Constants.KEY_ID_PIN, Constants.KEY_ID_BIO, Constants.KEY_ID_KEY_AGREE));
        List<DIDKeyInfo> didKeyInfos = new ArrayList<>();
        for(KeyInfo keyInfo : keyInfos){
            DIDKeyInfo didKeyInfo = new DIDKeyInfo();
            if(keyInfo.getId().equals(Constants.KEY_ID_PIN)) {
                didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.assertionMethod, DIDMethodType.DID_METHOD_TYPE.authentication), controller);
            }
            if(keyInfo.getId().equals(Constants.KEY_ID_BIO)) {
                didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.assertionMethod, DIDMethodType.DID_METHOD_TYPE.authentication), controller);
            }
            if(keyInfo.getId().equals(Constants.KEY_ID_KEY_AGREE))
                didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.keyAgreement), controller);
            didKeyInfos.add(didKeyInfo);
        }
        didManager.updateDIDDoc(didKeyInfos, controller, null);
        return didManager.getDocument();

    }

    @Override
    public DIDDocument createDeviceDIDDoc() throws WalletCoreException, UtilityException {
        if(!deviceKeyManager.isAnyKeySaved()) {
            KeyGenWalletMethodType keyGenWalletMethodType = new KeyGenWalletMethodType();
            WalletKeyGenRequest keyGenInfo = new WalletKeyGenRequest(
                    Constants.KEY_ID_ASSERT,
                    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                    StorageOption.STORAGE_OPTION.WALLET,
                    keyGenWalletMethodType
            );
            deviceKeyManager.generateKey(keyGenInfo);

            keyGenInfo = new WalletKeyGenRequest(
                    Constants.KEY_ID_AUTH,
                    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                    StorageOption.STORAGE_OPTION.WALLET,
                    keyGenWalletMethodType
            );
            deviceKeyManager.generateKey(keyGenInfo);

            keyGenInfo = new WalletKeyGenRequest(
                    Constants.KEY_ID_KEY_AGREE,
                    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                    StorageOption.STORAGE_OPTION.WALLET,
                    keyGenWalletMethodType
            );
            deviceKeyManager.generateKey(keyGenInfo);

            String did = DIDManager.genDID(Config.DID_METHOD);
            String controller = Config.DID_CONTROLLER;
            List<KeyInfo> keyInfos = deviceKeyManager.getKeyInfos(List.of(Constants.KEY_ID_ASSERT, Constants.KEY_ID_AUTH, Constants.KEY_ID_KEY_AGREE));
            List<DIDKeyInfo> didKeyInfos = new ArrayList<>();
            for(KeyInfo keyInfo : keyInfos){
                DIDKeyInfo didKeyInfo = new DIDKeyInfo();
                if(keyInfo.getId().equals(Constants.KEY_ID_ASSERT))
                    didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.assertionMethod), controller);
                if(keyInfo.getId().equals(Constants.KEY_ID_AUTH))
                    didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.authentication), controller);
                if(keyInfo.getId().equals(Constants.KEY_ID_KEY_AGREE))
                    didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.keyAgreement), controller);
                didKeyInfos.add(didKeyInfo);
            }
            deviceDIDManager.createDocument(did, didKeyInfos, controller, null);
            return deviceDIDManager.getDocument();
        } else {
            return deviceDIDManager.getDocument();
        }
    }

    @Override
    public DIDDocument createHolderDIDDoc() throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        if(!keyManager.isKeySaved(Constants.KEY_ID_KEY_AGREE)) {
            KeyGenWalletMethodType keyGenWalletMethodType = new KeyGenWalletMethodType();
            WalletKeyGenRequest keyGenInfo = new WalletKeyGenRequest(
                    Constants.KEY_ID_KEY_AGREE,
                    AlgorithmType.ALGORITHM_TYPE.SECP256R1,
                    StorageOption.STORAGE_OPTION.WALLET,
                    keyGenWalletMethodType
            );
            keyManager.generateKey(keyGenInfo);
            String did = DIDManager.genDID(Config.DID_METHOD);
            String controller = Config.DID_CONTROLLER;
            List<KeyInfo> keyInfos = keyManager.getKeyInfos(List.of(Constants.KEY_ID_PIN, Constants.KEY_ID_BIO, Constants.KEY_ID_KEY_AGREE));
            List<DIDKeyInfo> didKeyInfos = new ArrayList<>();
            for(KeyInfo keyInfo : keyInfos){
                DIDKeyInfo didKeyInfo = new DIDKeyInfo();
                if(keyInfo.getId().equals(Constants.KEY_ID_PIN)) {
                    didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.assertionMethod, DIDMethodType.DID_METHOD_TYPE.authentication), controller);
                }
                if(keyInfo.getId().equals(Constants.KEY_ID_BIO)) {
                    didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.assertionMethod, DIDMethodType.DID_METHOD_TYPE.authentication), controller);
                }
                if(keyInfo.getId().equals(Constants.KEY_ID_KEY_AGREE))
                    didKeyInfo = new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.keyAgreement), controller);
                didKeyInfos.add(didKeyInfo);
            }
            didManager.createDocument(did, didKeyInfos,controller, null);
            return didManager.getDocument();
        } else {
            return didManager.getDocument();
        }
    }
    @Override
    public void generateKeyPair(String passcode) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);
        if(passcode.isEmpty())
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL, "passcode");
        WalletKeyGenRequest keyGenRequest = new WalletKeyGenRequest();
        keyGenRequest.setId(Constants.KEY_ID_PIN);
        keyGenRequest.setAlgorithmType(AlgorithmType.ALGORITHM_TYPE.SECP256R1);
        keyGenRequest.setStorage(StorageOption.STORAGE_OPTION.WALLET);
        KeyGenWalletMethodType keyGenWalletMethodType = new KeyGenWalletMethodType(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, passcode.getBytes()));
        keyGenRequest.setWalletMethodType(keyGenWalletMethodType);
        keyManager.generateKey(keyGenRequest);
    }

    @Override
    public DIDDocument getDocument(int type) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);
        DIDDocument didDocument = new DIDDocument();
        if(type == Constants.DID_DOC_TYPE_DEVICE) // device
            didDocument =  deviceDIDManager.getDocument();
        else if(type == Constants.DID_DOC_TYPE_HOLDER) //holder
            didDocument =  didManager.getDocument();
        else
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_FAIL);

        return didDocument;
    }

    @Override
    public void saveDocument(int type) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);
        
        if(type == Constants.DID_DOC_TYPE_DEVICE){
            deviceDIDManager.saveDocument();
        } else {
            didManager.saveDocument();
        }
    }

    @Override
    public boolean isExistWallet() {
        walletLogger.d("No registered device key : " + deviceKeyManager.isAnyKeySaved());
        walletLogger.d("No registered device DID : " + deviceDIDManager.isSaved());
        return deviceKeyManager.isAnyKeySaved() && deviceDIDManager.isSaved();
    }

    public void deleteKey(List<String> keyId) throws WalletCoreException, UtilityException {
        keyManager.deleteKeys(keyId);
    }

    @Override
    public void deleteWallet(boolean deleteAll) throws WalletCoreException {

        if (didManager.isSaved()) {
            didManager.deleteDocument();
        }
        if (vcManager.isAnyCredentialsSaved()) {
            vcManager.deleteAllCredentials();
        }
        if (keyManager.isAnyKeySaved()) {
            keyManager.deleteAllKeys(deleteAll);
        }

        if (deleteAll) {
            if (deviceKeyManager.isAnyKeySaved()) {
                deviceKeyManager.deleteAllKeys(deleteAll);
            }
            if (deviceDIDManager.isSaved()) {
                deviceDIDManager.deleteDocument();
            }
        }
    }
    @Override
    public boolean isAnyCredentialsSaved() throws WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        return vcManager.isAnyCredentialsSaved();
    }
    @Override
    public void addCredentials(VerifiableCredential verifiableCredential) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        vcManager.addCredentials(verifiableCredential);
    }
    @Override
    public List<VerifiableCredential> getCredentials(List<String> identifiers) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        if(!vcManager.isAnyCredentialsSaved())
            return null;
        return vcManager.getCredentials(identifiers);
    }
    @Override
    public List<VerifiableCredential> getAllCredentials() throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        if(!vcManager.isAnyCredentialsSaved())
            return null;
        return vcManager.getAllCredentials();
    }
    @Override
    public void deleteCredentials(List<String> identifiers) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        vcManager.deleteCredentials(identifiers);
        WalletLogger.getInstance().d("delete vc success");

        for (String identifier : identifiers) {
            if (zkpManager.isZkpCredentialsSaved(identifier)) {
                zkpManager.deleteCredentials(identifiers);
                WalletLogger.getInstance().d("delete zkp credential success");
            }
        }
    }

    @Override
    public VerifiablePresentation makePresentation(List<ClaimInfo> claimInfos, PresentationInfo presentationInfo) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        return vcManager.makePresentation(claimInfos, presentationInfo);
    }
    @Override
    public void registerBioKey(Context ctx) throws WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        bioPromptHelper.setBioPromptListener(new BioPromptHelper.BioPromptInterface() {
            @Override
            public void onSuccess(String result) {
                try {
                    SecureKeyGenRequest keyGenInfo = new SecureKeyGenRequest();
                    keyGenInfo.setId(Constants.KEY_ID_BIO);
                    keyGenInfo.setAlgorithmType(AlgorithmType.ALGORITHM_TYPE.SECP256R1);
                    keyGenInfo.setStorage(StorageOption.STORAGE_OPTION.KEYSTORE);
                    keyGenInfo.setAccessMethod(KeyStoreAccessMethod.KEYSTORE_ACCESS_METHOD.BIOMETRY);
                    keyManager.generateKey(keyGenInfo);
                    bioPromptInterface.onSuccess(result);
                } catch (WalletCoreException | UtilityException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(String result) {
                bioPromptInterface.onError(result);
            }
            @Override
            public void onCancel(String result) {
                bioPromptInterface.onCancel(result);
            }
            @Override
            public void onFail(String result) {
                bioPromptInterface.onFail(result);
            }
        });
        bioPromptHelper.registerBioKey(ctx, null);
    }
    @Override
    public void authenticateBioKey(Context ctx) throws WalletCoreException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        bioPromptHelper.setBioPromptListener(new BioPromptHelper.BioPromptInterface() {
            @Override
            public void onSuccess(String result) {
                bioPromptInterface.onSuccess(result);
            }
            @Override
            public void onError(String result) {
                bioPromptInterface.onError(result);
            }
            @Override
            public void onCancel(String result) {
                bioPromptInterface.onCancel(result);
            }
            @Override
            public void onFail(String result) {
                bioPromptInterface.onFail(result);
            }
        });
        bioPromptHelper.authenticateBioKey(ctx, null);

    }
    @Override
    public byte[] sign(String id, byte[] pin, byte[] digest, int type) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        byte[] hashedData = DigestUtils.getDigest(digest, DigestEnum.DIGEST_ENUM.SHA_256);
        byte[] signValue = null;
        if(type == Constants.DID_DOC_TYPE_DEVICE)
            signValue = deviceKeyManager.sign(id, pin, hashedData);
        else if(type == Constants.DID_DOC_TYPE_HOLDER)
            signValue = keyManager.sign(id, pin, hashedData);
        return signValue;
    }

    @Override
    public boolean verify(byte[] publicKey, byte[] digest, byte[] signature) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        byte[] hashedData = DigestUtils.getDigest(digest, DigestEnum.DIGEST_ENUM.SHA_256);
        boolean result = keyManager.verify(AlgorithmType.ALGORITHM_TYPE.SECP256R1, publicKey, hashedData, signature);
        return result;
    }

    @Override
    public boolean isSavedKey(String id) throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        return keyManager.isKeySaved(id);
    }

    @Override
    public void changePin(String keyId, String oldPin, String newPin) throws WalletCoreException, UtilityException {
        keyManager.changePin(keyId, oldPin.getBytes(), newPin.getBytes());
    }

    @Override
    public boolean isAnyZkpCredentialsSaved() throws WalletCoreException, UtilityException, WalletException {
        if(WalletApi.isLock)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_LOCKED_WALLET);

        return zkpManager.isAnyCredentialsSaved();
    }

    // ZKP
    @Override
    public List<Credential> getZkpCredentials(List<String> identifiers) throws WalletCoreException, UtilityException {
        return zkpManager.getCredentials(identifiers);
    }

    @Override
    public ArrayList<Credential> getAllZkpCredentials() throws WalletCoreException, UtilityException {
        return zkpManager.getAllCredentials();
    }

    @Override
    public void deleteZkpCredentials(List<String> identifiers) throws WalletCoreException, UtilityException {
        zkpManager.deleteCredentials(identifiers);
    }

    @Override
    public void deleteAllZkpCredentials() throws WalletCoreException, UtilityException {
        zkpManager.deleteAllCredentials();
    }

    @Override
    public CredentialRequestContainer createCredentialRequest(CredentialPrimaryPublicKey credentialPublicKey, CredentialOffer credOffer) throws WalletCoreException, UtilityException, WalletException {

        return zkpManager.createCredentialRequest(this.getDocument(2).getId(), credentialPublicKey, credOffer);
    }

    @Override
    public void verifyAndStoreZkpCredential(CredentialRequestMeta credentialRequestMeta,
                                            CredentialPrimaryPublicKey credentialPrimaryPublicKey,
                                            Credential credential) throws WalletCoreException, UtilityException {
        zkpManager.verifyAndStoreCredential(credentialRequestMeta, credentialPrimaryPublicKey, credential);
    }

    @Override
    public AvailableReferent searchZkpCredentials(ProofRequest proofRequest) throws WalletCoreException, UtilityException {
        return zkpManager.searchCredentials(proofRequest);
    }

    @Override
    public ReferentInfo createZkpReferent(List<UserReferent> customReferents) throws WalletCoreException, UtilityException {
        return zkpManager.createReferent(customReferents);
    }

    @Override
    public Proof createZkpProof(ProofRequest proofRequest,
                                List<ProofParam> proofParams, Map<String, String> selfAttributes) throws WalletCoreException, UtilityException {
        return zkpManager.createProof(proofRequest, proofParams, selfAttributes);
    }

    public boolean isZkpCredentialsSaved(String identifier) throws WalletCoreException, UtilityException, WalletException {
        return zkpManager.isZkpCredentialsSaved(identifier);
    }
}
