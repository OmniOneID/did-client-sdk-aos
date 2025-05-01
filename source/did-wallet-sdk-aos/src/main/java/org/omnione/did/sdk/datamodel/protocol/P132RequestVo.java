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

import org.omnione.did.sdk.datamodel.did.AttestedDidDoc;
import org.omnione.did.sdk.datamodel.did.SignedDidDoc;
import org.omnione.did.sdk.datamodel.security.ReqEcdh;
import org.omnione.did.sdk.datamodel.token.ServerTokenSeed;

public class P132RequestVo extends BaseRequestVo {

    private AttestedDidDoc attestedDIDDoc;
    private ReqEcdh reqEcdh;
    private ServerTokenSeed seed;
    private SignedDidDoc signedDidDoc;
    private String serverToken;

    private String iv;
    private String kycTxId;

    public P132RequestVo(String id) {
        super(id);
    }
    public P132RequestVo(String id, String txId) {
        super(id, txId);
    }

    public AttestedDidDoc getAttestedDIDDoc() {
        return attestedDIDDoc;
    }

    public void setAttestedDIDDoc(AttestedDidDoc attestedDIDDoc) {
        this.attestedDIDDoc = attestedDIDDoc;
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

    public SignedDidDoc getSignedDidDoc() {
        return signedDidDoc;
    }

    public void setSignedDidDoc(SignedDidDoc signedDidDoc) {
        this.signedDidDoc = signedDidDoc;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getKycTxId() {
        return kycTxId;
    }

    public void setKycTxId(String kycTxId) {
        this.kycTxId = kycTxId;
    }
    //    public String toJson() {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
//                .registerTypeAdapterFactory(new StringEnumAdapterFactory())
//                .create();
//
//        String json = gson.toJson(this);
//        return ODIJsonSortUtil.sortJsonString(gson, json);
//    }

}
