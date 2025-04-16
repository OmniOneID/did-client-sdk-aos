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
import android.os.Build;
import android.util.Log;

import org.omnione.did.sdk.datamodel.protocol.P132RequestVo;
import org.omnione.did.sdk.datamodel.did.SignedDidDoc;
import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.wallet.walletservice.config.Config;
import org.omnione.did.sdk.wallet.walletservice.network.HttpUrlConnection;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class RegUser {
    private Context context;
    public RegUser(){}
    public RegUser(Context context){
        this.context = context;
    }
    public CompletableFuture<String> registerUser(String tasUrl, String txId, String serverToken, SignedDidDoc signedDIDDoc) {
        String api4 = "/tas/api/v1/request-register-user"; //user registration

        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return httpUrlConnection.send(tasUrl + api4, "POST", M132_RequestRegisterUserByWallet(txId, serverToken, signedDIDDoc));
            } catch (CommunicationException e) {
                throw new CompletionException(e);
            }
        })
            .thenCompose(CompletableFuture::completedFuture)
            .exceptionally(ex -> {
                throw new CompletionException(ex);
        });
    }

    private String M132_RequestRegisterUserByWallet(String txId, String serverToken, SignedDidDoc signedDIDDoc){
        P132RequestVo requestVo = new P132RequestVo(WalletUtil.createMessageId(), txId);
        requestVo.setSignedDidDoc(signedDIDDoc);
        requestVo.setServerToken(serverToken);
        String request = requestVo.toJson();
        return request;
    }
}
