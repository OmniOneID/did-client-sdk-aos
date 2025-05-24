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

package org.omnione.did.sdk.wallet;

import org.omnione.did.sdk.datamodel.did.SignedDidDoc;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.datamodel.token.SignedWalletInfo;
import org.omnione.did.sdk.datamodel.vc.issue.ReturnEncVP;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.common.enums.VerifyAuthType;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.profile.IssueProfile;
import org.omnione.did.sdk.datamodel.profile.ReqE2e;
import org.omnione.did.sdk.datamodel.zkp.CredentialOffer;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;
import org.omnione.did.sdk.core.exception.WalletCoreException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface WalletServiceInterface {
    void fetchCaInfo(String tasUrl) throws WalletException, ExecutionException, InterruptedException;
    void createDeviceDocument(String walletUrl, String tasUrl) throws WalletException, WalletCoreException, UtilityException, ExecutionException, InterruptedException;
    boolean bindUser();
    boolean unbindUser();
    void deleteWallet() throws WalletCoreException, WalletException;
    DIDDocument createHolderDIDDoc() throws UtilityException, WalletCoreException, WalletException;
    SignedDidDoc createSignedDIDDoc(DIDDocument ownerDIDDoc) throws WalletException, WalletCoreException, UtilityException;
    SignedWalletInfo getSignedWalletInfo() throws WalletException, WalletCoreException, UtilityException;
    CompletableFuture<String> requestRegisterUser(String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc);
    CompletableFuture<String> requestRestoreUser(String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws WalletException, ExecutionException, InterruptedException;
    CompletableFuture<String> requestUpdateUser(String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws WalletException, ExecutionException, InterruptedException;
    DIDAuth getSignedDIDAuth(String authNonce, String pin) throws WalletException, WalletCoreException, UtilityException;

    CompletableFuture<String> requestIssueVc(String tasUrl, String apiGateWayUrl, String serverToken, String refId, IssueProfile profile, DIDAuth signedDIDAuth, String txId) throws WalletException, WalletCoreException, UtilityException, ExecutionException, InterruptedException;
    CompletableFuture<String> requestRevokeVc(String tasUrl, String serverToken, String txId, String vcId, String issuerNonce, String passcode, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws WalletException, WalletCoreException, UtilityException,  ExecutionException, InterruptedException;
    ReturnEncVP createEncVp(String vcId, List<String> claimCode, ReqE2e reqE2e, String passcode, String nonce, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws WalletException, WalletCoreException, UtilityException;
    ProofContainer addProofsToDocument(ProofContainer document, List<String> keyIds, String did, int type, String passcode, boolean isDIDAuth) throws WalletException, WalletCoreException, UtilityException;
}