package org.omnione.did.sdk.core.api;

import android.content.Context;

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.common.enums.VerifyAuthType;
import org.omnione.did.sdk.datamodel.common.enums.WalletTokenPurpose;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.did.SignedDidDoc;
import org.omnione.did.sdk.datamodel.profile.IssueProfile;
import org.omnione.did.sdk.datamodel.profile.ProofRequestProfile;
import org.omnione.did.sdk.datamodel.profile.ReqE2e;
import org.omnione.did.sdk.datamodel.protocol.P311RequestVo;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.datamodel.token.SignedWalletInfo;
import org.omnione.did.sdk.datamodel.token.WalletTokenData;
import org.omnione.did.sdk.datamodel.token.WalletTokenSeed;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;
import org.omnione.did.sdk.datamodel.vc.issue.ReturnEncVP;
import org.omnione.did.sdk.datamodel.zkp.AvailableReferent;
import org.omnione.did.sdk.datamodel.zkp.Credential;
import org.omnione.did.sdk.datamodel.zkp.ProofParam;
import org.omnione.did.sdk.datamodel.zkp.ProofRequest;
import org.omnione.did.sdk.datamodel.zkp.ReferentInfo;
import org.omnione.did.sdk.datamodel.zkp.UserReferent;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface IWalletApi {
    interface IWalletService {
        boolean isExistWallet();
        boolean createWallet(String walletUrl, String tasUrl) throws WalletException, WalletCoreException, UtilityException, ExecutionException, InterruptedException;
        void deleteWallet(boolean deleteAll) throws WalletCoreException;
        WalletTokenSeed createWalletTokenSeed(WalletTokenPurpose.WALLET_TOKEN_PURPOSE purpose, String pkgName, String userId) throws WalletCoreException, UtilityException, WalletException;
        String createNonceForWalletToken(String apiGateWayUrl, WalletTokenData walletTokenData) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException;
        boolean bindUser(String hWalletToken) throws WalletException;
        boolean unbindUser(String hWalletToken) throws WalletException;
        CompletableFuture<String> requestRegisterUser(String hWalletToken, String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) throws WalletException;
        SignedWalletInfo getSignedWalletInfo() throws WalletException, UtilityException, WalletCoreException;
    }

    interface IDIDKeyService {
        DIDDocument createHolderDIDDoc(String hWalletToken) throws WalletException, UtilityException, WalletCoreException;
        SignedDidDoc createSignedDIDDoc(DIDDocument ownerDIDDoc) throws WalletException, UtilityException, WalletCoreException;
        DIDDocument getDIDDocument(int type) throws UtilityException, WalletCoreException, WalletException;
        void generateKeyPair(String hWalletToken, String passcode) throws WalletException, UtilityException, WalletCoreException;
        DIDAuth getSignedDIDAuth(String authNonce, String passcode) throws WalletException, UtilityException, WalletCoreException;
        DIDDocument updateHolderDIDDoc(String hWalletToken) throws WalletException, UtilityException, WalletCoreException;
        void saveDocument() throws WalletException, WalletCoreException, UtilityException;
        void deleteKey(String hWalletToken, List<String> keyIds) throws WalletCoreException, UtilityException, WalletException;
        CompletableFuture<String> requestUpdateUser(String hWalletToken, String tasUrl, String serverToken, DIDAuth signedDIDAuth, SignedDidDoc signedDIDDoc, String txId) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException;
        CompletableFuture<String> requestRestoreUser(String hWalletToken, String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws WalletException, ExecutionException, InterruptedException;
    }

    interface ICredentialService {
        CompletableFuture<String> requestIssueVc(String hWalletToken, String tasUrl, String apiGateWayUrl, String serverToken, String refId, IssueProfile profile, DIDAuth signedDIDAuth, String txId) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException;
        CompletableFuture<String> requestRevokeVc(String hWalletToken, String tasUrl, String serverToken, String txId, String vcId, String issuerNonce, String passcode, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws WalletException, UtilityException, WalletCoreException, ExecutionException, InterruptedException;
        List<VerifiableCredential> getAllCredentials(String hWalletToken) throws WalletException, UtilityException, WalletCoreException;
        List<VerifiableCredential> getCredentials(String hWalletToken, List<String> identifiers) throws WalletException, UtilityException, WalletCoreException;
        void deleteCredentials(String hWalletToken, String vcId) throws WalletException, UtilityException, WalletCoreException;
        ReturnEncVP createEncVp(String hWalletToken, String vcId, List<String> claimCode, ReqE2e reqE2e, String passcode, String nonce, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws WalletException, UtilityException, WalletCoreException;
        ProofContainer addProofsToDocument(ProofContainer document, List<String> keyIds, String did, int type, String passcode, boolean isDIDAuth) throws WalletException, UtilityException, WalletCoreException;
        boolean isAnyCredentialsSaved() throws WalletException;
    }

    interface IZKPService {
        ReferentInfo createZkpReferent(List<UserReferent> customReferents) throws WalletCoreException, UtilityException;
        P311RequestVo createEncZkpProof(String hWalletToken, ProofRequestProfile proofRequestProfile, List<ProofParam> proofParams, Map<String, String> selfAttributes, String txId) throws WalletCoreException, UtilityException, WalletException;
        AvailableReferent searchZkpCredentials(String hWalletToken, ProofRequest proofRequest) throws WalletCoreException, UtilityException, WalletException;
        ArrayList<Credential> getAllZkpCredentials(String hWalletToken) throws WalletCoreException, UtilityException, WalletException;
        boolean isAnyZkpCredentialsSaved() throws WalletCoreException, UtilityException, WalletException;
        List<Credential> getZkpCredentials(String hWalletToken, List<String> identifiers) throws WalletCoreException, UtilityException, WalletException;
    }

    interface ISecurityAuthService {
        boolean registerLock(String hWalletToken, String passCode, boolean isLock) throws WalletException, UtilityException, WalletCoreException;
        void authenticateLock(String passCode) throws UtilityException, WalletCoreException;
        boolean isLock();
        void registerBioKey(Context ctx) throws WalletException;
        void authenticateBioKey(Context ctx) throws WalletCoreException, WalletException;
        void changePin(String keyId, String oldPin, String newPin) throws UtilityException, WalletCoreException;
        void changeLock(String oldPassCode, String newPassCode) throws UtilityException, WalletCoreException, WalletException;
        void authenticatePin(String id, byte[] pin) throws WalletCoreException, UtilityException;
    }
}
