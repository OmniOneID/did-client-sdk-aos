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

package org.omnione.did.sdk.wallet.walletservice.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.datamodel.common.enums.SymmetricCipherType;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.did.DidDocVo;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.datamodel.util.MessageUtil;
import org.omnione.did.sdk.datamodel.zkp.CredentialDefinition;
import org.omnione.did.sdk.datamodel.zkp.CredentialDefinitionVo;
import org.omnione.did.sdk.datamodel.zkp.ladger.ZkpCredentionDefRows;
import org.omnione.did.sdk.datamodel.zkp.ladger.ZkpLadgerRequestData;
import org.omnione.did.sdk.datamodel.zkp.ladger.ZkpLadgerResponseData;
import org.omnione.did.sdk.utility.CryptoUtils;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Encodings.Base16;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.wallet.walletservice.config.Config;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;
import org.omnione.did.sdk.wallet.walletservice.network.HttpUrlConnection;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class WalletUtil {
    public static String createMessageId(){
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
        String messageId = "";
        try {
            messageId = dateFormat.format(today) + Base16.toHex(CryptoUtils.generateNonce(4));
        } catch (Exception e){
            e.printStackTrace();
        }
        return messageId;
    }

    public static String genId(){
        UUID id = UUID.randomUUID();
        return id.toString();
    }

    public static String getDate(){
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(today);
    }
    public static String createValidUntil(int minute){
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.MINUTE, minute);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(cal.getTime());
    }
    public static boolean checkDate(String targetDateStr) {
        ZonedDateTime targetDate = ZonedDateTime.parse(targetDateStr);
        ZonedDateTime today = ZonedDateTime.parse(Instant.now().toString());

        return today.isBefore(targetDate);
    }

    public static CompletableFuture<String> getAllowedCa(String tasUrl) {
        String api = Config.TAS_GET_ALLOWED_CA + Config.WALLET_PKG_NAME;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(tasUrl + api, "GET", "");
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    public static CompletableFuture<String> getDidDoc(String apiGateWayUrl, String did) {
        String api = Config.API_GATEWAY_GET_DID_DOC + did;
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(apiGateWayUrl + api, "GET", "");
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    public static CompletableFuture<String> getCertVc(String certVcUrl){
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(certVcUrl, "GET", "");
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    public static CompletableFuture<CredentialDefinition> getCredentialDefinition(String gatewayAPI, String credDefId){
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();
        return CompletableFuture.supplyAsync(() -> {
            try {
                WalletLogger.getInstance().d("credDefUrl: "+gatewayAPI);
                String credDef = httpUrlConnection.send(gatewayAPI+"/api-gateway/api/v1/zkp-cred-def?id="+credDefId, "GET", "");

                CredentialDefinitionVo credentialDefinitionVo = MessageUtil.deserialize(credDef, CredentialDefinitionVo.class);
                CredentialDefinition credentialDefinition = MessageUtil.deserialize(new String(MultibaseUtils.decode(credentialDefinitionVo.getCredDef())), CredentialDefinition.class);
                WalletLogger.getInstance().d("credentialDefinition: "+GsonWrapper.getGson().toJson(credentialDefinition));
                return credentialDefinition;

                } catch (CommunicationException e) {
                    throw new CompletionException(e);
                } catch (UtilityException e) {
                throw new RuntimeException(e);
            }
                })
            .thenCompose(CompletableFuture::completedFuture)
            .exceptionally(ex -> {
                throw new CompletionException(ex);
            });
    }

    public static CompletableFuture<String> getVcSchema(String schemaId){
        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(schemaId, "GET", "");
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    public static byte[] mergeSharedSecretAndNonce(byte[] sharedSecret, byte[] nonce, SymmetricCipherType.SYMMETRIC_CIPHER_TYPE cipherType) throws UtilityException {
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

}
