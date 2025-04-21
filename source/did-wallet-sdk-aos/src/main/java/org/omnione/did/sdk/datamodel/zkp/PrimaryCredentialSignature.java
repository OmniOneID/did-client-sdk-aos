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

import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;

import java.math.BigInteger;

public class PrimaryCredentialSignature {

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger a;

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger e;

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger v;

    //TODO: 사용여부 확인 필요
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger q;

    //TODO: did 검증용
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger m2;

    public PrimaryCredentialSignature() {}

    public PrimaryCredentialSignature (BigInteger a, BigInteger e, BigInteger v) {
        this.a = a;
        this.e = e;
        this.v = v;
    }

    public BigInteger getA() {
        return a;
    }
    public void setA(BigInteger a) {
        this.a = a;
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

    @Deprecated
    public BigInteger getQ() {
        return q;
    }
    @Deprecated
    public void setQ(BigInteger q) {
        this.q = q;
    }

    public void addV(BigInteger vPrime) {
        this.v = this.v.add(vPrime);
    }

    public BigInteger getM2() {
        return m2;
    }

    public void setM2(BigInteger m2) {
        this.m2 = m2;
    }
}
