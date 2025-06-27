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
import java.util.LinkedHashMap;

public class KeyCorrectnessProof {
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger c;

    @SerializedName("xzCap")
    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger xzCap;

    @SerializedName("xrCap")
    @JsonAdapter(BigIntegerMapSerializer.class)
    private LinkedHashMap<String, BigInteger> xrCap;

    public KeyCorrectnessProof() {}
    public KeyCorrectnessProof(BigInteger c, BigInteger xzCap, LinkedHashMap<String, BigInteger> xrCap) {
        this.c = c;
        this.xzCap = xzCap;
        this.xrCap = xrCap;
    }

    public BigInteger getC() {
        return c;
    }public void setC(BigInteger c) {
        this.c = c;
    }
    public BigInteger getXzCap() {
        return xzCap;
    }

    public LinkedHashMap<String, BigInteger> getXrCap() {
        return new LinkedHashMap<String, BigInteger>(xrCap);
    }
}