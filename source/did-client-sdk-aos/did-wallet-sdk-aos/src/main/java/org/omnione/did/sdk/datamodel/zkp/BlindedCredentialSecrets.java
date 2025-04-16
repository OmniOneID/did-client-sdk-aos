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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerMapSerializer;
import org.omnione.did.sdk.core.zkp.other.util.BigIntegerSerializer;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class BlindedCredentialSecrets {

    @JsonAdapter(BigIntegerSerializer.class)
    private BigInteger u;

    @SerializedName("hiddenAttributes")
    private List<String> hiddenAttrs;

    public BlindedCredentialSecrets() {}

    public BlindedCredentialSecrets(BigInteger u, BigInteger v_prime,
                                    List<String> hiddenAttrs,
                                    LinkedHashMap<String, BigInteger> committedAttrs) {
        this.u = u;
        this.hiddenAttrs = (hiddenAttrs == null) ? new LinkedList<String>() : hiddenAttrs;
    }

    public BigInteger getU() {
        return u;
    }

    public List<String> getHiddenAttrs() {
        return hiddenAttrs;
    }

    public String toJson() {
        GsonWrapper gson = new GsonWrapper();
        return gson.toJson(this);
    }
}