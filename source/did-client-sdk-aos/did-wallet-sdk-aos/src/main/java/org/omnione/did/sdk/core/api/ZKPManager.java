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

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.storagemanager.datamodel.FileExtension;
import org.omnione.did.sdk.core.storagemanager.datamodel.UsableInnerWalletItem;
import org.omnione.did.sdk.core.zkp.datamodel.Category;
import org.omnione.did.sdk.core.zkp.datamodel.ZKPInfo;
import org.omnione.did.sdk.core.zkp.datamodel.ZKPMeta;
import org.omnione.did.sdk.datamodel.zkp.AttrReferent;
import org.omnione.did.sdk.datamodel.zkp.AttributeInfo;
import org.omnione.did.sdk.datamodel.zkp.AvailableReferent;
import org.omnione.did.sdk.datamodel.zkp.Credential;
import org.omnione.did.sdk.datamodel.zkp.CredentialOffer;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.CredentialRequest;
import org.omnione.did.sdk.datamodel.zkp.CredentialRequestMeta;
import org.omnione.did.sdk.core.zkp.other.helper.CredentialValueHelper;
import org.omnione.did.sdk.datamodel.zkp.CredentialVerifier;
import org.omnione.did.sdk.datamodel.zkp.Identifiers;
import org.omnione.did.sdk.core.zkp.other.util.KeyPairVerifier;
import org.omnione.did.sdk.datamodel.zkp.MasterSecret;
import org.omnione.did.sdk.datamodel.zkp.MasterSecretBlindingData;
import org.omnione.did.sdk.datamodel.zkp.NonCredentialSchema;
import org.omnione.did.sdk.datamodel.zkp.PredicateInfo;
import org.omnione.did.sdk.datamodel.zkp.PredicateReferent;
import org.omnione.did.sdk.datamodel.zkp.Proof;
import org.omnione.did.sdk.core.zkp.other.util.ProofBuilder;
import org.omnione.did.sdk.datamodel.zkp.ProofParam;
import org.omnione.did.sdk.datamodel.zkp.ProofRequest;
import org.omnione.did.sdk.datamodel.zkp.ProveCredential;
import org.omnione.did.sdk.datamodel.zkp.ProvePredicate;
import org.omnione.did.sdk.datamodel.zkp.ProveRevealedAttribute;
import org.omnione.did.sdk.datamodel.zkp.ProveUnrevealedAttribute;
import org.omnione.did.sdk.datamodel.zkp.Referent;
import org.omnione.did.sdk.datamodel.zkp.ReferentAttributeValue;
import org.omnione.did.sdk.datamodel.zkp.ReferentInfo;
import org.omnione.did.sdk.datamodel.zkp.RequestedProof;
import org.omnione.did.sdk.datamodel.zkp.SubProofRequest;
import org.omnione.did.sdk.datamodel.zkp.SubProofRequestBuilder;
import org.omnione.did.sdk.datamodel.zkp.UserReferent;
import org.omnione.did.sdk.core.zkp.other.util.ZkpConstants;
import org.omnione.did.sdk.datamodel.zkp.CredentialRequestContainer;
import org.omnione.did.sdk.core.zkp.other.helper.CredentialRequestHelper;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerUtil;
import org.omnione.did.sdk.core.zkp.other.util.DateUtil;
import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ZKPManager<E extends BaseObject> {
    Context context;
    private final StorageManager<ZKPMeta, ZKPInfo> storageManager;

    /**
     * ZKPManager: Constructs a ZKPManager instance for managing zero-knowledge credentials.
     *
     * This constructor initializes the ZKPManager by setting up the secure storage system and context environment.
     * It also checks whether a master secret is already saved; if not, it generates and saves a new one automatically.
     *
     * @param fileName The name of the storage file used to persist ZKP-related data.
     * @param context The Android context used for file and storage access.
     * @throws WalletCoreException If an error occurs during master secret initialization or internal wallet logic.
     * @throws UtilityException If a utility-level error occurs while setting up the storage or cryptographic components.
     */
    public ZKPManager(String fileName, Context context) throws WalletCoreException, UtilityException {
        this.context = context;
        storageManager = new StorageManager<>(fileName, FileExtension.FILE_EXTENSION.ZKP, true, context, ZKPInfo.class, ZKPMeta.class);

        WalletLogger.getInstance().d("ZKPManager isSavedMasterSecret(): "+ isSavedMasterSecret());

        if (!isSavedMasterSecret()) {
            String msID = addMasterSecret();
            WalletLogger.getInstance().d("ZKPManager addMasterSecret() msID: "+ msID);
        }
    }

    /**
     * isSavedMasterSecret: Checks whether a master secret is saved in the storage.
     *
     * This method determines if a master secret has been previously stored. It first checks whether the
     * storage itself has been initialized. Then, it retrieves all stored metadata entries and searches
     * for any entry categorized as a master secret.
     *
     * @return Returns true if a master secret exists in the storage; false otherwise.
     * @throws WalletCoreException If an error occurs while accessing the wallet's storage manager.
     * @throws UtilityException If an error occurs during metadata retrieval or processing.
     */
    private boolean isSavedMasterSecret() throws WalletCoreException, UtilityException {

        if (!storageManager.isSaved()) {
            return false;
        }
        List<ZKPMeta> zkpMetas = storageManager.getAllMetas();

        if (zkpMetas.size() == 0) return false;

        for (ZKPMeta zkpMeta : zkpMetas) {
            if (zkpMeta.getCategory() == Category.MASTER_SECRET) {
                return true;
            }
        }
        return false;
    }

    /**
     * addCredential: Stores a given credential into secure storage.
     *
     * This method validates and saves a credential in the ZKP-specific storage. It first checks if the
     * credential object is not null. Then, it creates and populates metadata and credential information
     * objects, wraps them into a wallet item, and saves the item using the storage manager. It also
     * determines whether this is the first credential being saved to set initialization flags accordingly.
     *
     * @param credential The credential object to be stored. Must not be null.
     * @throws WalletCoreException If the credential is null or if an error occurs during the save process.
     * @throws UtilityException If a utility-level error occurs while handling the credential or storage operations.
     */
    private void addCredential(Credential credential) throws WalletCoreException, UtilityException {
        if(credential == null){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "credential");
        }

        UsableInnerWalletItem<ZKPMeta, ZKPInfo> item = new UsableInnerWalletItem<>();
        ZKPMeta zkpMeta = new ZKPMeta();
        zkpMeta.setId(credential.getCredentialId());
        zkpMeta.setCategory(Category.CREDENTIAL);
        item.setMeta(zkpMeta);

        ZKPInfo zkpInfo = new ZKPInfo();
        zkpInfo.setCategory(Category.CREDENTIAL);
        zkpInfo.setId(credential.getCredentialId());
        zkpInfo.setValue(credential.toJson());

        item.setItem(zkpInfo);

        WalletLogger.getInstance().d("ZKPManager addCredentials() item: "+ GsonWrapper.getGson().toJson(item));

        storageManager.addItem(item, !isAnyCredentialsSaved());
    }

    /**
     * getCredentials: Retrieves a list of credentials corresponding to the given identifiers.
     *
     * This method checks for duplicate identifiers and fetches the corresponding credential records
     * from secure storage. If any duplicates are found or if the number of retrieved credentials does
     * not match the number of requested identifiers, an exception is thrown. The retrieved data is
     * deserialized into Credential objects and returned as a list.
     *
     * @param identifiers A list of unique credential identifiers to retrieve.
     * @return Returns a list of Credential objects matching the provided identifiers.
     * @throws WalletCoreException If the identifier list contains duplicates or if some credentials
     *                              could not be found for the given identifiers.
     * @throws UtilityException If a utility error occurs during retrieval or deserialization.
     */
    public List<Credential> getCredentials(List<String> identifiers) throws WalletCoreException, UtilityException {

        if(identifiers.size() != new HashSet<String>(identifiers).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_DUPLICATED_PARAMETER, "identifiers");
        }
        List<UsableInnerWalletItem<ZKPMeta, ZKPInfo>> walletItems = storageManager.getItems(identifiers);

        if (identifiers.size() != walletItems.size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_NOT_FOUND_CREDENTIAL_BY_IDENTIFIERS, "check credential key correctness proof fail");
        }


        List<Credential> vcList = new ArrayList<>();
        for (UsableInnerWalletItem<ZKPMeta, ZKPInfo> walletItem : walletItems) {
            if (walletItem.getItem().getCategory() == Category.CREDENTIAL)
                vcList.add(GsonWrapper.getGson().fromJson(walletItem.getItem().getValue(), Credential.class));
        }

        WalletLogger.getInstance().d("ZKPManager getCredentials() vcList: "+ GsonWrapper.getGson().toJson(vcList));

        return vcList;
    }

    /**
     * getAllCredentials: Retrieves all stored credentials from the wallet.
     *
     * This method fetches all items from the secure ZKP storage and filters out those categorized as credentials.
     * It then deserializes the credential data into Credential objects and collects them into a list.
     * If no credentials are found, an exception is thrown to indicate that the storage is empty.
     *
     * @return Returns a list of all Credential objects currently stored in the wallet.
     * @throws WalletCoreException If no credentials are found in the storage or a storage error occurs.
     * @throws UtilityException If a utility error occurs during retrieval or deserialization.
     */
    public ArrayList<Credential> getAllCredentials() throws WalletCoreException, UtilityException {
        List<UsableInnerWalletItem<ZKPMeta, ZKPInfo>> walletItems = storageManager.getAllItems();

        ArrayList<Credential> vcList = new ArrayList<>();
        for (UsableInnerWalletItem<ZKPMeta, ZKPInfo> walletItem : walletItems) {
            if (walletItem.getItem().getCategory() == Category.CREDENTIAL)
                vcList.add(GsonWrapper.getGson().fromJson(walletItem.getItem().getValue(), Credential.class));
        }

        WalletLogger.getInstance().d("ZKPManager getAllZKPCredentials() vcList: "+ GsonWrapper.getGson().toJson(vcList));

        if (vcList.isEmpty()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_NOT_FOUND_CREDENTIAL_STORED, "Credential not found in storage");
        }
        return vcList;
    }

    /**
     * deleteCredentials: Deletes credentials associated with the specified identifiers.
     *
     * This method removes a list of credentials from secure storage using the provided identifiers.
     * It first validates the input list to ensure it is not null, empty, or contains duplicate values.
     * After deleting the specified credentials, it checks whether any metadata remains in storage.
     * If no metadata is left, it performs a full cleanup of all items in the storage.
     *
     * @param identifiers A list of unique credential identifiers to be deleted.
     * @throws WalletCoreException If the identifier list is null, empty, or contains duplicates.
     * @throws UtilityException If an error occurs while removing items from storage.
     */
    public void deleteCredentials(List<String> identifiers) throws WalletCoreException, UtilityException {
        if(identifiers == null || identifiers.isEmpty()){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "identifiers");
        }
        if(identifiers.size() != new HashSet<String>(identifiers).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_DUPLICATED_PARAMETER, "identifiers");
        }
        storageManager.removeItems(identifiers);
        if(storageManager.getAllMetas().isEmpty()) {
            storageManager.removeAllItems();
        }
    }
    /**
     * deleteAllCredentials: Deletes all credentials currently stored in the wallet.
     *
     * This method retrieves all metadata entries from the storage and filters out those categorized as credentials.
     * It then collects the corresponding identifiers and removes the associated credential items from storage.
     *
     * @throws WalletCoreException If an error occurs while accessing or modifying the wallet storage.
     * @throws UtilityException If a utility-level error occurs during the deletion process.
     */
    public void deleteAllCredentials() throws WalletCoreException, UtilityException {
        List<ZKPMeta> zkpMetas = storageManager.getAllMetas();
        List<String> identifiers = new ArrayList<>();
        for (ZKPMeta zkpMeta : zkpMetas) {
            if (zkpMeta.getCategory() == Category.CREDENTIAL) {
                identifiers.add(zkpMeta.id);
            }
        }

        storageManager.removeItems(identifiers);
    }

    /**
     * isAnyCredentialsSaved: Checks whether any credentials are currently saved in the wallet.
     *
     * This method verifies the existence of stored credentials by first checking if the storage
     * has been initialized and then scanning through the metadata entries. If any metadata entry
     * is categorized as a credential, it returns true, indicating that at least one credential exists.
     *
     * @return Returns true if at least one credential is saved in the storage; false otherwise.
     * @throws WalletCoreException If an error occurs while accessing the wallet's storage.
     * @throws UtilityException If a utility-level error occurs during metadata retrieval or processing.
     */
    public boolean isAnyCredentialsSaved() throws WalletCoreException, UtilityException {
        if (!storageManager.isSaved()) {
            return false;
        }
        List<ZKPMeta> zkpMetas = storageManager.getAllMetas();
        if (zkpMetas.isEmpty()) return false;

        for (ZKPMeta zkpMeta : zkpMetas) {
            if (zkpMeta.getCategory() == Category.CREDENTIAL) {
                return true;
            }
        }
        return false;
    }

    /**
     * addMasterSecret: Generates and stores a new master secret in secure storage.
     *
     * This method creates a new master secret using cryptographic utilities and wraps it in the internal
     * storage structure. It constructs metadata and item representations, sets their respective identifiers
     * and categories, and saves them into secure storage if no master secret currently exists.
     *
     * @return Returns the identifier of the newly created master secret.
     * @throws WalletCoreException If a storage or validation error occurs while saving the master secret.
     * @throws UtilityException If a utility-level error occurs during master secret generation or serialization.
     */
    private String addMasterSecret() throws WalletCoreException, UtilityException {

        MasterSecret masterSecret = new MasterSecret();
        masterSecret.setMasterSecret(new BigIntegerUtil().generateMasterSecret());

        UsableInnerWalletItem<ZKPMeta, ZKPInfo> item = new UsableInnerWalletItem<>();
        ZKPMeta zkpMeta = new ZKPMeta();
        zkpMeta.setId(masterSecret.getMasterSecretId());
        zkpMeta.setCategory(Category.MASTER_SECRET);
        item.setMeta(zkpMeta);

        ZKPInfo zkpInfo = new ZKPInfo();
        zkpInfo.setCategory(Category.MASTER_SECRET);
        zkpInfo.setId(masterSecret.getMasterSecretId());
        zkpInfo.setValue(masterSecret.toJson());

        item.setItem(zkpInfo);

        storageManager.addItem(item, !isSavedMasterSecret());

        return masterSecret.getMasterSecretId();
    }

    /**
     * getMasterSecret: Retrieves the first master secret stored in the wallet.
     *
     * This method fetches all stored master secrets and returns the first one in the list.
     * It assumes that at least one master secret exists. If no master secrets are found,
     * the underlying method may throw an exception due to index access.
     *
     * @return Returns the first MasterSecret object found in storage.
     * @throws WalletCoreException If an error occurs while accessing the wallet storage.
     * @throws UtilityException If a utility-level error occurs during retrieval or parsing.
     */
    private MasterSecret getMasterSecret() throws WalletCoreException, UtilityException {
        return this.getAllMasterSecret().get(0);
    }

    /**
     * getAllMasterSecret: Retrieves all master secrets stored in the wallet.
     *
     * This method accesses all items stored in the wallet and filters them by the MASTER_SECRET category.
     * Each matching item is deserialized into a MasterSecret object and added to the result list.
     * A log entry is created to output the full list of retrieved master secrets.
     *
     * @return Returns a list of MasterSecret objects currently stored.
     * @throws WalletCoreException If an error occurs while accessing or reading from storage.
     * @throws UtilityException If a utility-level error occurs during deserialization or data handling.
     */
    private List<MasterSecret> getAllMasterSecret() throws WalletCoreException, UtilityException {

        List<UsableInnerWalletItem<ZKPMeta, ZKPInfo>> walletItems = storageManager.getAllItems();

        List<MasterSecret> masterSecrets = new ArrayList<>();
        for (UsableInnerWalletItem<ZKPMeta, ZKPInfo> walletItem : walletItems) {
            if (walletItem.getItem().getCategory() == Category.MASTER_SECRET)
                masterSecrets.add(GsonWrapper.getGson().fromJson(walletItem.getItem().getValue(), MasterSecret.class));
        }

        WalletLogger.getInstance().d("ZKPManager getAllMasterSecret() masterSecrets: "+ GsonWrapper.getGson().toJson(masterSecrets));
        return masterSecrets;
    }

    /**
     * deleteMasterSecrets: Deletes master secrets associated with the specified identifiers.
     *
     * This method removes a list of master secrets from secure storage based on the provided identifiers.
     * It first validates that the list is not null, not empty, and contains only unique values.
     * After deleting the specified items, it checks if any metadata remains in the storage.
     * If the storage is empty, it performs a complete cleanup of all remaining items.
     *
     * @param identifiers A list of unique master secret identifiers to be deleted.
     * @throws WalletCoreException If the identifier list is null, empty, or contains duplicates.
     * @throws UtilityException If an error occurs while removing items from storage.
     */
    private void deleteMasterSecrets(List<String> identifiers) throws WalletCoreException, UtilityException {
        if(identifiers == null || identifiers.isEmpty()){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "identifiers");
        }
        if(identifiers.size() != new HashSet<String>(identifiers).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_DUPLICATED_PARAMETER, "identifiers");
        }

        storageManager.removeItems(identifiers);
        if(storageManager.getAllMetas().isEmpty()) {
            storageManager.removeAllItems();
        }
    }

    /**
     * deleteAllMasterSecrets: Deletes all master secrets from the wallet storage.
     *
     * This method removes all items from the secure storage, including any stored master secrets.
     * It performs a complete wipe of the storage contents without filtering by type.
     * Use with caution as this action is irreversible and affects all stored data under this category.
     *
     * @throws WalletCoreException If an error occurs while clearing the storage.
     */
    private void deleteAllMasterSecrets() throws WalletCoreException {
        storageManager.removeAllItems();
    }

    /**
     * createCredentialRequest: Generates a credential request for ZKP-based credential issuance.
     *
     * This method builds a credential request using the prover's DID, the issuer's public key, and the credential offer.
     * It performs validation on all input parameters, checks the correctness proof of the issuer's public key,
     * retrieves the master secret from storage, and generates a prover nonce.
     * Then it creates a blinded credential request along with associated metadata for storage and future verification.
     *
     * @param proverDid The DID of the prover requesting the credential.
     * @param credentialPublicKey The issuer's credential primary public key used in the proof.
     * @param credOffer The credential offer provided by the issuer, including key correctness proof.
     * @return Returns a ZkpRequestCredentialBuilder object containing the credential request and metadata.
     * @throws WalletCoreException If any required parameter is null, key correctness proof verification fails,
     *                              or the master secret cannot be retrieved.
     * @throws UtilityException If a utility-level error occurs during nonce generation or request building.
     */
    public CredentialRequestContainer createCredentialRequest(String proverDid,
                                                              CredentialPrimaryPublicKey credentialPublicKey,
                                                              CredentialOffer credOffer) throws WalletCoreException, UtilityException {
        if (proverDid == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [proverDid has null parameter]");
        if (credentialPublicKey == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [credentialPublicKey has null parameter]");
        if (credOffer == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [credOffer has null parameter]");
        if (!KeyPairVerifier.verify(credentialPublicKey, credOffer.getKeyCorrectnessProof())) {
            WalletLogger.getInstance().d("check credential key correctness proof fail");
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_BIG_NUMBER_COMPARE_FAIL, "check credential key correctness proof fail");
        }

        MasterSecret masterSecret = this.getMasterSecret();
        BigInteger proverNonce = new BigIntegerUtil().generateNonce();
        // create vPrime
        BigInteger v_prime =  new BigIntegerUtil().createRandomBigInteger(ZkpConstants.LARGE_VPRIME);

        WalletLogger.getInstance().d("createCredentialRequest prover nonce: "+proverNonce);

        if (masterSecret == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_SELECT_MASTER_SECRET_FROM_WALLET_FAIL, "select master secret from wallet fail");
        }

        CredentialRequest credentialRequest = CredentialRequestHelper.generateCredentialRequest(credentialPublicKey, proverDid, masterSecret, credOffer, proverNonce, v_prime);

        CredentialRequestMeta credentialRequestMeta = new CredentialRequestMeta(
                new MasterSecretBlindingData(v_prime),
                    proverNonce,
                    masterSecret.getMasterSecretId());

        return new CredentialRequestContainer.Builder()
                .setCredentialRequest(credentialRequest)
                .setCredentialRequestMeta(credentialRequestMeta).
                build();
    }

    /**
     * verifyAndStoreCredential: Verifies the given credential using ZKP and stores it if valid.
     *
     * This method validates the input parameters including the credential, credential public key, and credential request metadata.
     * It then retrieves the master secret and performs a zero-knowledge proof verification to ensure the credential is valid.
     * If verification is successful, the credential is securely stored.
     *
     * @param credentialRequestMeta The metadata associated with the credential request, including blinding data and nonce.
     * @param credentialPrimaryPublicKey The issuer's credential public key used for signature verification.
     * @param credential The credential to be verified and stored.
     * @return Returns true if the credential was successfully verified and stored.
     * @throws WalletCoreException If any parameter is null, the master secret cannot be retrieved, or verification fails.
     * @throws UtilityException If a utility-level error occurs during credential value construction or storage operations.
     */
    public boolean verifyAndStoreCredential(CredentialRequestMeta credentialRequestMeta,
                                                CredentialPrimaryPublicKey credentialPrimaryPublicKey,
                                                Credential credential) throws WalletCoreException, UtilityException {

        if (credentialRequestMeta.getMasterSecretBlindingData() == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [masterSecretBlindingData has null parameter]");
        if (credentialPrimaryPublicKey == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [credentialPrimaryPublicKey has null parameter]");
        if (credential == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [credential has null parameter]");
        if (credentialRequestMeta.getNonce() == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [proverNonce has null parameter]");

        MasterSecret masterSecret = getMasterSecret();

        if (masterSecret == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_SELECT_MASTER_SECRET_FROM_WALLET_FAIL, "select master secret from wallet fail");
        }

        WalletLogger.getInstance().d("masterSecrets: "+GsonWrapper.getGson().toJson(masterSecret));

        if (!CredentialVerifier.verify(
                credential.getCredentialSignature(),
                credential.getSignatureCorrectnessProof(),
                CredentialValueHelper.generateCredentialValues(credential.getValues(), masterSecret),
                credentialPrimaryPublicKey, credentialRequestMeta.getMasterSecretBlindingData().getVPrime(),
                credentialRequestMeta.getNonce())) {

            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_BIG_NUMBER_COMPARE_FAIL, "verify primary credential fail in verify and store credential");
        }

        // Store c1, c2, m2
        this.addCredential(credential);

        return true;
    }

    /**
     * searchCredentials: Searches for credentials that satisfy the given proof request.
     *
     * This method processes the provided ProofRequest to identify and match attributes and predicates
     * against the credentials stored in the wallet. It builds maps for self-attested attributes,
     * revealed attributes, and predicates based on what can be fulfilled from the available credentials.
     *
     * If all requested predicates are not satisfiable, an exception is thrown. The resulting matched
     * referents are packaged into an AvailableReferent object which is returned to the caller.
     *
     * @param proofRequest The ProofRequest object containing requested attributes and predicates.
     * @return Returns an AvailableReferent containing matched self-attested, revealed, and predicate referents.
     * @throws WalletCoreException If the proofRequest is null or the required predicates cannot be satisfied.
     * @throws UtilityException If an internal utility error occurs during credential retrieval or mapping.
     */
    public AvailableReferent searchCredentials(ProofRequest proofRequest) throws WalletCoreException, UtilityException {

        if (proofRequest == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [proofRequest has null parameter]");

        ArrayList<Credential> credentialList = this.getAllCredentials();
        WalletLogger.getInstance().d("credentialList: "+GsonWrapper.getGson().toJson(credentialList));

        // User 에게 반환할 데이터 생성
        Map<String, AttrReferent> selfAttrMap = new HashMap<String, AttrReferent>();
        Map<String, AttrReferent> attrMap = new HashMap<String, AttrReferent>();
        Map<String, PredicateReferent> predicateMap = new HashMap<String, PredicateReferent>();

        Map<String, AttributeInfo> attrInfoMap = proofRequest.getRequestedAttributes();
        Map<String, PredicateInfo> predInfoMap = proofRequest.getRequestedPredicates();

        WalletLogger.getInstance().d("attrInfo: "+ GsonWrapper.getGsonPrettyPrinting().toJson(attrInfoMap));
        WalletLogger.getInstance().d("predicateInfo: "+ GsonWrapper.getGsonPrettyPrinting().toJson(predInfoMap));

        selfAttrMap = AvailableReferent.addSelfAttrReferent(attrInfoMap);
        // VC안에 속성을 못찾은 경우
        attrMap = AvailableReferent.addAttrReferent(attrInfoMap, credentialList);
        // attrMap -> 0개
        predicateMap = AvailableReferent.addPredicateReferent(predInfoMap, credentialList);

        // proofRequest 조건에 부합하지 않으면 실패 리턴
//        if (attrInfoMap.size() != attrMap.size()) {
//            throw new ZkpException(ZkpErrorCode.OMNI_ERROR_ZKP_PROVER_NOT_FOUND_AVAILABLE_REQUEST_ATTRIBUTE);
//        }
        if (predInfoMap.size() != predicateMap.size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_NOT_FOUND_AVAILABLE_PREDICATE_ATTRIBUTE);
        }

        AvailableReferent availableReferent = new AvailableReferent.Builder()
                .setSelfAttrReferent(selfAttrMap)
                .setAttrReferent(attrMap)
                .setPredicateReferent(predicateMap)
                .build();

        WalletLogger.getInstance().d("availableReferent: "+ GsonWrapper.getGsonPrettyPrinting().toJson(availableReferent));

        return availableReferent;
    }

    /**
     * createReferent: Creates referent mappings from user-selected attributes for each credential.
     *
     * This method processes a list of user-defined referent selections and matches them against the
     * stored credentials. For each matching credential, it constructs a Referent object by mapping
     * the selected attribute names and values, then adds it to the ReferentInfo structure.
     * The method also performs null checks on essential referent fields such as referent key, raw value, and name.
     *
     * @param customReferents A list of UserReferent objects representing user-selected attributes for proof generation.
     * @return Returns a ReferentInfo object containing mapped referents for the credentials.
     * @throws WalletCoreException If the customReferents list is null or contains any null fields in its elements.
     * @throws UtilityException If an error occurs during credential retrieval or mapping construction.
     */
    public ReferentInfo createReferent(List<UserReferent> customReferents) throws WalletCoreException, UtilityException {

        if (customReferents == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [customReferents has null parameter]");

        ReferentInfo referentInfo = new ReferentInfo();

        ArrayList<Credential> credentialList =  this.getAllCredentials();

        // LOOP: Credential Key/Value
        for (Credential credential: credentialList) {
            Referent ref = new Referent();
            LinkedHashMap<String, ReferentAttributeValue> attr = new LinkedHashMap<String, ReferentAttributeValue>();

            for (UserReferent inputReferent : customReferents) {

                if (credential.getCredentialId().equals(inputReferent.getCredentialId())) {
                    if (inputReferent.getReferentKey() == null)
                        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "referent key is null in createReferent");
                    else if (inputReferent.getRaw() == null)
                        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "referent raw is null in createReferent");
                    else if (inputReferent.getReferentName() == null)
                        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "referent name is null in createReferent");

                    WalletLogger.getInstance().d("credentialKey: "+credential.getCredentialId()+", inputReferent.getReferentName(): "+GsonWrapper.getGsonPrettyPrinting().toJson(inputReferent.getReferentName()));
                    attr.put(inputReferent.getReferentName(), new ReferentAttributeValue(inputReferent.getReferentKey(), inputReferent.isRevealed()));
                    ref.setSchemaId(credential.getSchemaId());
                    ref.setCredDefId(credential.getCredDefId());
                }
            }

            if (attr.size() > 0) {
                ref.setAttributes(attr);
                referentInfo.addReferent(credential.getCredentialId(), ref);
            }
        }

        return referentInfo;
    }

    /**
     * getProvingCredential: Constructs a list of credentials and attribute values required for zero-knowledge proof generation.
     *
     * This method processes the provided proof request and associated proof parameters to identify
     * which attributes from the stored credentials will be revealed, unrevealed, or used in predicates.
     * It builds a list of ProveCredential objects, each of which contains the proper structure
     * and metadata needed for constructing a ZKP presentation.
     *
     * For each ProofParam, this method extracts the associated Referent data and compares each
     * requested attribute and predicate in the ProofRequest. Based on the configuration (revealed or not),
     * it constructs the appropriate ProveRevealedAttribute, ProveUnrevealedAttribute, and ProvePredicate objects.
     *
     * @param proofRequest The ProofRequest object specifying the attributes and predicates required by the Verifier.
     * @param proofParams A list of ProofParam objects selected by the user that contain referent mappings and credential info.
     * @return Returns a list of ProveCredential objects representing the structure required for ZKP generation.
     * @throws WalletCoreException If no matching credentials are found or the data cannot be built for proving.
     */
    private List<ProveCredential> getProvingCredential(ProofRequest proofRequest, List<ProofParam> proofParams) throws WalletCoreException {

        WalletLogger.getInstance().d("proving start ===============================================================proofParams.size: "+proofParams.size());
        List<ProveCredential> proveCredentialList = new LinkedList<ProveCredential>();

        for (ProofParam proofParam : proofParams) {
            HashMap<String, Referent> referents = proofParam.getReferentInfo().getReferents();

            List<ProveRevealedAttribute> revealedAttrs = new LinkedList<ProveRevealedAttribute>();
            List<ProveUnrevealedAttribute> unrevealedAttrs = new LinkedList<ProveUnrevealedAttribute>();
            List<ProvePredicate> predicates = new LinkedList<ProvePredicate>();

            // LOOP: referents
            for (String referentKey : referents.keySet()) {
                Referent referent = referents.get(referentKey);

                // LOOP: attr
                for (String attrKey : referent.getAttributes().keySet()) {
                    ReferentAttributeValue referentAttributeValue = referent.getAttributes().get(attrKey);

                    // revealed Attrs 생성
                    for (String proofRequestAttrKey : proofRequest.getRequestedAttributes().keySet()) {
                        AttributeInfo requestAttrInfo = proofRequest.getRequestedAttributes().get(proofRequestAttrKey);

                        if (requestAttrInfo.getName().equals(attrKey) && referentAttributeValue.getRevealed()) {
                            revealedAttrs.add(new ProveRevealedAttribute.Builder()
                                    .setAttributeName(attrKey)
                                    .setReferentKey(referentAttributeValue.getReferentKey())
                                    .build());
                        } else if (requestAttrInfo.getName().equals(attrKey) && !referentAttributeValue.getRevealed()) {
                            unrevealedAttrs.add(new ProveUnrevealedAttribute.Builder()
                                    .setReferentKey(referentAttributeValue.getReferentKey())
                                    .build());
                        }
                    }

                    // predicate Attrs 생성
                    for (String proofRequestPredicateKey : proofRequest.getRequestedPredicates().keySet()) {
                        PredicateInfo requestPredicateInfo = proofRequest.getRequestedPredicates().get(proofRequestPredicateKey);

                        if (requestPredicateInfo.getName().equals(attrKey) && !referentAttributeValue.getRevealed()) {
                            predicates.add(new ProvePredicate.Builder()
                                    .setAttributeName(attrKey)
                                    .setReferentKey(referentAttributeValue.getReferentKey())
                                    .setPType(requestPredicateInfo.getPType())
                                    .setPValue(requestPredicateInfo.getPValue())
                                    .build());
                        }
                    }
                }

                proveCredentialList.add(new ProveCredential.Builder()
                        .setCredentialId(referentKey)
                        .setRevealedAttrs(revealedAttrs)
                        .setUnrevealedAttrs(unrevealedAttrs)
                        .setPredicates(predicates)
                        .setTimestemp(String.valueOf(DateUtil.getTimestamp()))
                        .setSchema(proofParam.getSchema())
                        .setCredentialDefinition(proofParam.getCredDef())
                        .build());
            }
        }

        if (proveCredentialList.size() == 0)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_BUILD_CREDENTIAL_FOR_PROVING_FAIL, "validRequest fail [proofRequest has null parameter]");

        WalletLogger.getInstance().d("credential for proving: " + GsonWrapper.getGsonPrettyPrinting().toJson(proveCredentialList));
        WalletLogger.getInstance().d("=============================================================== proving end");
        return proveCredentialList;
    }

    /**
     * createProof: Generates a zero-knowledge proof based on the provided proof request and selected credentials.
     *
     * This method takes in a proof request, proof parameters, and optional self-attested attributes. It performs validation,
     * constructs the required revealed, unrevealed, and predicate attributes, builds the requested proof, and then
     * generates the final proof object using a ZKP proof builder.
     *
     * The method iterates through each selected credential and its referent mappings, applies appropriate attributes to
     * the proof structure, and links them with schemas, credential definitions, and cryptographic values. It handles both
     * self-attested attributes and schema-based attributes, ensuring that the resulting proof satisfies the proof request
     * requirements set by the verifier.
     *
     * @param proofRequest The ProofRequest containing the requested attributes, predicates, and nonce.
     * @param proofParams A list of ProofParam objects specifying the referent details and selected credentials.
     * @param selfAttributes A map of self-attested attributes to be included in the proof.
     * @return Returns a fully constructed Proof object that can be sent to a verifier for verification.
     * @throws WalletCoreException If any input is null, schema is missing, or proof creation fails due to validation or storage issues.
     * @throws UtilityException If a utility-level failure occurs during encoding, credential value generation, or proof building.
     */
    public Proof createProof(ProofRequest proofRequest, List<ProofParam> proofParams, Map<String, String> selfAttributes) throws WalletCoreException, UtilityException {
        if (proofParams == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [proofParams has null parameter]");
        if (proofRequest == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [proofRequest has null parameter]");
        if (selfAttributes == null)
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PARAMETER_VALID_FAIL, "validRequest fail [selfAttributes has null parameter]");

        WalletLogger.getInstance().d("proofParams: " + GsonWrapper.getGsonPrettyPrinting().toJson(proofParams));
        WalletLogger.getInstance().d("selfAttributes: " + GsonWrapper.getGsonPrettyPrinting().toJson(selfAttributes));

        List<ProveCredential> proveCredentialList = this.getProvingCredential(proofRequest, proofParams);

        // proof 생성
        ProofBuilder builder = new ProofBuilder(ZkpConstants.MASTER_SECRET_KEY);

        List<Identifiers> identifierList = new LinkedList<Identifiers>();
        RequestedProof requestedProof = new RequestedProof();

        ArrayList<Credential> credentialList = this.getAllCredentials();

        //LOOP proving, credential,schema, def
        for (int i = 0 ; i < proveCredentialList.size() ; i ++) {

            ProveCredential proveCredential = proveCredentialList.get(i);
            // LOOP: Credential Key/Value
            for (Credential credential : credentialList) {

                if (credential.getCredentialId().equals(proveCredential.getCredentialId())) {
                    // Referent : credential 1개가 subProof
                    Map<String, Map<String, String>> revealedAttrs = new LinkedHashMap<String, Map<String, String>>();

                    for (ProveRevealedAttribute revealedAttribute: proveCredential.getRevealedAttrs()) {
                        Map<String, String> subRevealedAttrs = new LinkedHashMap<String, String>();
                        subRevealedAttrs.put("subProofIndex", String.valueOf(i));
                        subRevealedAttrs.put("row", credential.getValues().get(revealedAttribute.getAttributeName()).getRaw());
                        subRevealedAttrs.put("encoded", credential.getValues().get(revealedAttribute.getAttributeName()).getEncode().toString());
                        revealedAttrs.put(revealedAttribute.getReferentKey(), subRevealedAttrs);
                    }
                    requestedProof.addRevealedAttrs(revealedAttrs);

                    Map<String, Map<String, String>> unrevealedAttrs = new LinkedHashMap<String, Map<String, String>>();
                    Map<String, String> subUnrevealedAttrs = new LinkedHashMap<String, String>();

                    for (ProveUnrevealedAttribute unrevealedAttribute: proveCredential.getUnrevealedAttrs()) {
                        subUnrevealedAttrs.put("subProofIndex", String.valueOf(i));
                        unrevealedAttrs.put(unrevealedAttribute.getReferentKey(), subUnrevealedAttrs);
                    }
                    requestedProof.addUnrevealedAttrs(unrevealedAttrs);

                    Map<String, Map<String, String>>predicates = new LinkedHashMap<String, Map<String, String>>();
                    Map<String, String>subPredicates = new HashMap<String, String>();

                    for (ProvePredicate provePredicate: proveCredential.getPredicates()) {
                        subPredicates.put("subProofIndex", String.valueOf(i));
                        predicates.put(provePredicate.getReferentKey(), subPredicates);
                    }
                    requestedProof.addPredicates(predicates);

                    Map<String, String> attestedAttr = new LinkedHashMap<String, String>();

                    for (String key : selfAttributes.keySet()) {
                        attestedAttr.put(key, selfAttributes.get(key));
                    }
                    requestedProof.addSelfAttestedAttrs(attestedAttr);
                    WalletLogger.getInstance().d("requestedProof: "+GsonWrapper.getGsonPrettyPrinting().toJson(requestedProof));

                    if (proveCredential.getSchema() == null)
                        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_NOT_FOUND_SCHEMA_FROM_LIST, "not found schema from list");

                    identifierList.add(new Identifiers(proveCredential.getSchema().getId(),
                            proveCredential.getCredentialDefinition().getId()));

                    SubProofRequestBuilder subProofRequestBuilder = new SubProofRequestBuilder();

                    HashSet<String> requestedAttributes = new LinkedHashSet<String>();
                    for (ProveRevealedAttribute proveRevealedAttribute : proveCredential.getRevealedAttrs()) {
                        requestedAttributes.add(proveRevealedAttribute.getAttributeName());
                    }
                    subProofRequestBuilder.addRevealedAttr(requestedAttributes);// referent 의 name address.. encoded value

                    HashSet<PredicateInfo> predicateAttributes = new LinkedHashSet<PredicateInfo>();

                    for (ProvePredicate provePredicate : proveCredential.getPredicates()) {
                        PredicateInfo predicateInfo = new PredicateInfo();
                        predicateInfo.setName(provePredicate.getAttrName());
                        predicateInfo.setPType(provePredicate.getPType());
                        predicateInfo.setPValue(provePredicate.getPValue());

                        predicateAttributes.add(predicateInfo);
                    }

                    subProofRequestBuilder.addPredicateAttr(predicateAttributes);
                    SubProofRequest subProofRequest = subProofRequestBuilder.build();

                    WalletLogger.getInstance().d("subProofRequest: "+GsonWrapper.getGsonPrettyPrinting().toJson(subProofRequest));

                    NonCredentialSchema nonCredentialSchema = new NonCredentialSchema();
                    nonCredentialSchema.addAttr(ZkpConstants.MASTER_SECRET_KEY);

                    WalletLogger.getInstance().d("credential=====================================================================");
                    WalletLogger.getInstance().d(GsonWrapper.getGsonPrettyPrinting().toJson(credential));
                    WalletLogger.getInstance().d("=====================================================================credential");

                    builder.addSubProofRequest(subProofRequest,
                            proveCredential.getSchema(),
                            nonCredentialSchema,
                            CredentialValueHelper.generateCredentialValues(credential.getValues(), this.getMasterSecret()),
                            credential.getCredentialSignature(),
                            proveCredential.getCredentialDefinition().getValue().getPrimary());
                }
            }
        }

        Proof proof = builder.build(proofRequest.getNonce(), requestedProof, identifierList);
        WalletLogger.getInstance().d("proof: "+GsonWrapper.getGson().toJson(proof));

        return proof;
    }
}
