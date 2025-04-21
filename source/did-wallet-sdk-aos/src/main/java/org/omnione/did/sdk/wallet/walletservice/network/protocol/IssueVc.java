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

import org.omnione.did.sdk.datamodel.protocol.P210RequestVo;
import org.omnione.did.sdk.datamodel.security.AccE2e;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.wallet.walletservice.network.HttpUrlConnection;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class IssueVc {
    private Context context;
    public IssueVc(){}
    public IssueVc(Context context){
        this.context = context;
    }
    public CompletableFuture<String> issueVc(String tasUrl, String txId, String serverToken, DIDAuth didAuth, AccE2e accE2e, String encReqVc) {
        String api5 = "/tas/api/v1/request-issue-vc"; //VC issuance

        HttpUrlConnection httpUrlConnection = new HttpUrlConnection();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return httpUrlConnection.send(tasUrl + api5, "POST", M210_RequestIssueVcByWallet(txId, serverToken, didAuth, accE2e, encReqVc));
                    } catch (CommunicationException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenCompose(CompletableFuture::completedFuture)
                .exceptionally(ex -> {
                    throw new CompletionException(ex);
                });
    }

    private String M210_RequestIssueVcByWallet(String txId, String serverToken, DIDAuth didAuth, AccE2e accE2e, String encReqVc) {
        P210RequestVo requestVo = new P210RequestVo(WalletUtil.createMessageId(), txId);
        requestVo.setServerToken(serverToken);
        requestVo.setDIDAuth(didAuth);
        requestVo.setAccE2e(accE2e);
        requestVo.setEncReqVc(encReqVc);
        String request = requestVo.toJson();
        return request;
    }
}
