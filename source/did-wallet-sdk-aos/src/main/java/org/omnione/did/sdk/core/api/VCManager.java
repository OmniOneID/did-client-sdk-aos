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
import org.omnione.did.sdk.core.vcmanager.datamodel.ClaimInfo;
import org.omnione.did.sdk.core.vcmanager.datamodel.PresentationInfo;
import org.omnione.did.sdk.core.vcmanager.datamodel.VcMeta;
import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.datamodel.vc.Claim;
import org.omnione.did.sdk.datamodel.vc.CredentialSubject;
import org.omnione.did.sdk.datamodel.vc.VCProof;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;
import org.omnione.did.sdk.datamodel.vp.VerifiablePresentation;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

class VCManager<E extends BaseObject> {
    private StorageManager<VcMeta, VerifiableCredential> storageManager;
    public VCManager(){}
    public VCManager(String fileName, Context context) {
        storageManager = new StorageManager<>(fileName, FileExtension.FILE_EXTENSION.VC, true, context, VerifiableCredential.class, VcMeta.class);
    }

    /**
     * Checks if any credentials are saved.
     *
     * @return true if any credentials are saved, false otherwise
     */
    public boolean isAnyCredentialsSaved(){
        return storageManager.isSaved();
    }

    /**
     * Adds a verifiable credential.
     *
     * @param verifiableCredential the credential to be added
     * @throws Exception if the credential is null, the credential ID already exists, or the credential has no claims
     */
    public void addCredentials(VerifiableCredential verifiableCredential) throws WalletCoreException, UtilityException  {
        if(verifiableCredential == null){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_VC_MANAGER_INVALID_PARAMETER, "VerifiableCredential");
        }

//        if(isAnyCredentialsSaved()){
//            List<VcMeta> vcMetas = storageManager.getAllMetas();
//            for (VcMeta vcMeta : vcMetas) {
//                if (vcMeta.getId().equals(verifiableCredential.getId())) {
//                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_VC_MANAGER_NO_CLAIM_CODE_IN_CREDENTIAL_FOR_PRESENTATION);
//                }
//            }
//        }
        UsableInnerWalletItem<VcMeta, VerifiableCredential> item = new UsableInnerWalletItem<>();
        item.setItem(verifiableCredential);
        VcMeta vcMeta = new VcMeta();
        vcMeta.setId(verifiableCredential.getId());
        vcMeta.setIssuerId(verifiableCredential.getIssuer().getId());
        vcMeta.setCredentialSchemaId(verifiableCredential.getCredentialSchema().getId());
        vcMeta.setCredentialSchemaType(verifiableCredential.getCredentialSchema().getType().getValue());
        item.setMeta(vcMeta);
        storageManager.addItem(item, !isAnyCredentialsSaved());

    }

    /**
     * Gets specific verifiable credentials by their IDs.
     *
     * @param identifiers the list of credential IDs to gets
     * @return a list of saved verifiable credentials
     * @throws Exception if no credentials are saved or the credentials are not found
     */
    public List<VerifiableCredential> getCredentials(List<String> identifiers) throws WalletCoreException, UtilityException {
        if(identifiers.size() != new HashSet<String>(identifiers).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_VC_MANAGER_DUPLICATED_PARAMETER, "identifiers");
        }
        List<UsableInnerWalletItem<VcMeta,VerifiableCredential>> walletItems = storageManager.getItems(identifiers);

        List<VerifiableCredential> vcList = new ArrayList<>();
        for (UsableInnerWalletItem<VcMeta, VerifiableCredential> walletItem : walletItems) {
            vcList.add(walletItem.getItem());
        }
        return vcList;

    }
    /**
     * Gets all saved verifiable credentials.
     *
     * @return a list of all saved verifiable credentials
     * @throws Exception if no credentials are saved or no credentials are found
     */
    public List<VerifiableCredential> getAllCredentials() throws WalletCoreException, UtilityException {
        List<UsableInnerWalletItem<VcMeta,VerifiableCredential>> walletItems = storageManager.getAllItems();

        List<VerifiableCredential> vcList = new ArrayList<>();
        for (UsableInnerWalletItem<VcMeta, VerifiableCredential> walletItem : walletItems) {
            vcList.add(walletItem.getItem());
        }
        return vcList;
    }

    /**
     * Deletes specific verifiable credentials by their IDs.
     *
     * @param identifiers the list of credential IDs to delete
     * @throws Exception if the credential IDs list is null or empty, or if no credentials are saved
     */
    public void deleteCredentials(List<String> identifiers) throws WalletCoreException, UtilityException {
        if(identifiers == null || identifiers.size() == 0){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_VC_MANAGER_INVALID_PARAMETER, "identifiers");
        }
        if(identifiers.size() != new HashSet<String>(identifiers).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_VC_MANAGER_DUPLICATED_PARAMETER, "identifiers");
        }
        storageManager.removeItems(identifiers);
        if(storageManager.getAllMetas().size() == 0) {
            storageManager.removeAllItems();
        }
    }
    /**
     * Deletes all saved verifiable credentials.
     *
     * @throws Exception if no credentials are saved
     */
    public void deleteAllCredentials() throws WalletCoreException {
        storageManager.removeAllItems();
    }

    /**
     * Creates a Verifiable Presentation (without proof).
     *
     * @param claimInfos the list of claim information to include
     * @param presentationInfo the presentation information
     * @return the created Verifiable Presentation
     * @throws Exception if the input parameters are null or empty, or if no credentials are saved
     */
    public VerifiablePresentation makePresentation(List<ClaimInfo> claimInfos, PresentationInfo presentationInfo) throws WalletCoreException, UtilityException {
        if(claimInfos == null || claimInfos.size() == 0){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_VC_MANAGER_INVALID_PARAMETER, "claimInfos");
        }
        if(presentationInfo == null || presentationInfo.getValidUntil().isEmpty()){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_VC_MANAGER_INVALID_PARAMETER, "presentationInfo");
        }
        List<VerifiableCredential> filteredByRequestInfo = new ArrayList<>();

        LinkedHashMap<String, List<String>> claimInfoMap = new LinkedHashMap<String, List<String>>();

        List<String> credentialIds = new ArrayList<>();
        for(int i=0; i < claimInfos.size(); i++) {
            String credentialId = claimInfos.get(i).getCredentialId();
            List<String> claim = claimInfos.get(i).getClaimCodes();
            claimInfoMap.put(credentialId, claim);

            credentialIds.add(credentialId);
        }
        List<VerifiableCredential> vcListByIds = getCredentials(credentialIds);

        for(VerifiableCredential vcByIds : vcListByIds){
            List<Claim> tempClaims = new ArrayList<>();
            VerifiableCredential tempVc = vcByIds;
            List<String> tempProofValueList = new ArrayList<>();

            WalletLogger.getInstance().d("LOG_1: "+ GsonWrapper.getGson().toJson(vcByIds.getCredentialSubject().getClaims()));

            for(Claim claim : vcByIds.getCredentialSubject().getClaims()){
                WalletLogger.getInstance().d("claim: "+ GsonWrapper.getGson().toJson(claim));
                WalletLogger.getInstance().d("claimInfoMap: "+ GsonWrapper.getGson().toJson(claimInfoMap));
                WalletLogger.getInstance().d("vcByIds.getId(): "+ vcByIds.getId());
                WalletLogger.getInstance().d("tempVc: "+ GsonWrapper.getGson().toJson(tempVc));

                for(int i = 0; i < claimInfoMap.get(vcByIds.getId()).size(); i++){
                    if(claimInfoMap.get(vcByIds.getId()).get(i).equals(claim.getCode())) {
                        tempClaims.add(claim);
                        tempProofValueList.add(tempVc.getProof().getProofValueList().get(tempVc.getCredentialSubject().getClaims().indexOf(claim)));
                    }
                }
            }
            if(tempClaims.size() == 0){
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_VC_MANAGER_NO_CLAIM_CODE_IN_CREDENTIAL_FOR_PRESENTATION);
            }
            CredentialSubject tempCredentialSubject = tempVc.getCredentialSubject();
            VCProof tempVcProof = tempVc.getProof();
            tempVcProof.setProofValue(null);
            tempVcProof.setProofValueList(tempProofValueList);
            tempVc.setProof(tempVcProof);
            tempCredentialSubject.setClaims(tempClaims);
            tempVc.setCredentialSubject(tempCredentialSubject);
            filteredByRequestInfo.add(tempVc);
        }
        VerifiablePresentation verifiablePresentation = new VerifiablePresentation(
                presentationInfo.getHolder(),
                presentationInfo.getValidFrom(),
                presentationInfo.getValidUntil(),
                presentationInfo.getVerifierNonce(),
                filteredByRequestInfo
        );
        return verifiablePresentation;
    }
}
