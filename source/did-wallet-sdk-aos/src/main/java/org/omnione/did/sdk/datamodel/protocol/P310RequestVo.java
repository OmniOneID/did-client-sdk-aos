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
import org.omnione.did.sdk.datamodel.security.ReqEcdh;

public class P310RequestVo extends BaseRequestVo {
    private String offerId; //VP offer Id(uuid) option
    private ReqEcdh reqEcdh;
    private AccE2e accE2e;
    private String encVp;

    public P310RequestVo(String id) {
        super(id);
    }
    public P310RequestVo(String id, String txId) {
        super(id, txId);
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

    public AccE2e getAccE2e() {
        return accE2e;
    }

    public void setAccE2e(AccE2e accE2e) {
        this.accE2e = accE2e;
    }

    public String getEncVp() {
        return encVp;
    }

    public void setEncVp(String encVp) {
        this.encVp = encVp;
    }

}
