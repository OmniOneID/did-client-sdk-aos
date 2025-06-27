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
import android.util.Log;

import org.omnione.did.sdk.datamodel.protocol.P220RequestVo;
import org.omnione.did.sdk.datamodel.security.AccE2e;
import org.omnione.did.sdk.datamodel.vc.issue.ReqRevokeVC;
import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.utility.CryptoUtils;
import org.omnione.did.sdk.wallet.walletservice.config.Config;
import org.omnione.did.sdk.wallet.walletservice.network.HttpUrlConnection;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class RevokeVc {
    private Context context;
    public RevokeVc(){}
    public RevokeVc(Context context){
        this.context = context;
    }
    public CompletableFuture<String> revokeVc(String tasUrl, String txId, String serverToken, ReqRevokeVC reqRevokeVc) {
        String api5 = "/tas/api/v1/request-revoke-vc"; //VC revocation

        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(tasUrl + api5, "POST", M220_RequestRevokeVcByWallet(txId, serverToken, reqRevokeVc));
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    private String M220_RequestRevokeVcByWallet(String txId, String serverToken, ReqRevokeVC reqRevokeVC) {
        P220RequestVo requestVo = new P220RequestVo(WalletUtil.createMessageId(), txId);
        requestVo.setServerToken(serverToken);
        requestVo.setReqRevokeVc(reqRevokeVC);
        return requestVo.toJson();
    }
}
