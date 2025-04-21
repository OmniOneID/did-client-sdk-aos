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

import org.omnione.did.sdk.core.zkp.other.type.AttributeType;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;

import java.math.BigInteger;

public class CredentialValue {
    private final AttributeType type;

    @JsonAdapter(BigIntegerSerializer.class)
    private final BigInteger value;

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger blindingFactor;

    public CredentialValue(AttributeType type, BigInteger value) {
        this.type = type;
        this.value = value;
    }

    public boolean isKnown() {
        return type.equals(AttributeType.Known);
    }

    public boolean isHidden() {
        return type.equals(AttributeType.Hidden);
    }

    public boolean isCommitment() {
        return type.equals(AttributeType.Commitment);
    }

    public BigInteger getValue() {
        return value;
    }

    public AttributeType getType() {
        return type;
    }

    public BigInteger getBlindingFactor() {
        return blindingFactor;
    }
}
