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

import java.math.BigInteger;
import java.util.Map;
import java.util.Vector;

public class PrimaryPredicateInequalityInitProof {

    @SerializedName("c_list")
    private Vector<BigInteger> cList;

    @SerializedName("tau_list")
    private Vector<BigInteger> tauList;
    private Map<String, BigInteger> u;

    @SerializedName("u_tilde")
    private Map<String, BigInteger> uTilde;
    private Map<String, BigInteger> r;

    @SerializedName("r_tilde")
    private Map<String, BigInteger> rTilde;

    @SerializedName("alpha_tilde")
    private BigInteger alphaTilde;
    private Predicate predicate;
    private Map<String, BigInteger> t;

    public PrimaryPredicateInequalityInitProof(Vector<BigInteger> c_list, Vector<BigInteger> tau_list,
                                               Map<String, BigInteger> u, Map<String, BigInteger> u_tilde, Map<String, BigInteger> r,
                                               Map<String, BigInteger> r_tilde, BigInteger alpha_tilde, Predicate predicate,
                                               Map<String, BigInteger> t) {
        this.cList = c_list;
        this.tauList = tau_list;
        this.u = u;
        this.uTilde = u_tilde;
        this.r = r;
        this.rTilde = r_tilde;
        this.alphaTilde = alpha_tilde;
        this.predicate = predicate;
        this.t = t;
    }

    public Vector<BigInteger> getCommonValues(){
        return this.cList;
    }

    public Vector<BigInteger> getTValues(){
        return this.tauList;
    }

    public Map<String, BigInteger> getU() {
        return u;
    }

    public Map<String, BigInteger> getUTilde() {
        return uTilde;
    }

    public Map<String, BigInteger> getR() {
        return r;
    }

    public Map<String, BigInteger> getRTilde() {
        return rTilde;
    }

    public BigInteger getAlphaTilde() {
        return alphaTilde;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public Map<String, BigInteger> getT() {
        return t;
    }
}
