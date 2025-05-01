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

import org.omnione.did.sdk.core.zkp.other.util.BigIntegerMapSerializer;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;

import java.math.BigInteger;
import java.util.LinkedHashMap;

public class PublicKeyMetadata {

    @JsonAdapter(BigIntegerSerializer.class)
    private final BigInteger xz;

    @JsonAdapter(BigIntegerMapSerializer.class)
    private final LinkedHashMap<String, BigInteger> xr;

    public PublicKeyMetadata(BigInteger xz, LinkedHashMap<String, BigInteger> xr) {
        this.xz = xz;
        this.xr = xr;
    }

    public BigInteger getXz() {
        return xz;
    }

    public LinkedHashMap<String, BigInteger> getXr() {
        return xr;
    }
}