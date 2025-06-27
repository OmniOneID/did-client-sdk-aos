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

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestedProof {

    @SerializedName("selfAttestedAttrs")
    private Map<String, String> selfAttestedAttrs = new LinkedHashMap<String, String>();

    @SerializedName("predicates")
    private Map<String, Map<String, String>> predicates = new LinkedHashMap<String, Map<String, String>>();

    @SerializedName("revealedAttrs")
    private Map<String, Map<String, String>> revealedAttrs = new LinkedHashMap<String, Map<String, String>>();

    @SerializedName("unrevealedAttrs")
    private Map<String, Map<String, String>> unrevealedAttrs = new LinkedHashMap<String, Map<String, String>>();

    public Map<String, String> getSelfAttestedAttrs() {
        return selfAttestedAttrs;
    }

    public void addSelfAttestedAttrs(Map<String, String> selfAttestedAttrs) {
        this.selfAttestedAttrs.putAll(selfAttestedAttrs);
    }

    public Map<String, Map<String, String>> getPredicates() {
        return predicates;
    }

    public void addPredicates(Map<String, Map<String, String>> predicates) {
        this.predicates.putAll(predicates);
    }

    public Map<String, Map<String, String>> getRevealedAttrs() {
        return revealedAttrs;
    }

    public void addRevealedAttrs(Map<String, Map<String, String>> revealedAttrs) {
        this.revealedAttrs.putAll(revealedAttrs);
    }

    public Map<String, Map<String, String>> getUnrevealedAttrs() {
        return unrevealedAttrs;
    }

    public void addUnrevealedAttrs(Map<String, Map<String, String>> unrevealedAttrs) {
        this.unrevealedAttrs.putAll(unrevealedAttrs);
    }
}

