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

import org.omnione.did.sdk.datamodel.security.AccE2e;
import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.datamodel.security.ReqEcdh;
import org.omnione.did.sdk.datamodel.token.ServerTokenSeed;

public class P210RequestVo extends BaseRequestVo {
    private String vcPlanId;
    private String issuer; //issuer did option
    private String offerId; //VC offer Id(uuid) option
    private ReqEcdh reqEcdh;
    private ServerTokenSeed seed;
    private String serverToken;
    private DIDAuth didAuth;
    private AccE2e accE2e;
    private String encReqVc;
    private String vcId;

    public P210RequestVo(String id) {
        super(id);
    }
    public P210RequestVo(String id, String txId) {
        super(id, txId);
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public DIDAuth getDIDAuth() {
        return didAuth;
    }

    public void setDIDAuth(DIDAuth didAuth) {
        this.didAuth = didAuth;
    }

    public AccE2e getAccE2e() {
        return accE2e;
    }

    public void setAccE2e(AccE2e accE2e) {
        this.accE2e = accE2e;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
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

    public String getEncReqVc() {
        return encReqVc;
    }

    public void setEncReqVc(String encReqVc) {
        this.encReqVc = encReqVc;
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

//    public String toJson() {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
//                .create();
//
//        String json = gson.toJson(this);
//        return ODIJsonSortUtil.sortJsonString(gson, json);
//    }

}
