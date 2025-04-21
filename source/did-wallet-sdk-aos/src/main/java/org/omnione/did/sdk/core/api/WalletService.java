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

import org.omnione.did.sdk.datamodel.protocol.P210ResponseVo;
import org.omnione.did.sdk.datamodel.protocol.P220ResponseVo;
import org.omnione.did.sdk.datamodel.did.DidDocVo;
import org.omnione.did.sdk.datamodel.security.AccE2e;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.datamodel.vc.issue.ReqRevokeVC;
import org.omnione.did.sdk.datamodel.vc.issue.ReqVC;
import org.omnione.did.sdk.datamodel.token.SignedWalletInfo;
import org.omnione.did.sdk.datamodel.vc.issue.ReturnEncVP;
import org.omnione.did.sdk.datamodel.common.enums.RoleType;
import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.datamodel.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.datamodel.common.enums.VerifyAuthType;
import org.omnione.did.sdk.datamodel.did.VerificationMethod;
import org.omnione.did.sdk.datamodel.profile.IssueProfile;
import org.omnione.did.sdk.datamodel.profile.ReqE2e;
import org.omnione.did.sdk.datamodel.util.MessageUtil;
import org.omnione.did.sdk.datamodel.protocol.P131RequestVo;
import org.omnione.did.sdk.datamodel.did.AttestedDidDoc;
import org.omnione.did.sdk.datamodel.did.SignedDidDoc;
import org.omnione.did.sdk.datamodel.token.Wallet;
import org.omnione.did.sdk.datamodel.common.Proof;
import org.omnione.did.sdk.datamodel.common.ProofContainer;
import org.omnione.did.sdk.datamodel.common.enums.ProofPurpose;
import org.omnione.did.sdk.datamodel.common.enums.ProofType;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.vc.Claim;
import org.omnione.did.sdk.datamodel.vc.VCProof;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;
import org.omnione.did.sdk.datamodel.vcschema.VCSchema;
import org.omnione.did.sdk.datamodel.vp.VerifiablePresentation;
import org.omnione.did.sdk.datamodel.wallet.AllowedCAList;
import org.omnione.did.sdk.utility.CryptoUtils;
import org.omnione.did.sdk.utility.DataModels.CipherInfo;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DataModels.EcKeyPair;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.wallet.WalletServiceInterface;
import org.omnione.did.sdk.wallet.walletservice.config.Constants;
import org.omnione.did.sdk.wallet.walletservice.db.CaPkg;
import org.omnione.did.sdk.wallet.walletservice.db.CaPkgDao;
import org.omnione.did.sdk.wallet.walletservice.db.DBManager;
import org.omnione.did.sdk.wallet.walletservice.db.Preference;
import org.omnione.did.sdk.wallet.walletservice.db.TokenDao;
import org.omnione.did.sdk.wallet.walletservice.db.User;
import org.omnione.did.sdk.wallet.walletservice.db.UserDao;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;
import org.omnione.did.sdk.wallet.walletservice.network.HttpUrlConnection;
import org.omnione.did.sdk.wallet.walletservice.network.protocol.IssueVc;
import org.omnione.did.sdk.wallet.walletservice.network.protocol.RegUser;
import org.omnione.did.sdk.wallet.walletservice.network.protocol.RestoreUser;
import org.omnione.did.sdk.wallet.walletservice.network.protocol.RevokeVc;
import org.omnione.did.sdk.wallet.walletservice.network.protocol.UpdateUser;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;
import org.omnione.did.sdk.core.bioprompthelper.BioPromptHelper;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.vcmanager.datamodel.ClaimInfo;
import org.omnione.did.sdk.core.vcmanager.datamodel.PresentationInfo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class WalletService implements WalletServiceInterface {
    private Context context;
    BioPromptHelper bioPromptHelper;
    WalletCore walletCore;
    WalletLogger walletLogger;

    public void setBioPromptListener(BioPromptHelper.BioPromptInterface bioPromptInterface){
        this.bioPromptInterface = bioPromptInterface;
    }
    public interface BioPromptInterface{
        void onSuccess(String result);
        void onFail(String result);
    }
    private BioPromptHelper.BioPromptInterface bioPromptInterface;

    public WalletService(){}
    public WalletService(Context context, WalletCore walletCore){
        this.context = context;
        this.walletCore = walletCore;
        bioPromptHelper = new BioPromptHelper(context);
        walletLogger = WalletLogger.getInstance();
    }

    @Override
    public void fetchCaInfo(String tasUrl) throws WalletException, ExecutionException, InterruptedException{
        //String pkgName = "org.omnione.did.ca";
        if(tasUrl.isEmpty())
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL, "tasUrl");
        String pkgNameList = WalletUtil.getAllowedCa(tasUrl).get();
        AllowedCAList allowedCAList = MessageUtil.deserialize(pkgNameList, AllowedCAList.class);
        if(allowedCAList.getCount() == 0)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_CREATE_WALLET_FAIL);
        String[] allowedCAPkgList = new String[allowedCAList.getCount()];
        for(int i = 0; i < allowedCAList.getCount(); i++) {
            allowedCAPkgList[i] = allowedCAList.getItems().get(i);
        }
        walletLogger.d("allowed CA pkgName : " + allowedCAPkgList[0]);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager walletDB = DBManager.getDatabases(context);
                CaPkgDao caPkgDao = walletDB.caPkgDao();
                caPkgDao.insertAll(new CaPkg(allowedCAPkgList[0]));
            }
        }).start();
    }
    @Override
    public void createDeviceDocument(String walletUrl, String tasUrl) throws WalletException, WalletCoreException, UtilityException, ExecutionException, InterruptedException {
        if(walletUrl.isEmpty())
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL, "walletUrl");
        if(tasUrl.isEmpty())
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL, "tasUrl");
        DIDDocument deviceDIDDoc = walletCore.createDeviceDIDDoc();
        String did = deviceDIDDoc.getId();
        List<String> keyIds = List.of(Constants.KEY_ID_ASSERT, Constants.KEY_ID_AUTH);
        WalletApi.isLock = false;
        DIDDocument ownerDIDDoc = (DIDDocument) addProofsToDocument(deviceDIDDoc, keyIds, did, Constants.DID_DOC_TYPE_DEVICE, "", false);
        String attDIDDoc = getWalletAttestedDIDDoc(walletUrl, ownerDIDDoc.toJson()).get();
        AttestedDidDoc attestedDIDDoc = MessageUtil.deserialize(attDIDDoc, AttestedDidDoc.class);
        Preference.saveWalletId(context, attestedDIDDoc.getWalletId());
        P131RequestVo requestVo = new P131RequestVo(WalletUtil.createMessageId());
        requestVo.setAttestedDidDoc(attestedDIDDoc);
        if (requestRegisterWallet(tasUrl, requestVo.toJson()).get() != null) {
            walletCore.saveDocument(Constants.DID_DOC_TYPE_DEVICE);
        }
        WalletApi.isLock = true;
    }

    private CompletableFuture<String> getWalletAttestedDIDDoc(String walletUrl, String request){
        String api = "/wallet/api/v1/request-sign-wallet";
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(walletUrl + api, "POST", request);
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    private CompletableFuture<String> requestRegisterWallet(String tasUrl, String request) {
        String api = "/tas/api/v1/request-register-wallet";
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(tasUrl + api, "POST", request);
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    @Override
    public boolean bindUser(){
        DBManager walletDB = DBManager.getDatabases(context);
        TokenDao tokenDao = walletDB.tokenDao();
        String pii = tokenDao.getAll().get(0).pii;
        Preference.savePii(context, pii);
        UserDao userDao = walletDB.userDao();
        User user = new User(pii);
        userDao.insertAll(user);
        return true;
    }

    @Override
    public boolean unbindUser(){
        DBManager walletDB = DBManager.getDatabases(context);
        UserDao userDao = walletDB.userDao();
        userDao.deleteAll();
        return true;
    }
    @Override
    public void deleteWallet() throws WalletCoreException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager walletDB = DBManager.getDatabases(context);
                walletDB.caPkgDao().deleteAll();
                walletDB.userDao().deleteAll();
                walletDB.tokenDao().deleteAll();
            }
        }).start();
        walletCore.deleteWallet();
    }
    @Override
    public DIDDocument createHolderDIDDoc() throws UtilityException, WalletCoreException, WalletException {
        DIDDocument holderDIDDoc = walletCore.createHolderDIDDoc();
        walletCore.saveDocument(Constants.DID_DOC_TYPE_HOLDER);
        return holderDIDDoc;
    }
    @Override
    public SignedDidDoc createSignedDIDDoc(DIDDocument ownerDIDDoc) throws WalletException, WalletCoreException, UtilityException{
        if(ownerDIDDoc == null)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL, "ownerDIDDoc");
        SignedDidDoc signedDIDDoc = new SignedDidDoc();
        signedDIDDoc.setOwnerDidDoc(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, ownerDIDDoc.toJson().getBytes()));
        Wallet wallet = new Wallet();
        wallet.setDID(walletCore.getDocument(Constants.DID_DOC_TYPE_DEVICE).getId());
        wallet.setId(Preference.loadWalletId(context));
        signedDIDDoc.setWallet(wallet);
        signedDIDDoc.setNonce(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, CryptoUtils.generateNonce(16)));
        List<String> keyIds = List.of(Constants.KEY_ID_ASSERT);
        signedDIDDoc = (SignedDidDoc) addProofsToDocument(signedDIDDoc, keyIds, walletCore.getDocument(Constants.DID_DOC_TYPE_DEVICE).getId(), Constants.DID_DOC_TYPE_DEVICE, "", false);
        return signedDIDDoc;
    }
    @Override
    public SignedWalletInfo getSignedWalletInfo() throws WalletException, WalletCoreException, UtilityException {
        SignedWalletInfo signedWalletInfo = new SignedWalletInfo();
        Wallet wallet = new Wallet();
        String did = walletCore.getDocument(Constants.DID_DOC_TYPE_DEVICE).getId();
        wallet.setDID(did);
        wallet.setId(Preference.loadWalletId(context));
        signedWalletInfo.setWallet(wallet);
        signedWalletInfo.setNonce(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, CryptoUtils.generateNonce(16)));
        signedWalletInfo = (SignedWalletInfo) addProofsToDocument(signedWalletInfo, List.of("assert"), walletCore.getDocument(Constants.DID_DOC_TYPE_DEVICE).getId(), Constants.DID_DOC_TYPE_DEVICE, "", false);
        return signedWalletInfo;
    }

    @Override
    public CompletableFuture<String> requestRegisterUser(String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) {
        RegUser regUser = new RegUser(context);
        return regUser.registerUser(tasUrl, txId, serverToken, signedDIDDoc);
    }

    @Override
    public CompletableFuture<String> requestRestoreUser(String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws WalletException, ExecutionException, InterruptedException {
        RestoreUser restoreUser = new RestoreUser(context);
        String result = restoreUser.restoreUser(tasUrl, txId, serverToken, signedDIDAuth).get();
        if (result == null)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_RESTORE_USER_FAIL);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<String> requestUpdateUser(String tasUrl, String serverToken, DIDAuth signedDIDAuth, String txId) throws WalletException, ExecutionException, InterruptedException {
        UpdateUser updateUser = new UpdateUser(context);
        String result = updateUser.updateUser(tasUrl, txId, serverToken, signedDIDAuth).get();
        if (result == null)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_UPDATE_USER_FAIL);
        return CompletableFuture.completedFuture(result);
    }
    @Override
    public DIDAuth getSignedDIDAuth(String authNonce, String pin) throws WalletException, WalletCoreException, UtilityException {
        DIDDocument holderDIDDoc = walletCore.getDocument(Constants.DID_DOC_TYPE_HOLDER);
        DIDAuth didAuth = new DIDAuth();
        didAuth.setDID(holderDIDDoc.getId());
        didAuth.setAuthNonce(authNonce);
        DIDAuth signedDIDAuth = new DIDAuth();

        String pinStr = pin != null ? pin.toString() : null;

        if(pinStr != null)
            signedDIDAuth = (DIDAuth) addProofsToDocument(didAuth, List.of(Constants.KEY_ID_PIN), holderDIDDoc.getId(), Constants.DID_DOC_TYPE_HOLDER, pin, true);
        else
            signedDIDAuth = (DIDAuth) addProofsToDocument(didAuth, List.of(Constants.KEY_ID_BIO), holderDIDDoc.getId(), Constants.DID_DOC_TYPE_HOLDER, "", true);
        return signedDIDAuth;
    }
    @Override
    public CompletableFuture<String> requestIssueVc(String tasUrl, String apiGateWayUrl, String serverToken, String refId, IssueProfile profile, DIDAuth signedDIDAuth, String txId) throws WalletException, WalletCoreException, UtilityException, ExecutionException, InterruptedException {
        verifyCertVc(RoleType.ROLE_TYPE.ISSUER, profile.getProfile().issuer.getDID(), profile.getProfile().issuer.getCertVcRef(), apiGateWayUrl);

        // todo: profile 서명 검증

        String serverPublicKey = profile.getProfile().process.reqE2e.getPublicKey();
        EcKeyPair e2eKeyPair = CryptoUtils.generateECKeyPair(EcType.EC_TYPE.SECP256_R1);
        byte[] iv = CryptoUtils.generateNonce(16);
        byte[] clientPublicKey = MultibaseUtils.decode(e2eKeyPair.getPublicKey());
        AccE2e accE2e = new AccE2e();
        accE2e.setIv(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, iv));
        accE2e.setPublicKey(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, clientPublicKey));
        accE2e = (AccE2e) addProofsToDocument(accE2e, List.of(Constants.KEY_ID_KEY_AGREE), walletCore.getDocument(Constants.DID_DOC_TYPE_HOLDER).getId(), Constants.DID_DOC_TYPE_HOLDER, "", false);

        byte[] secretKey = CryptoUtils.generateSharedSecret(EcType.EC_TYPE.SECP256_R1, MultibaseUtils.decode(e2eKeyPair.getPrivateKey()), MultibaseUtils.decode(serverPublicKey));
        byte[] serverNonce = MultibaseUtils.decode(profile.getProfile().process.reqE2e.getNonce());
        byte[] e2eKey = mergeSharedSecretAndNonce(secretKey, serverNonce, SymmetricCipherType.SYMMETRIC_CIPHER_TYPE.AES256CBC);

        ReqVC reqVc = new ReqVC();
        reqVc.setRefId(refId);
        reqVc.setProfile(new ReqVC.Profile(profile.getId(), profile.getProfile().process.issuerNonce));

        String[] cipher = profile.getProfile().process.reqE2e.getCipher().getValue().split("-");
        CipherInfo info = new CipherInfo
                (CipherInfo.ENCRYPTION_TYPE.fromValue(cipher[0]),
                        CipherInfo.ENCRYPTION_MODE.fromValue(cipher[2]),
                        CipherInfo.SYMMETRIC_KEY_SIZE.fromValue(cipher[1]),
                        CipherInfo.SYMMETRIC_PADDING_TYPE.fromKey(profile.getProfile().process.reqE2e.getPadding().getValue()));
        byte[] encReqVc = CryptoUtils.encrypt(
                reqVc.toJson().getBytes(),
                info,
                e2eKey,
                iv
        );
        String encReqVcStr = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, encReqVc);

        IssueVc issueVc = new IssueVc(context);
        String result = issueVc.issueVc(tasUrl, txId, serverToken, signedDIDAuth, accE2e, encReqVcStr).get();
        if (result == null)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_ISSUE_CREDENTIAL_FAIL);
        String vc = decryptVc(result, profile.getProfile().process.reqE2e, e2eKeyPair, apiGateWayUrl);
        return CompletableFuture.completedFuture(saveVc(vc, apiGateWayUrl));

    }

    private String decryptVc(String e2e, ReqE2e reqE2e, EcKeyPair dhKeyPair, String apiGateWayUrl) throws UtilityException{
        String iv = MessageUtil.deserialize(e2e, P210ResponseVo.class).getE2e().getIv();
        String encVc = MessageUtil.deserialize(e2e, P210ResponseVo.class).getE2e().getEncVc();
        String[] cipher = reqE2e.getCipher().getValue().split("-");
        CipherInfo info = new CipherInfo
                (CipherInfo.ENCRYPTION_TYPE.fromValue(cipher[0]),
                        CipherInfo.ENCRYPTION_MODE.fromValue(cipher[2]),
                        CipherInfo.SYMMETRIC_KEY_SIZE.fromValue(cipher[1]),
                        CipherInfo.SYMMETRIC_PADDING_TYPE.fromKey(reqE2e.getPadding().getValue()));

        byte[] sessKey = createDhSecret(reqE2e, dhKeyPair);

        byte[] plain = CryptoUtils.decrypt(MultibaseUtils.decode(encVc), info, sessKey, MultibaseUtils.decode(iv));
        return new String(plain);
    }
    private String saveVc(String vcStr, String apiGateWayUrl) throws WalletCoreException, UtilityException, WalletException, ExecutionException, InterruptedException {
        List<String> removeIds = new ArrayList<>();
        VerifiableCredential vc = MessageUtil.deserialize(vcStr, VerifiableCredential.class);
        if(walletCore.isAnyCredentialsSaved()) {
            for (VerifiableCredential verifiableCredential : walletCore.getAllCredentials()) {
                if (vc.getCredentialSchema().getId().equals(verifiableCredential.getCredentialSchema().getId()))
                    removeIds.add(verifiableCredential.getId());
            }
            if(removeIds.size() > 0)
                walletCore.deleteCredentials(removeIds);
        }
        boolean result = verifyProof(vcStr, false, apiGateWayUrl);
        if(result)
            walletCore.addCredentials(vc);
        return vc.getId();
    }
    @Override
    public CompletableFuture<String> requestRevokeVc(String tasUrl, String serverToken, String txId, String vcId, String issuerNonce, String passcode, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws WalletException, WalletCoreException, UtilityException,  ExecutionException, InterruptedException {
        ReqRevokeVC reqRevokeVc = new ReqRevokeVC();
        reqRevokeVc.setVcId(vcId);
        reqRevokeVc.setIssuerNonce(issuerNonce);
        if(authType == VerifyAuthType.VERIFY_AUTH_TYPE.PIN
                || authType == VerifyAuthType.VERIFY_AUTH_TYPE.BIO
                || authType == VerifyAuthType.VERIFY_AUTH_TYPE.PIN_OR_BIO) {
            if(!passcode.isEmpty()) {
                reqRevokeVc = (ReqRevokeVC) addProofsToDocument(reqRevokeVc, List.of(Constants.KEY_ID_PIN), walletCore.getDocument(Constants.DID_DOC_TYPE_HOLDER).getId(), Constants.DID_DOC_TYPE_HOLDER, passcode, false);
            } else {
                reqRevokeVc = (ReqRevokeVC) addProofsToDocument(reqRevokeVc, List.of(Constants.KEY_ID_BIO), walletCore.getDocument(Constants.DID_DOC_TYPE_HOLDER).getId(), Constants.DID_DOC_TYPE_HOLDER, "", false);
            }
        }
        RevokeVc revokeVc = new RevokeVc(context);
        String result = revokeVc.revokeVc(tasUrl, txId, serverToken, reqRevokeVc).get();
        P220ResponseVo responseVo = MessageUtil.deserialize(result, P220ResponseVo.class);
        return CompletableFuture.completedFuture(result);
    }


    private byte[] createDhSecret(ReqE2e reqE2e, EcKeyPair dhKeyPair) throws UtilityException {
        byte[] privKey = MultibaseUtils.decode(dhKeyPair.getPrivateKey());
        String multibasePubKey = reqE2e.getPublicKey();
        byte[] serverPubKey = MultibaseUtils.decode(multibasePubKey);
        byte[] secretKey = CryptoUtils.generateSharedSecret(EcType.EC_TYPE.SECP256_R1, privKey, serverPubKey);
        byte[] serverNonce = MultibaseUtils.decode(reqE2e.getNonce());
        byte[] e2eKey = mergeSharedSecretAndNonce(secretKey, serverNonce, SymmetricCipherType.SYMMETRIC_CIPHER_TYPE.AES256CBC);
        return e2eKey;
    }

    @Override
    public ProofContainer addProofsToDocument(ProofContainer document, List<String> keyIds, String did, int type, String passcode, boolean isDIDAuth) throws WalletException, WalletCoreException, UtilityException {
        List<Proof> proofs = new ArrayList<>();
        for(String keyId : keyIds) {
            Proof proof = new Proof();
            switch (keyId) {
                case Constants.KEY_ID_ASSERT:
                case Constants.KEY_ID_PIN:
                case Constants.KEY_ID_BIO:
                    proof = createProof(did, ProofPurpose.PROOF_PURPOSE.assertionMethod, keyId);
                    if(isDIDAuth)
                        proof = createProof(did, ProofPurpose.PROOF_PURPOSE.authentication, keyId);
                    break;
                case Constants.KEY_ID_AUTH:
                    proof = createProof(did, ProofPurpose.PROOF_PURPOSE.authentication, keyId);
                    break;
                case Constants.KEY_ID_KEY_AGREE:
                    proof = createProof(did, ProofPurpose.PROOF_PURPOSE.keyAgreement, keyId);
                    break;
                default:
                    throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_CREATE_PROOF_FAIL);
            }
            document.setProof(proof);
            byte[] signature = null;
            if(keyId.equals(Constants.KEY_ID_PIN)) {
                signature = walletCore.sign(keyId, passcode.getBytes(), document.toJson().getBytes(), type);
            } else {
                signature = walletCore.sign(keyId, null, document.toJson().getBytes(), type);
            }
            if(signature == null)
                throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_CREATE_PROOF_FAIL);
            proof.setProofValue(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, signature));
            document.setProof(proof);
            if(keyIds.size() == 1){
                return document;
            }
            proofs.add(proof);
            document.setProof(null);
        }
        document.setProofs(proofs);
        return document;
    }

    private Proof createProof(String did, ProofPurpose.PROOF_PURPOSE proofPurpose, String keyId) {
        Proof proof = new Proof();
        proof.setCreated(WalletUtil.getDate());
        proof.setProofPurpose(proofPurpose);
        proof.setType(ProofType.PROOF_TYPE.secp256r1Signature2018);
        proof.setVerificationMethod(did + "?versionId=1#" + keyId);
        return proof;
    }

    @Override
    public ReturnEncVP createEncVp(String vcId, List<String> claimCode, ReqE2e reqE2e, String passcode, String nonce, VerifyAuthType.VERIFY_AUTH_TYPE authType) throws WalletException, WalletCoreException, UtilityException {
        String serverPublicKey = reqE2e.getPublicKey();
        EcKeyPair e2eKeyPair = CryptoUtils.generateECKeyPair(EcType.EC_TYPE.SECP256_R1);
        byte[] iv = CryptoUtils.generateNonce(16);
        byte[] clientPublicKey = MultibaseUtils.decode(e2eKeyPair.getPublicKey());
        AccE2e accE2e = new AccE2e();
        accE2e.setIv(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, iv));
        accE2e.setPublicKey(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, clientPublicKey));
        accE2e = (AccE2e) addProofsToDocument(accE2e, List.of(Constants.KEY_ID_KEY_AGREE), walletCore.getDocument(Constants.DID_DOC_TYPE_HOLDER).getId(), Constants.DID_DOC_TYPE_HOLDER, "", false);
        byte[] secretKey = CryptoUtils.generateSharedSecret(EcType.EC_TYPE.SECP256_R1, MultibaseUtils.decode(e2eKeyPair.getPrivateKey()), MultibaseUtils.decode(serverPublicKey));
        byte[] serverNonce = MultibaseUtils.decode(reqE2e.getNonce());
        byte[] e2eKey = mergeSharedSecretAndNonce(secretKey, serverNonce, SymmetricCipherType.SYMMETRIC_CIPHER_TYPE.AES256CBC);

        String[] cipher = reqE2e.getCipher().getValue().split("-");
        CipherInfo info = new CipherInfo
                (CipherInfo.ENCRYPTION_TYPE.fromValue(cipher[0]),
                        CipherInfo.ENCRYPTION_MODE.fromValue(cipher[2]),
                        CipherInfo.SYMMETRIC_KEY_SIZE.fromValue(cipher[1]),
                        CipherInfo.SYMMETRIC_PADDING_TYPE.fromKey(reqE2e.getPadding().getValue()));

        List<VerifiableCredential> vcList = walletCore.getAllCredentials();

        List<ClaimInfo> claimInfos = new ArrayList<>();
        ClaimInfo claimInfo = new ClaimInfo(
                vcId,
                claimCode
        );
        claimInfos.add(claimInfo);

        PresentationInfo presentationInfo = new PresentationInfo();
        presentationInfo.setHolder(walletCore.getDocument(Constants.DID_DOC_TYPE_HOLDER).getId()); //holder did
        presentationInfo.setValidFrom(WalletUtil.getDate());
        presentationInfo.setValidUntil(WalletUtil.createValidUntil(600));
        presentationInfo.setVerifierNonce(nonce);
        VerifiablePresentation vp = walletCore.makePresentation(claimInfos, presentationInfo);
        VerifiablePresentation signedVp = new VerifiablePresentation();
        if(authType == VerifyAuthType.VERIFY_AUTH_TYPE.PIN
                || authType == VerifyAuthType.VERIFY_AUTH_TYPE.BIO
                || authType == VerifyAuthType.VERIFY_AUTH_TYPE.PIN_OR_BIO) {
            if(!passcode.isEmpty()) {
                signedVp = (VerifiablePresentation) addProofsToDocument(vp, List.of(Constants.KEY_ID_PIN), walletCore.getDocument(Constants.DID_DOC_TYPE_HOLDER).getId(), Constants.DID_DOC_TYPE_HOLDER, passcode, false);
            } else {
                signedVp = (VerifiablePresentation) addProofsToDocument(vp, List.of(Constants.KEY_ID_BIO), walletCore.getDocument(Constants.DID_DOC_TYPE_HOLDER).getId(), Constants.DID_DOC_TYPE_HOLDER, "", false);
            }
        }
        byte[] encVp = CryptoUtils.encrypt(
                signedVp.toJson().getBytes(),
                info,
                e2eKey,
                iv
        );
        String encVpStr = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, encVp);
        ReturnEncVP returnEncVP = new ReturnEncVP();
        returnEncVP.setEncVp(encVpStr);
        returnEncVP.setAccE2e(accE2e);
        return returnEncVP;

    }

    // unlock bio key
    public void registerUnlockBioKey(Context ctx) {
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
            bioPromptHelper.registerBioKey(ctx, null);
    }

    public void authenticateUnlockBioKey(Fragment fragment, Context ctx) {
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
            bioPromptHelper.authenticateBioKey(fragment, ctx, null);
    }

    private byte[] mergeSharedSecretAndNonce(byte[] sharedSecret, byte[] nonce, SymmetricCipherType.SYMMETRIC_CIPHER_TYPE cipherType) throws UtilityException {
        byte[] clientMergedSharedSecret = null;
        int length = sharedSecret.length + nonce.length;
        byte[] digest = new byte[length];
        System.arraycopy(sharedSecret, 0, digest, 0, sharedSecret.length);
        System.arraycopy(nonce, 0, digest, sharedSecret.length, nonce.length);
        byte[] combinedResult = DigestUtils.getDigest(digest, DigestEnum.DIGEST_ENUM.SHA_256);

        if(cipherType.equals(SymmetricCipherType.SYMMETRIC_CIPHER_TYPE.AES128CBC) ||
                cipherType.equals(SymmetricCipherType.SYMMETRIC_CIPHER_TYPE.AES128ECB)) {
            clientMergedSharedSecret = new byte[16];
            System.arraycopy(combinedResult, 0, clientMergedSharedSecret, 0, 16);
        } else if (cipherType.equals(SymmetricCipherType.SYMMETRIC_CIPHER_TYPE.AES256CBC) ||
                cipherType.equals(SymmetricCipherType.SYMMETRIC_CIPHER_TYPE.AES256ECB)) {
            clientMergedSharedSecret = new byte[32];
            System.arraycopy(combinedResult, 0, clientMergedSharedSecret, 0, 32);
        }

        return clientMergedSharedSecret;
    }

    private boolean verifyProof(String vcStr, boolean isCertVc, String apiGateWayUrl) throws WalletCoreException, UtilityException, WalletException, ExecutionException, InterruptedException {
        VerifiableCredential vc = MessageUtil.deserialize(vcStr, VerifiableCredential.class);
        String did = vc.getIssuer().getId();
        if(isCertVc)
            did = vc.getCredentialSubject().getId();
        String didDoc = WalletUtil.getDidDoc(apiGateWayUrl, did).get();
        DidDocVo didDocVo = MessageUtil.deserialize(didDoc, DidDocVo.class);
        DIDDocument didDocument = MessageUtil.deserialize(new String(MultibaseUtils.decode(didDocVo.getDidDoc())), DIDDocument.class);
        String encodePubKey = "";
        for(VerificationMethod vm : didDocument.getVerificationMethod()){
            if(vm.getId().equals("assert")){
                encodePubKey = vm.getPublicKeyMultibase();
            }
        }
        byte[] signature = MultibaseUtils.decode(vc.getProof().getProofValue());
        VCProof proof = vc.getProof();
        proof.setProofValue(null);
        proof.setProofValueList(null);
        vc.setProof(proof);
        boolean result = walletCore.verify(MultibaseUtils.decode(encodePubKey), vc.toJson().getBytes(StandardCharsets.UTF_8), signature);
        walletLogger.d("verify verifyProof : " + result);
        return result;
    }
    private void verifyCertVc(RoleType.ROLE_TYPE roleType, String providerDID, String providerURL, String apiGateWayUrl) throws WalletException, WalletCoreException, UtilityException,  ExecutionException, InterruptedException{

        String certVcStr = WalletUtil.getCertVc(providerURL).get();
        VerifiableCredential certVc = MessageUtil.deserialize(certVcStr, VerifiableCredential.class);

        if (!certVc.getCredentialSubject().getId().equals(providerDID))
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_DID_MATCH_FAIL);

        String schemaUrl = certVc.getCredentialSchema().getId();
        String schemaData = WalletUtil.getVcSchema(schemaUrl).get();
        VCSchema vcSchema = MessageUtil.deserialize(schemaData, VCSchema.class);
        List<VCSchema.Claim> vcSchemaClaims = vcSchema.getCredentialSubject().getClaims();
        List<Claim> certVcClaims = certVc.getCredentialSubject().getClaims();

        boolean isExistRole = false;
        for (VCSchema.Claim schemaClaim : vcSchemaClaims) {
            for (VCSchema.ClaimDef item : schemaClaim.items) {
                if (item.caption.equals("role")) {
                    for (Claim certVcClaim : certVcClaims) {
                        if (certVcClaim.getCaption().equals(item.caption)) {
                            if (roleType.getValue().equals(certVcClaim.getValue()))
                                isExistRole = true;
                        }
                    }
                }
            }
        }
        if (!isExistRole)
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_ROLE_MATCH_FAIL);
        if (!verifyProof(certVcStr, false, apiGateWayUrl))
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_CERT_VC_FAIL);
    }


}
