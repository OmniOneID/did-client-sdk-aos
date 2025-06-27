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
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.math.BigInteger;

public class AttributeValue {

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger encoded;
    private String raw;

    public String getRaw() {
        return raw;
    }
    public BigInteger getEncoded() {
        return encoded;
    }

    public void setRaw(String raw) throws UtilityException {
        this.raw = raw;
        this.encoded = genEncode(raw);
//        this.encoded = new BigInteger(raw);
    }
    public void setEncoded(BigInteger encoded) {
        this.encoded = encoded;
    }

    public BigInteger getEncode() {
        return this.encoded;
    }

    private BigInteger genEncode(String raw) throws UtilityException {

        try {
            return new BigInteger(raw);
        } catch (NumberFormatException ne) {
            return new BigInteger(1, DigestUtils.getDigest(raw.getBytes(), DigestEnum.DIGEST_ENUM.SHA_256));
        }
    }
}