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

package org.omnione.did.sdk.wallet.walletservice.network.protocol;

import android.content.Context;

import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.datamodel.did.SignedDidDoc;
import org.omnione.did.sdk.datamodel.protocol.P141RequestVo;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.wallet.walletservice.network.HttpUrlConnection;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class UpdateUser {
    private Context context;
    public UpdateUser(){}
    public UpdateUser(Context context){
        this.context = context;
    }
    public CompletableFuture<String> updateUser(String tasUrl, String txId, String serverToken, DIDAuth didAuth, SignedDidDoc signedDidDoc) {
        String api5 = "/tas/api/v1/request-update-diddoc"; //user update

        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(tasUrl + api5, "POST", M141_RequestUpdateDidDocByWallet(txId, serverToken, didAuth, signedDidDoc));
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    private String M141_RequestUpdateDidDocByWallet(String txId, String serverToken, DIDAuth didAuth, SignedDidDoc signedDidDoc) {
        P141RequestVo requestVo = new P141RequestVo(WalletUtil.createMessageId(), txId);
        requestVo.setServerToken(serverToken);
        requestVo.setDidAuth(didAuth);
        requestVo.setSignedDidDoc(signedDidDoc);
        return requestVo.toJson();
    }
}
