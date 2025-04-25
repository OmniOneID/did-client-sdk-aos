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

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerUtil;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerMapSerializer;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;

import java.math.BigInteger;
import java.util.Map;

public class PrimaryEqualInitProof {

    @SerializedName("a_prime")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger aPrime;

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger t;

    @SerializedName("e_tilde")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger eTilde;

    @SerializedName("e_prime")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger ePrime;

    @SerializedName("v_tilde")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger vTilde;

    @SerializedName("v_prime")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger vPrime;

    @SerializedName("m_tilde")
    @JsonAdapter(BigIntegerMapSerializer.class)
    private Map<String, BigInteger> mTilde;

    @SerializedName("m2_tilde")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger m2Tilde;

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger m2;

    public PrimaryEqualInitProof(BigInteger a_prime, BigInteger t, BigInteger e_tilde, BigInteger e_prime,
                                 BigInteger v_tilde, BigInteger v_prime, Map<String, BigInteger> m_tilde,
                                 BigInteger m2_tilde, BigInteger m2) {

        this.aPrime = a_prime;
        this.t = t;
        this.eTilde = e_tilde;
        this.ePrime = e_prime;
        this.vTilde = v_tilde;
        this.vPrime = v_prime;
        this.mTilde = m_tilde;
        this.m2Tilde = m2_tilde;
        this.m2 = m2;
    }

    public byte[] getCommonValue() {
        return BigIntegerUtil.asUnsignedByteArray(aPrime);
    }

    public byte[] getTvalue() {
        return BigIntegerUtil.asUnsignedByteArray(t);
    }

    public Map<String, BigInteger> getM_tilde() {
        return mTilde;
    }

    public BigInteger getAPrime() {
        return aPrime;
    }

    public BigInteger getETilde() {
        return eTilde;
    }

    public BigInteger getEPrime() {
        return ePrime;
    }

    public BigInteger getVTilde() {
        return vTilde;
    }

    public BigInteger getVPrime() {
        return vPrime;
    }

    public BigInteger getM2Tilde() {
        return m2Tilde;
    }

    public BigInteger getM2() {
        return m2;
    }
}