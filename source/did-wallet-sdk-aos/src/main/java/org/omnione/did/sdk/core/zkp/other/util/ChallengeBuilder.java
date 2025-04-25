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

package org.omnione.did.sdk.core.zkp.other.util;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * for Fiat-Shamir challenge
 */
public class ChallengeBuilder {
    private final ByteArrayOutputStream values;

    public ChallengeBuilder() {
        this.values = new ByteArrayOutputStream();
    }

    public ChallengeBuilder add(BigInteger value) throws WalletCoreException {
        return add(BigIntegerUtil.asUnsignedByteArray(value));
    }

    public ChallengeBuilder add(byte[] value) throws WalletCoreException {
        try {
            this.values.write(value);
            return this;
        } catch(IOException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_IO, "[Challenge Building]");
        }
    }

    public ChallengeBuilder add(LinkedHashMap<String, BigInteger> map) throws WalletCoreException {

        if (map == null || map.size() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "[Challenge Building] map");
        }

        for (String key : map.keySet()) {
            this.add(map.get(key));
        }

        return this;
    }

    public <T> ChallengeBuilder add(Vector<T> vector) throws WalletCoreException {

        if (vector == null || vector.size() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "[Challenge Building] vector");
        }
        T element = vector.get(0);

        if (element instanceof BigInteger) {
            for (T value : vector) {
                this.add((BigInteger)value);
            }
        } else if (element instanceof byte[]) {
            for (T value : vector) {
                this.add((byte[])value);
            }
        } else {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NOT_SUPPORTED_TYPE, "[Challenge Building] not supported Type " + element.getClass().getName());
        }
        return this;
    }

    public final byte[] build() throws WalletCoreException {
        if (values == null || values.size() == 0) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_NULL, "ChallengeBuilder value is null");
        }
        return this.values.toByteArray();
    }

    public final BigInteger buildWithHashing() throws WalletCoreException, UtilityException {
        return BigIntegerUtil.getHash(this.build());
    }
}
