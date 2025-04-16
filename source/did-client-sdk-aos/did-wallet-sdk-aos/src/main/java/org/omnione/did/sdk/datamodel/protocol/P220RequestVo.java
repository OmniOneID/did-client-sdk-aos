/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.datamodel.protocol;

import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.security.ReqEcdh;
import org.omnione.did.sdk.datamodel.token.ServerTokenSeed;
import org.omnione.did.sdk.datamodel.vc.issue.ReqRevokeVC;


public class P220RequestVo extends BaseRequestVo {
    private String vcId;
    private ReqEcdh reqEcdh;
    private ServerTokenSeed seed;
    private String serverToken;
    @SerializedName("request")
    private ReqRevokeVC reqRevokeVc;

    public P220RequestVo(String id) {
        super(id);
    }
    public P220RequestVo(String id, String txId) {
        super(id, txId);
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public ReqEcdh getReqEcdh() {
        return reqEcdh;
    }

    public void setReqEcdh(ReqEcdh reqEcdh) {
        this.reqEcdh = reqEcdh;
    }

    public ServerTokenSeed getSeed() {
        return seed;
    }

    public void setSeed(ServerTokenSeed seed) {
        this.seed = seed;
    }

    public String getServerToken() {
        return serverToken;
    }
    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public ReqRevokeVC getReqRevokeVc() {
        return reqRevokeVc;
    }

    public void setReqRevokeVc(ReqRevokeVC reqRevokeVc) {
        this.reqRevokeVc = reqRevokeVc;
    }


}
