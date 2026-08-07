/*
 * Copyright 2026 OmniOne.
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
import org.omnione.did.sdk.datamodel.oid4vc.OID4VCICredential;
import org.omnione.did.sdk.datamodel.oid4vc.OID4VCICredentialMeta;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class OID4VCManager {
    private StorageManager<OID4VCICredentialMeta, OID4VCICredential> storageManager;

    public OID4VCManager() {}

    public OID4VCManager(String fileName, Context context) {
        storageManager = new StorageManager<>(fileName, FileExtension.FILE_EXTENSION.OID4VC, true, context,
                OID4VCICredential.class, OID4VCICredentialMeta.class);
    }

    public boolean isAnyCredentialsSaved() {
        return storageManager.isSaved();
    }

    public void addCredential(OID4VCICredential credential) throws WalletCoreException, UtilityException {
        if (credential == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER, "OID4VCICredential");
        }
        UsableInnerWalletItem<OID4VCICredentialMeta, OID4VCICredential> item = new UsableInnerWalletItem<>();
        item.setItem(credential);
        OID4VCICredentialMeta meta = new OID4VCICredentialMeta();
        meta.setId(credential.getId());
        meta.setFormat(credential.getFormat());
        meta.setCredentialConfigurationId(credential.getCredentialConfigurationId());
        item.setMeta(meta);
        storageManager.addItem(item, !isAnyCredentialsSaved());
    }

    public List<OID4VCICredential> getCredentials(List<String> identifiers) throws WalletCoreException, UtilityException {
        if (identifiers == null || identifiers.size() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER, "identifiers");
        }
        if (identifiers.size() != new HashSet<String>(identifiers).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_MANAGER_DUPLICATED_PARAMETER, "identifiers");
        }
        List<UsableInnerWalletItem<OID4VCICredentialMeta, OID4VCICredential>> walletItems = storageManager.getItems(identifiers);

        List<OID4VCICredential> credentialList = new ArrayList<>();
        for (UsableInnerWalletItem<OID4VCICredentialMeta, OID4VCICredential> walletItem : walletItems) {
            credentialList.add(walletItem.getItem());
        }
        return credentialList;
    }

    public List<OID4VCICredential> getAllCredentials() throws WalletCoreException, UtilityException {
        List<UsableInnerWalletItem<OID4VCICredentialMeta, OID4VCICredential>> walletItems = storageManager.getAllItems();

        List<OID4VCICredential> credentialList = new ArrayList<>();
        for (UsableInnerWalletItem<OID4VCICredentialMeta, OID4VCICredential> walletItem : walletItems) {
            credentialList.add(walletItem.getItem());
        }
        return credentialList;
    }

    public void deleteCredentials(List<String> identifiers) throws WalletCoreException, UtilityException {
        if (identifiers == null || identifiers.size() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_MANAGER_INVALID_PARAMETER, "identifiers");
        }
        if (identifiers.size() != new HashSet<String>(identifiers).size()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_MANAGER_DUPLICATED_PARAMETER, "identifiers");
        }
        storageManager.removeItems(identifiers);
        if (storageManager.getAllMetas().size() == 0) {
            storageManager.removeAllItems();
        }
    }

    public void deleteAllCredentials() throws WalletCoreException {
        storageManager.removeAllItems();
    }
}
