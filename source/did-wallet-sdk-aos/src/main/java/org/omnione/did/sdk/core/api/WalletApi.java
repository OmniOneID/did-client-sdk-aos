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

import androidx.fragment.app.Fragment;

import org.omnione.did.sdk.datamodel.did.SignedDidDoc;
import org.omnione.did.sdk.datamodel.profile.ProofRequestProfile;
import org.omnione.did.sdk.datamodel.protocol.P310ZkpRequestVo;
import org.omnione.did.sdk.datamodel.protocol.P310ZkpResponseVo;
import org.omnione.did.sdk.datamodel.vc.issue.ReturnEncVP;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.common.enums.VerifyAuthType;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.profile.ReqE2e;
import org.omnione.did.sdk.datamodel.zkp.AvailableReferent;
import org.omnione.did.sdk.datamodel.zkp.Credential;
import org.omnione.did.sdk.datamodel.zkp.CredentialDefinition;
import org.omnione.did.sdk.datamodel.zkp.CredentialOffer;
import org.omnione.did.sdk.datamodel.zkp.CredentialPrimaryPublicKey;
import org.omnione.did.sdk.datamodel.zkp.CredentialRequestContainer;
import org.omnione.did.sdk.datamodel.zkp.CredentialRequestMeta;
import org.omnione.did.sdk.datamodel.zkp.Proof;
import org.omnione.did.sdk.datamodel.zkp.ProofParam;
import org.omnione.did.sdk.datamodel.zkp.ProofRequest;
import org.omnione.did.sdk.datamodel.zkp.ReferentInfo;
import org.omnione.did.sdk.datamodel.zkp.UserReferent;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;
import org.omnione.did.sdk.core.bioprompthelper.BioPromptHelper;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.datamodel.profile.IssueProfile;
import org.omnione.did.sdk.datamodel.token.SignedWalletInfo;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;
import org.omnione.did.sdk.wallet.walletservice.LockManager;
import org.omnione.did.sdk.datamodel.token.WalletTokenData;
import org.omnione.did.sdk.datamodel.common.enums.WalletTokenPurpose;
import org.omnione.did.sdk.datamodel.token.WalletTokenSeed;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WalletApi {
    private Context context;
    private static WalletApi instance;
    public static boolean isLock = true;
    WalletToken walletToken;
    LockManager lockManager;
    WalletCore walletCore;
    WalletService walletService;
    WalletLogger walletLogger;

    /**
     * @param bioPromptInterface
     */
    public void setBioPromptListener(BioPromptHelper.BioPromptInterface bioPromptInterface) {
        this.bioPromptInterface = bioPromptInterface;
    }

    public interface BioPromptInterface {
        void onSuccess(String result);

        void onFail(String result);
    }

    private BioPromptHelper.BioPromptInterface bioPromptInterface;

    private WalletApi() {
    }

    private WalletApi(Context context) throws WalletCoreException {
        this.context = context;
        lockManager = new LockManager(context);
        walletCore = new WalletCore(context);
        walletToken = new WalletToken(context, walletCore);
        walletService = new WalletService(context, walletCore);
        walletLogger = WalletLogger.getInstance();
    }

    public static WalletApi getInstance(Context context) throws WalletCoreException {
        if (instance == null) {
            instance = new WalletApi(context);
        }
        return instance;
    }

    public boolean isExistWallet() {
        walletLogger.d("isExistWallet");
        return walletCore.isExistWallet();
    }

    /**
     * Creates a wallet and performs necessary setup operations such as fetching CA (Certificate App) package information
     * and creating a device DID (Decentralized Identifier) document. This method handles all the required steps to
     * initialize a wallet on the device.
     *
     * @return boolean - Returns `true` if the wallet exists after creation, otherwise `false`.
     * @throws Exception - If an error occurs during the wallet creation process, including issues with fetching CA package information,
     *                   creating the device DID document, or any other wallet-related operation.
     */
    public boolean createWallet(String walletUrl, String tasUrl) throws WalletException, WalletCoreException, UtilityException, ExecutionException, InterruptedException {
        walletLogger.d("createWallet : " + walletUrl + " / " + tasUrl);
        walletService.fetchCaInfo(tasUrl);
        walletService.createDeviceDocument(walletUrl, tasUrl);
        return isExistWallet();
    }

    public void deleteWallet() throws WalletCoreException {
        walletLogger.d("deleteWallet");
        walletService.deleteWallet();
    }

    /**
     * Creates a wallet token seed for the specified purpose, package name, and user ID.
     *
     * @param purpose The purpose of the wallet token, defined by the `WALLET_TOKEN_PURPOSE` enum.
     * @param pkgName The CA package name associated with the wallet token.
     * @param userId  The user ID for which the wallet token seed is being created.
     * @return WalletTokenSeed - The created wallet token seed.
     * @throws Exception - If an error occurs during the token seed creation process,
     *                   including wallet core issues or utility operation failures.
     */
    public WalletTokenSeed createWalletTokenSeed(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName, String userId) throws WalletCoreException, UtilityException, WalletException {
        return walletToken.createWalletTokenSeed(purpose, pkgName, userId);
    }

    /**
     * Creates a nonce (a unique number used once) for the provided wallet token data.
     *
     * @param walletTokenData The data for which the nonce is being created, represented by the `WalletTokenData` object.
     * @return String - The created nonce.
     * @throws Exception - If an error occurs during the nonce creation process,
     *                   including wallet interaction, utility operation failures, or wallet core issues.
     */
    public String createNonceForWalletToken(String apiGateWayUrl, WalletTokenData walletTokenData) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException {
        return walletToken.createNonceForWalletToken(apiGateWayUrl, walletTokenData);
    }

    /**
     * Binds a user to the wallet using the provided wallet token.
     *
     * @param hWalletToken The wallet token to be used for binding the user, as a `String`.
     * @return boolean - Returns `true` if the user is successfully bound, otherwise `false`.
     * @throws Exception - If an error occurs during the wallet token verification or user binding process.
     */
    public boolean bindUser(String hWalletToken) throws WalletException {
        walletLogger.d("bindUser: + " + hWalletToken);
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.PERSONALIZE_AND_CONFIGLOCK,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.PERSONALIZE));
        return walletService.bindUser();

    }

    /**
     * Unbinds a user from the wallet using the provided wallet token.
     *
     * @param hWalletToken The wallet token to be used for unbinding the user.
     * @return boolean - Returns `true` if the user is successfully unbound, otherwise `false`.
     * @throws Exception - If an error occurs during the wallet token verification or user unbinding process.
     */
    public boolean unbindUser(String hWalletToken) throws WalletException {
        walletLogger.d("unbindUser: + " + hWalletToken);
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.DEPERSONALIZE));
        return walletService.unbindUser();

    }

    /**
     * Registers a wallet lock type with the provided wallet token, passcode, and lock status.
     *
     * @param hWalletToken The wallet token to be used for lock registration.
     * @param passCode     The passcode associated with the lock.
     * @param isLock       `true` if registering a lock, `false` if unregistering a lock.
     * @return boolean - Returns `true` if the lock is successfully registered or unregistered, otherwise `false`.
     * @throws Exception - If an error occurs during the wallet token verification, lock registration, or any related operation.
     */
    public boolean registerLock(String hWalletToken, String passCode, boolean isLock) throws WalletException, UtilityException, WalletCoreException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.PERSONALIZE_AND_CONFIGLOCK,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.CONFIGLOCK));
        return lockManager.registerLock(passCode, isLock);

    }

    /**
     * Authenticates a lock using the provided passcode.
     *
     * @param passCode The passcode used to authenticate the lock.
     * @throws Exception - If an error occurs during the lock authentication process, including utility or core issues.
     */
    public void authenticateLock(String passCode) throws UtilityException, WalletCoreException {
        lockManager.authenticateLock(passCode);
    }

    /**
     * Creates a DID (Decentralized Identifier) document for the holder using the provided wallet token.
     *
     * @param hWalletToken The wallet token to be used for creating the DID document.
     * @return DIDDocument - The created DID document.
     * @throws Exception - If an error occurs during the wallet token verification, DID document creation, or any related operation.
     */
    public DIDDocument createHolderDIDDoc(String hWalletToken) throws WalletException, UtilityException, WalletCoreException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.CREATE_DID));
        return walletService.createHolderDIDDoc();
    }

    /**
     * Creates a signed DID document using the provided owner DID document.
     *
     * @param ownerDIDDoc The DID document of the owner.
     * @return SignedDidDoc - The created signed DID document.
     * @throws Exception - Any error that occurs during the creation of the signed DID document.
     */
    public SignedDidDoc createSignedDIDDoc(DIDDocument ownerDIDDoc) throws WalletException, UtilityException, WalletCoreException {
        return walletService.createSignedDIDDoc(ownerDIDDoc);
    }

    /**
     * Retrieves a DID document based on the specified type.
     *
     * @param type The type of the DID document. (1: device key, 2: holder key)
     * @return DIDDocument - The requested DID document.
     * @throws Exception - Any error that occurs while retrieving the DID document.
     */
    public DIDDocument getDIDDocument(int type) throws UtilityException, WalletCoreException, WalletException {
        return walletCore.getDocument(type);
    }

    /**
     * Generates a key pair using the provided wallet token and passcode.
     *
     * @param hWalletToken The wallet token used for key pair generation.
     * @param passcode     The passcode required for key pair generation.
     * @throws Exception - Any error that occurs during wallet token verification or key pair generation.
     */
    public void generateKeyPair(String hWalletToken, String passcode) throws WalletException, UtilityException, WalletCoreException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.CREATE_DID));
        walletCore.generateKeyPair(passcode);
    }

    /**
     * Checks if the system is in a locked state.
     *
     * @return boolean - `true` if the system is locked, otherwise `false`.
     */
    public boolean isLock() {
        return lockManager.isLock();
    }

    /**
     * Retrieves signed wallet information.
     *
     * @return SignedWalletInfo - The requested signed wallet information.
     * @throws Exception - Any error that occurs while retrieving the signed wallet information.
     */
    public SignedWalletInfo getSignedWalletInfo() throws WalletException, UtilityException, WalletCoreException {
        return walletService.getSignedWalletInfo();
    }

    /**
     * Requests user registration with the given wallet token, transaction ID, server token, and signed DID document.
     *
     * @param hWalletToken The wallet token used for user registration.
     * @param tasUrl       The URL of the TAS
     * @param txId         The transaction ID.
     * @param serverToken  The server-issued token.
     * @param signedDIDDoc The signed DID document.
     * @return CompletableFuture<String> - A `CompletableFuture` representing the result of the user registration request.
     * @throws Exception - Any error that occurs during wallet token verification or user registration request.
     */
    public CompletableFuture<String> requestRegisterUser(String hWalletToken, String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) throws WalletException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.CREATE_DID,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.CREATE_DID_AND_ISSUE_VC));
        return walletService.requestRegisterUser(tasUrl, txId, serverToken, signedDIDDoc);
    }

    /**
     * Requests user restoration with the given wallet token, server token, signed DID authentication, and transaction ID.
     *
     * @param hWalletToken  The wallet token used for user restoration.
     * @param tasUrl        The URL of the TAS (Trusted Authority Service).
     * @param serverToken   The server-issued token.
     * @param signedDIDAuth The signed DID authentication document.
     * @param txId          The transaction ID.
     * @return CompletableFuture<String> - A `CompletableFuture` representing the result of the user restoration request.
     * @throws Exception - Any error that occurs during wallet token verification, user restoration request, or related processes.
     */
    public CompletableFuture<String> requestRestoreUser(String hWalletToken, String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.RESTORE_DID));
        return walletService.requestRestoreUser(tasUrl, serverToken, signedDIDAuth, txId);
    }

    /**
     * Requests user DID update with the given wallet token, server token, signed DID authentication, and transaction ID.
     *
     * @param hWalletToken  The wallet token used for user DID update.
     * @param tasUrl        The URL of the TAS (Trusted Authority Service).
     * @param serverToken   The server-issued token.
     * @param signedDIDAuth The signed DID authentication document.
     * @param txId          The transaction ID.
     * @return CompletableFuture<String> - A `CompletableFuture` representing the result of the user DID update request.
     * @throws Exception - Any error that occurs during wallet token verification, user DID update request, or related processes.
     */
    public CompletableFuture<String> requestUpdateUser(String hWalletToken, String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.UPDATE_DID));
        return walletService.requestUpdateUser(tasUrl, serverToken, signedDIDAuth, txId);
    }

    /**
     * Creates signed DID authentication using the provided authentication nonce and passcode.
     *
     * @param authNonce The authentication nonce.
     * @param passcode  The passcode.
     * @return DIDAuth - The signed DID authentication object.
     * @throws Exception - Any error that occurs during the DID authentication process.
     */
    public DIDAuth getSignedDIDAuth(String authNonce, String passcode) throws WalletException, UtilityException, WalletCoreException {
        return walletService.getSignedDIDAuth(authNonce, passcode);
    }

    /**
     * Requests to issue a Verifiable Credential (VC) using the provided wallet token, server token, reference ID, profile, signed DID authentication, and transaction ID.
     *
     * @param hWalletToken  The wallet token used for VC issuance.
     * @param serverToken   The server-issued token.
     * @param refId         The reference ID.
     * @param profile       The issuance profile.
     * @param signedDIDAuth The signed DID authentication object.
     * @param txId          The transaction ID.
     * @return CompletableFuture<String> - A `CompletableFuture` representing the result of the VC issuance request.
     * @throws Exception - Any error that occurs during wallet token verification or VC issuance request.
     */
    public CompletableFuture<String> requestIssueVc(String hWalletToken, String tasUrl, String apiGateWayUrl, String serverToken, String refId, IssueProfile profile, DIDAuth signedDIDAuth, String txId, CredentialPrimaryPublicKey credentialPrimaryPublicKey, CredentialOffer credentialOffer) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.ISSUE_VC,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.CREATE_DID_AND_ISSUE_VC));
        return walletService.requestIssueVc(tasUrl, apiGateWayUrl, serverToken, refId, profile, signedDIDAuth, txId);
    }

    /**
     * Requests to revoke a Verifiable Credential (VC) using the provided wallet token, server token, transaction ID, VC ID, issuer nonce, and passcode.
     *
     * @param hWalletToken The wallet token used for VC revocation.
     * @param serverToken  The server-issued token.
     * @param txId         The transaction ID.
     * @param vcId         The ID of the VC to be revoked.
     * @param issuerNonce  The issuer nonce.
     * @param passcode     The passcode.
     * @return CompletableFuture<String> - A `CompletableFuture` representing the result of the VC revocation request.
     * @throws Exception - Any error that occurs during wallet token verification or VC revocation request.
     */
    public CompletableFuture<String> requestRevokeVc(String hWalletToken, String tasUrl, String serverToken, String txId, String vcId, String issuerNonce, String passcode, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.REMOVE_VC));
        return walletService.requestRevokeVc(tasUrl, serverToken, txId, vcId, issuerNonce, passcode, authType);
    }

    /**
     * Retrieves all Verifiable Credentials (VCs) associated with the provided wallet token.
     *
     * @param hWalletToken The wallet token used to retrieve the VC list.
     * @return List<VerifiableCredential> - A list of all VCs.
     * @throws Exception - Any error that occurs during wallet token verification or VC retrieval.
     */
    public List<VerifiableCredential> getAllCredentials(String hWalletToken) throws WalletException, UtilityException, WalletCoreException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC_AND_PRESENT_VP));
        return walletCore.getAllCredentials();
    }

    /**
     * Retrieves specific Verifiable Credentials (VCs) based on the provided identifiers.
     *
     * @param hWalletToken The wallet token used to retrieve the VCs.
     * @param identifiers  A list of identifiers for the VCs to retrieve.
     * @return List<VerifiableCredential> - A list of requested VCs.
     * @throws Exception - Any error that occurs during wallet token verification or VC retrieval.
     */
    public List<VerifiableCredential> getCredentials(String hWalletToken, List<String> identifiers) throws WalletException, UtilityException, WalletCoreException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.DETAIL_VC));
        return walletCore.getCredentials(identifiers);
    }

    /**
     * Deletes a Verifiable Credential (VC) using the provided wallet token and VC ID.
     *
     * @param hWalletToken The wallet token used for VC deletion.
     * @param vcId         The ID of the VC to be deleted.
     * @throws Exception - Any error that occurs during wallet token verification or VC deletion.
     */
    public void deleteCredentials(String hWalletToken, String vcId) throws WalletException, UtilityException, WalletCoreException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.REMOVE_VC));
        walletCore.deleteCredentials(List.of(vcId));
    }

    /**
     * Creates an encrypted Verifiable Presentation (VP) using the provided wallet token, VC ID, claim codes, end-to-end request object, passcode, nonce, and authentication type.
     *
     * @param hWalletToken The wallet token used for VP creation.
     * @param vcId         The ID of the VC.
     * @param claimCode    A list of claim codes to be included in the VP.
     * @param reqE2e       The end-to-end request object.
     * @param passcode     The passcode used for VP creation.
     * @param nonce        The nonce used for VP creation.
     * @param authType     The authentication type.
     * @return ReturnEncVP - The created encrypted VP object.
     * @throws Exception - Any error that occurs during wallet token verification or VP creation.
     */
    public ReturnEncVP createEncVp(String hWalletToken, String vcId, List<String> claimCode, ReqE2e reqE2e, String passcode, String nonce, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws WalletException, UtilityException, WalletCoreException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.PRESENT_VP,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC_AND_PRESENT_VP));
        return walletService.createEncVp(vcId, claimCode, reqE2e, passcode, nonce, authType);
    }

    /**
     * Registers a biometric key for signing.
     *
     * @param ctx The context in which the biometric key will be registered.
     */
    public void registerBioKey(Context ctx) throws WalletException {
        walletCore.setBioPromptListener(new BioPromptHelper.BioPromptInterface() {
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
        walletCore.registerBioKey(ctx);
    }

    /**
     * Authenticates a biometric key for signing.
     *
     * @param fragment The fragment used for biometric authentication.
     * @param ctx      The context used for biometric authentication.
     * @throws Exception - Any error that occurs during biometric authentication.
     */
    public void authenticateBioKey(Fragment fragment, Context ctx) throws WalletCoreException, WalletException {
        walletCore.setBioPromptListener(new BioPromptHelper.BioPromptInterface() {
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
        walletCore.authenticateBioKey(fragment, ctx);

    }

    /**
     * Adds proofs to a document using the provided document, key IDs, DID, type, passcode, and DID authentication status.
     *
     * @param document  The document to which proofs will be added.
     * @param keyIds    The list of key IDs for the proofs.
     * @param did       The DID.
     * @param type      The DID document type.
     * @param passcode  The passcode.
     * @param isDIDAuth Indicates if DID authentication is required.
     * @return ProofContainer - The document with added proofs.
     * @throws Exception - Any error that occurs during proof addition to the document.
     */
    public ProofContainer addProofsToDocument(ProofContainer document, List<String> keyIds, String did, int type, String passcode, boolean isDIDAuth) throws WalletException, UtilityException, WalletCoreException {
        return walletService.addProofsToDocument(document, keyIds, did, type, passcode, isDIDAuth);
    }

    /**
     * Checks if a biometric key is saved.
     *
     * @return boolean - `true` if a biometric key is saved, otherwise `false`.
     * @throws Exception - Any error that occurs during the check for a saved biometric key.
     */
    public boolean isSavedKey(String id) throws UtilityException, WalletCoreException, WalletException {
        return walletCore.isSavedKey(id);
    }

    /**
     * Changes the Signing PIN for a given ID.
     *
     * @param keyId  The identifier of the key to be changed.
     * @param oldPin The current PIN.
     * @param newPin The new PIN.
     * @throws Exception Throws an exception if parameter validation fails or if an error occurs during encryption/decryption.
     */
    public void changePin(String keyId, String oldPin, String newPin) throws UtilityException, WalletCoreException {
        walletCore.changePin(keyId, oldPin, newPin);
    }

    /**
     * Changes the Unlock PIN.
     *
     * @param oldPin The current PIN.
     * @param newPin The new PIN.
     * @throws Exception Throws an exception if parameter validation fails or if an error occurs during encryption/decryption.
     */
    public void changeLock(String oldPin, String newPin) throws UtilityException, WalletCoreException, WalletException {
        lockManager.changeLock(oldPin, newPin);
    }


    /**
     * Creates a Zero-Knowledge Proof (ZKP) referent builder using the provided custom referents.
     *
     * @param customReferents A list of user-defined referents to be included in the ZKP generation.
     * @return ReferentInfo An object used to build ZKP referents for credential proof creation.
     * @throws WalletCoreException if an error occurs in wallet core operations.
     * @throws UtilityException if a utility-related error occurs during the referent creation process.
     */
    public ReferentInfo createZkpReferent(List<UserReferent> customReferents) throws WalletCoreException, UtilityException {
        return walletCore.createZkpReferent(customReferents);
    }

    /**
     * Creates a Zero-Knowledge Proof (ZKP) based on the given proof request and parameters.
     *
     * @param proofRequestProfileVo The proof request containing the verifier's requested attributes and predicates.
     * @param proofParams A list of proof parameters, including credentials and referents required for proof generation.
     * @param selfAttributes A map of self-attested attributes provided by the prover, not backed by credentials.
     * @return Proof An object used to build and generate the ZKP for presentation.
     * @throws WalletCoreException if an error occurs within the wallet core during proof creation.
     * @throws UtilityException if a utility or cryptographic error occurs during the process.
     */
    public P310ZkpRequestVo createZkpProof(String hWalletToken, P310ZkpResponseVo proofRequestProfileVo,
                                        List<ProofParam> proofParams, Map<String, String> selfAttributes) throws WalletCoreException, UtilityException, WalletException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.PRESENT_VP,
                WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC_AND_PRESENT_VP));
        return walletService.createZkpProof(proofRequestProfileVo, proofParams, selfAttributes);
    }

    /**
     * Searches for credentials in the wallet that satisfy the given Zero-Knowledge Proof (ZKP) proof request.
     * {"credDef":"meyJpZCI6ImRpZDpvbW46TmNZeGlEWGtwWWk2b3Y1RmNZRGkxZTozOkNMOmRpZDpvbW46TmNZeGlEWGtwWWk2b3Y1RmNZRGkxZToyOm1kbDoxLjA6VGFnMSIsInNjaGVtYUlkIjoiZGlkOm9tbjpOY1l4aURYa3BZaTZvdjVGY1lEaTFlOjI6bWRsOjEuMCIsInZlciI6IjEuMCIsInR5cGUiOiJDTCIsInZhbHVlIjp7InByaW1hcnkiOnsibiI6IjU5ODYwNTk3NDE1NzMyNTk0NTg2MzIyOTk2NzgwMTAxMDcyNDI2NzQyMDgxOTcxMTM5MjEyMTEwMTU4MDUxODk4MDQzNjI1NzExNTU2NjY3NjY0ODQzNDA3ODI5Njc1MjEyNzkxODAxODE0ODE1NzkxMjEyMDk1MzI3MTg0OTM5MTUxNzcwMTIyMzYzMDE0NTQ3MDc3MzUwNzYzODA4MTkyMjg0MjIyOTY1Nzk2MDE2OTkwMDYzNTExNzY0NTY2NzM5MTcyNzYyODg5NDMxMzE4NDUxODk0OTg0ODUwNzk0MTk5MjAwMzU4NTU0NzE4MTE2NDY5ODE1NjgxMzIwNzg5MjkyNTYwOTI4MDE3MzYzODI2MzI0ODU4OTQxOTU5MDUwNzE5NTIwMDcwOTU1OTk4NDY3ODI4MTQzNTQwOTQxNjA0NjA5MzY0NTk1OTI0MjkwNjAyMTU1MDg1MTg0NTg0MTkwOTc3NzUwNjI2MTExNTY5Nzk1MzMyMjAyNzc3MjQ2NzM0NDgxNjU4NDU0NDEzNzY3NjkwMzQ2MDY2NTM5ODI2Nzc5ODgyMDA0NDMwNDQ5MDQ5MTM4MDY3MzgxOTIyNDYwOTg3MjM2MDY1Nzg3MDE0NDU1NDY5ODQ3MzA5MjAwNDQxMzM5NTQzODU5ODE4OTQ0NDU3MTEyMDM1MDg0NjE3Nzg3MDY4NzQyMDQwMjg0OTk0MTU2MzAyMjcyNDgwNzEwMTQ2ODMwNzkzOTI1MjgyNDM4MDE1ODQ5NzY0ODc0NTU1ODk5MzA3NDA4ODMwOTE0NTY1Mzg2ODk5MjU0NjE3MDA2NTUxIiwieiI6IjUzOTQ4NTcyNDQyMzYyMDM0NTQ4MTQzMDc4MDgzODMxNjM0MzY3NDQ5Mzk0MTAzNjc0MDA3MTc1Nzg5MjQyNjE4MDgyNzg0MjUwMDIxNDk0Njc2NDY2OTUwMTk1NjM2NjcyODgzODc4OTM4MzU4MzQ1Mjg5MTI0MTM4Mzk2MTAzMjg3ODA4NDQyMzAxMzc5NTM4MDMzNzk4MzE1MjI4NjM5NjM3MjIyNzE2MzgxNjg3ODI5MzI4ODM4NzAzNjA3NTU4NDk1MTkwMDU1MDYxMTczNDk1MjQxNjk4NDE2Njc3OTkxMjc2NzQ0NjYxMjM4NTE0MzY0Nzc1NTU5Mjg3MDMwMDUyMzIwNzU5OTIzNDYyMjA3NTUxNDg5ODUzNzg1NjIzMTE3OTQ0MDM1MTc2MjkxMjQ4MjU2OTI3NDUwODAwMDI2NzU5NDE2Nzk5MzQ4MjU4NjA2NDQ1MDEyMjc5MDI4NTE1MTYwMDgzMDgwODgyNTMyMjcxMDMxOTU4OTMwNTM4MDY0MTg5MDIzNDk4ODAzODExNTQ3ODEyNDgwMjAyOTE5MTg0NzQ5NjUyNjgxNTc1NTU2NDk3NTY0MDc0NDkwMzgxMTM2MTM2MDc0MjY2NDMyNTE1NzgyNTg5NjQxOTMzMzU0MTk2MDY1NjE5MzAwMDY0MTEyNDM5NTA5NzQwMzk5MDg2NjY0NzUyOTYxMDQ1MzA2NDczNjAxNjAyNjc3NDQ2NTkzMzk0OTE4MTk5NjEwNTA0Mjc4Njg2NDA0MjE4NjAzNjI2MTQ5MTAwNDkyODEwNTA5OTk0NTI5ODY5ODM0MjAxOTkzIiwicyI6IjE5NjY2Nzg2NjQ5MzYyODUxODQxOTQ1MTYxMzM1NDA1OTMzMjkxOTc1MTA4ODUxMTczMzcyMzY3NTAzNzgxMzg2NTg4Mzg1OTc1ODYzNTA3ODQ5Mzk4NTA2NTM3NTA1MzMxNjQzODc5NzM4MTM3NzE3ODkyNjMyMDA1MjkwOTU5MzA1MDgxNTA5OTQ4NDIyNjAyMzM5MDE1NTM1NzY5MzE4NzY3MzM0MzIyODkwOTE1MTM4OTE0NjQ3NTc2ODUyMjg5NTgwMzcxMDY5NTUyODYzODA4NTE5MDMzMjk5NDI1OTQ4Njg1OTYzOTg0NDM0Mzc2MzUwMTM3Mzc5MjM1Njc2OTgwOTA0NzU0NDY1NTY1NDg4NjY5Mzg4ODI3NTcyNDIzNTM3NzEyNTU0NzczNjQ4MDIyOTg2OTY2NDI1ODk3ODg1NDA4NzcwMjI0ODAxMzQ0NTc4MTk4NjE2MjkxMDQ0MTU1MTEwOTE0ODUzMzAxNTU3NTM0NzI1OTE0NTczMDc2Nzg4NzI5NTA2MjUzODUyMjA3MTU4NjQ5MzgzMDIxMzkzODYyNDg0NDI3NTQ3OTU0MTA5Mjk3MTkwMzg3Mzc2Nzg4NDQ0OTA1MDU4NzA5Mjg3NDc5ODM5OTE1Nzc2ODI3NzExMDI4NzE5MTQ0MDIxMDk5OTE0MTQ5NzY2ODc4MDMyNjU0NDcwMDczNjU5MTE2NzI0MDI2NjgxMzU2MjMwMzEyOTk3MTQ3MTIzNDM3NzMzOTcxNTY5MjMzOTEzNDYxNDExNzE1MzUyODMzOTQwNTA1NTQyNzkxNTY1OTQ4MDU2MjAyMTciLCJyIjp7Ik1ETE5TLnprcHNleCI6IjgxNjUyODI1MTAzMjU4MDkwOTczOTI1MTk0NzkzNjk4Mzc1NjA1NTcyMjE2MTYyODA2NzY1ODAyNDMxODc5NzQ3MzcyMDQ0ODg2NTExNTA0NDg0Nzg4ODA2MDMyNDk5OTM4OTIyMjc5MTkwMTA5ODkwMTgxMjA2NzYxMzk3OTQ1NzU4ODMwMjU3NjA5NjE5NjY2ODAzNTU3MzE4MTkxMzM4MTk2NTAyMTAyMzQ4OTQwMDA0NTg3Njk4MTM3MjE5Nzk3Njc5Nzc0NTY1NzAxNDAyMDE3NTA1NDc2OTk3MzE3ODQ5MjQ1MDk3OTQ0MDIwMDU4MTExNjE1NDU0ODEzNTgwNDkyMDQ5NDMxMjgzMzkwNTY5NjgyMDg3ODYzMzY5MzQyNDIwMzE0NDczOTIwMjc1MjA0MDY5MjM0MzQ3NTc3Mzc1NjM4MDk3Mjg5ODA3NjE3MTQ1NDcyMDE2MDYwNTQ1NTU5NTk5NjQ1NDMzNzk2MjI1MjY5ODQwNTk5NTA4NzY4OTQ4MTIxMzM2MTI2NTA0NDg1MTIwMDIwOTYxOTQ0MzEzNDg3Mjk3MjIzNjg1MDc2NjM4NjQyODA5NTI5MDA0MDA0MTk1MDUwMjk2MjIyOTEzMzAwNjk5NDI2MzM3MzE0ODQ5OTQwOTMwNDM2ODg2NjY0MTgxNzQ1ODQ1NTcxMDMwNzIyNzAwNjA2OTExMDIzMTI0NDYyNDAyNTI2MzQxNjA3NDYxMzI0NTMxNjMyNDU3ODE2MTk0ODQ1MTExMDEwMzE1NTU4NTcwNjQwNzcyNzg2NzAxMzUyNTYwODUwMTk3ODQ2OTgiLCJNRExOUy56a3BiaXJ0aCI6IjI4MTIxOTYwMzM5MTY3Mzg4MTk5MjA5NDA2MTgxNjc5Nzg5NjM2NzAxNTUzODA1NDMyOTQ4MjI4MzQ4NjkyMDAwMjg0Nzg1NzkxMzI0MDI0OTgwNjU2NDI2MjQ1NDUxNDMxNjYxODg5MzQwMDkzNjY1MjIzMDE2NzM0MTg4MTMwMDg0MTAyMzk4ODgwNzc0NDM5NTU3NzY0MDIwMzA4NDI4Mzg3Njk4Nzg5ODg3NzM3NzQ4OTc2MTMxNjI2NjQ0MDY3NTE2ODk2MjM2NzYyNDg4MTkwNDA3MDgyOTMxMTc0MTQ1NTkwNTIyNjUyOTQ2MTM1MDQwNjU3NDEwMjY3ODgwNTMyMzYzNTMxNzkyMTEwNjA5ODMyMTEwMDI4OTQ5NjQ1NDQ4Njg1NTYwMTc0MTM0NjY
     * @param proofRequest The proof request specifying the required attributes and conditions for the ZKP presentation.
     * @return AvailableReferent An object used to retrieve and manage matching credentials for the proof request.
     * @throws WalletCoreException if an error occurs while accessing the wallet or retrieving credentials.
     * @throws UtilityException if a utility or processing error occurs during the search.
     */
    public AvailableReferent searchZkpCredentials(String hWalletToken, ProofRequest proofRequest) throws WalletCoreException, UtilityException, WalletException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC));
        return walletCore.searchZkpCredentials(proofRequest);
    }

    /**
     * Retrieves all Zero-Knowledge Proof (ZKP) credentials stored in the wallet.
     *
     * @return ArrayList<CredentialInfo> A list of all credentials available for ZKP-based operations.
     * @throws WalletCoreException if an error occurs while accessing the wallet or loading credentials.
     * @throws UtilityException if a utility or processing error occurs during credential retrieval.
     */
    public ArrayList<Credential> getAllZkpCredentials(String hWalletToken) throws WalletCoreException, UtilityException, WalletException {
        this.walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC_AND_PRESENT_VP));
        return walletCore.getAllZkpCredentials();
    }

    /**
     * Deletes all Zero-Knowledge Proof (ZKP) credentials stored in the wallet.
     *
     * @throws WalletCoreException if an error occurs while accessing or modifying the wallet data.
     * @throws UtilityException if a utility or processing error occurs during the deletion process.
     */
    public void deleteAllZkpCredentials(String hWalletToken) throws WalletCoreException, UtilityException, WalletException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.REMOVE_VC));
        walletCore.deleteAllZkpCredentials();
    }


    public boolean isAnyZkpCredentialsSaved() throws WalletCoreException, UtilityException, WalletException {
        return walletCore.isAnyZkpCredentialsSaved();
    }

    public List<Credential> getZkpCredentials(String hWalletToken, List<String> identifiers) throws WalletCoreException, UtilityException, WalletException {
        walletToken.verifyWalletToken(hWalletToken, List.of(WalletTokenPurpose.WALLET_TOKEN_PURPOSE.LIST_VC, WalletTokenPurpose.WALLET_TOKEN_PURPOSE.DETAIL_VC));
        return walletCore.getZkpCredentials(identifiers);
    }

}