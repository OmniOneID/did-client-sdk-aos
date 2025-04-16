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

import org.omnione.did.sdk.core.zkp.other.type.PredicateType;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.util.HashSet;
import java.util.TreeSet;

public class SubProofRequest {

    @SerializedName("revealedAttrs")
    private TreeSet<String> revealedAttrs;

    @SerializedName("predicates")
    private HashSet<Predicate> predicates;

    public SubProofRequest() {
        this.revealedAttrs = new TreeSet<String>();
        this.predicates = new HashSet<Predicate>();
    }

    public HashSet<Predicate> getPredicates() {
        return predicates;
    }

    public TreeSet<String> getRevealedAttrs() {
        return revealedAttrs;
    }

    public SubProofRequest addRevealedAttribute(String attr) {
        this.revealedAttrs.add(attr);
        return this;
    }

    @Deprecated
    public SubProofRequest addPredicate(String attrName, String pType, int value) {
        PredicateType type = PredicateType.valueOf(pType);
        predicates.add(new Predicate(attrName, type, value));
        return this;
    }
    public SubProofRequest addPredicate(String attrName, PredicateType pType, int value) {
        predicates.add(new Predicate(attrName, pType, value));
        return this;
    }

    public String toJson() {
        return GsonWrapper.getGsonPrettyPrinting().toJson(this);
    }
}
