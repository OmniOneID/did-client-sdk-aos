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
import org.omnione.did.sdk.datamodel.protocol.P142RequestVo;
import org.omnione.did.sdk.datamodel.protocol.P210RequestVo;
import org.omnione.did.sdk.datamodel.security.AccE2e;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.wallet.walletservice.network.HttpUrlConnection;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class RestoreUser {
    private Context context;
    public RestoreUser(){}
    public RestoreUser(Context context){
        this.context = context;
    }
    public CompletableFuture<String> restoreUser(String tasUrl, String txId, String serverToken, DIDAuth didAuth) {
        String api5 = "/tas/api/v1/request-restore-diddoc"; //user restoration

        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(tasUrl + api5, "POST", M142_RequestRestoreDidDocByWallet(txId, serverToken, didAuth));
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    private String M142_RequestRestoreDidDocByWallet(String txId, String serverToken, DIDAuth didAuth) {
        P142RequestVo requestVo = new P142RequestVo(WalletUtil.createMessageId(), txId);
        requestVo.setServerToken(serverToken);
        requestVo.setDidAuth(didAuth);
        String request = requestVo.toJson();
        return request;
    }
}
