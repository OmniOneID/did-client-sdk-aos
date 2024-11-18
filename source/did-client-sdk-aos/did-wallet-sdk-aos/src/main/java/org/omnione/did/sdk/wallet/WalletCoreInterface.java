/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.wallet;

import android.content.Context;

import androidx.fragment.app.Fragment;

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.vcmanager.datamodel.ClaimInfo;
import org.omnione.did.sdk.core.vcmanager.datamodel.PresentationInfo;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;
import org.omnione.did.sdk.datamodel.vp.VerifiablePresentation;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.util.List;

public interface WalletCoreInterface {
    DIDDocument createDeviceDIDDoc() throws WalletCoreException, UtilityException;
    DIDDocument createHolderDIDDoc() throws WalletCoreException, UtilityException, WalletException;
    void generateKeyPair(String passcode) throws WalletCoreException, UtilityException, WalletException;
    DIDDocument getDocument(int type) throws WalletCoreException, UtilityException, WalletException;
    void saveDocument(int type) throws WalletCoreException, UtilityException, WalletException;
    boolean isExistWallet();
    void deleteWallet() throws WalletCoreException;
    boolean isAnyCredentialsSaved() throws WalletException;
    void addCredentials(VerifiableCredential verifiableCredential) throws WalletCoreException, UtilityException, WalletException;
    List<VerifiableCredential> getCredentials(List<String> identifiers) throws WalletCoreException, UtilityException, WalletException;
    List<VerifiableCredential> getAllCredentials() throws WalletCoreException, UtilityException, WalletException;
    void deleteCredentials(List<String> identifiers) throws WalletCoreException, UtilityException, WalletException;
    VerifiablePresentation makePresentation(List<ClaimInfo> claimInfos, PresentationInfo presentationInfo) throws WalletCoreException, UtilityException, WalletException;
    void registerBioKey(Context ctx) throws WalletException;
    void authenticateBioKey(Fragment fragment, Context ctx) throws WalletCoreException, WalletException;
    byte[] sign(String id, byte[] pin, byte[] digest, int type) throws WalletCoreException, UtilityException, WalletException;
    boolean verify(byte[] publicKey, byte[] digest, byte[] signature) throws WalletCoreException, UtilityException, WalletException;
    boolean isSavedKey(String id) throws WalletCoreException, UtilityException, WalletException;
    void changePin(String keyId, String oldPin, String newPin) throws WalletCoreException, UtilityException;
}
