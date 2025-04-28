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

package org.omnione.did.sdk.datamodel.zkp;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.core.zkp.other.util.BigIntegerMapSerializer;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class PrimaryEqualProof {

    @SerializedName("revealedAttrs")
    @JsonAdapter(BigIntegerMapSerializer.class)
    private TreeMap<String, BigInteger> revealedAttrs;

    @SerializedName("aPrime")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger aPrime;

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger e;

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger v;

    @JsonAdapter(BigIntegerMapSerializer.class)
    private Map<String, BigInteger> m;

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger m2;

    public PrimaryEqualProof(Map<String, BigInteger> revealedAttrs, BigInteger aPrime, BigInteger e,
                             BigInteger v, Map<String, BigInteger> m, BigInteger m2) {

        this.revealedAttrs = new TreeMap<String, BigInteger>(revealedAttrs);
        this.aPrime = aPrime;
        this.e = e;
        this.v = v;
        this.m = m;
        this.m2 = m2;
    }

    public TreeMap<String, BigInteger> getRevealedAttrs() {
        return revealedAttrs;
    }

    public BigInteger getaPrime() {
        return aPrime;
    }
    public void setaPrime(BigInteger aPrime) {
        this.aPrime = aPrime;
    }

    public BigInteger getE() {
        return e;
    }
    public void setE(BigInteger e) {
        this.e = e;
    }

    public BigInteger getV() {
        return v;
    }
    public void setV(BigInteger v) {
        this.v = v;
    }

    public Map<String, BigInteger> getM() {
        return m;
    }
    public void setM(Map<String, BigInteger> m) {
        this.m = m;
    }

    public BigInteger getM2() {
        return m2;
    }

    public void setM2(BigInteger m2) {
        this.m2 = m2;
    }
}