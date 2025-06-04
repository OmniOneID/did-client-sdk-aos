/*
 * Copyright 2025 OmniOne.
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

import com.google.gson.annotations.JsonAdapter;

import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;
import org.omnione.did.sdk.datamodel.security.AccE2e;

import java.math.BigInteger;

public class P311RequestVo extends BaseRequestVo {

    private String offerId; //VP offer Id(uuid) option
    private AccE2e accE2e;
    private String encProof;
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger nonce;

    public P311RequestVo(String id) {
        super(id);
    }
    public P311RequestVo(String id, String txId) {
        super(id, txId);
    }

    public AccE2e getAccE2e() {
        return accE2e;
    }

    public void setAccE2e(AccE2e accE2e) {
        this.accE2e = accE2e;
    }

    public String getEncProof() {
        return encProof;
    }

    public void setEncProof(String encProof) {
        this.encProof = encProof;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}
