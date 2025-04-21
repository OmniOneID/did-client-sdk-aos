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

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.zkp.other.type.PredicateType;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AvailableReferent {
    @SerializedName("selfAttrReferent")
    private Map<String, AttrReferent> selfAttrReferent;

    @SerializedName("attrReferent")
    private Map<String, AttrReferent> attrReferent;

    @SerializedName("predicateReferent")
    private Map<String, PredicateReferent> predicateReferent;

    public AvailableReferent() {
    }

    public AvailableReferent(Builder builder) {
        this.attrReferent = builder.attrReferent;
        this.predicateReferent = builder.predicateReferent;
        this.selfAttrReferent = builder.selfAttrReferent;
    }

    public Map<String, AttrReferent> getAttrReferent() {
        return attrReferent;
    }

    public Map<String, AttrReferent> getSelfAttrReferent() {
        return selfAttrReferent;
    }

    public void addAttrReferent(String key, AttrReferent value) {
        this.attrReferent.put(key, value);
    }

    public void addPredicates(String key, PredicateReferent value) {
        this.predicateReferent.put(key, value);
    }

    public Map<String, PredicateReferent> getPredicateReferent() {
        return predicateReferent;
    }


    public static class Builder {
        private Map<String, AttrReferent> selfAttrReferent = new HashMap<String, AttrReferent>();
        private Map<String, AttrReferent> attrReferent = new HashMap<String, AttrReferent>();
        private Map<String, PredicateReferent> predicateReferent = new HashMap<String, PredicateReferent>();

        public Builder() {
        }

        public Builder setSelfAttrReferent(Map<String, AttrReferent> selfAttrReferent) {
            this.selfAttrReferent = selfAttrReferent;
            return this;
        }

        public Builder setAttrReferent(Map<String, AttrReferent> attrReferent) {
            this.attrReferent = attrReferent;
            return this;
        }

        public Builder setPredicateReferent(Map<String, PredicateReferent> predicateReferent) {
            this.predicateReferent = predicateReferent;
            return this;
        }

        public AvailableReferent build() {
            return new AvailableReferent(this);
        }
    }

    public static Map<String, AttrReferent> addSelfAttrReferent(Map<String, AttributeInfo> attrInfoMap) throws WalletCoreException {

        try {
            Map<String, AttrReferent> attrMap = new HashMap<String, AttrReferent>();
            // LOOP: attribute referent
            for (String attrReferentKey : attrInfoMap.keySet()) {
                AttributeInfo attrReferentValue = attrInfoMap.get(attrReferentKey);
                if (attrReferentValue.getRestrictions().size() == 0) {
                    List<SubReferent> subList = new LinkedList<SubReferent>();
                    attrMap.put(attrReferentKey, new AttrReferent.Builder()
                            .setName(attrReferentValue.getName())
                            .setCheckRevealed(true)
                            .setAttrSubReferent(subList)
                            .build());
                }
            }
            return attrMap;
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_NOT_FOUND_AVAILABLE_REQUEST_ATTRIBUTE);
        }
    }

    public static Map<String, AttrReferent> addAttrReferent(Map<String, AttributeInfo> attrInfoMap, ArrayList<Credential> credentialList) throws WalletCoreException {

        WalletLogger.getInstance().d("attrInfoMap: " + GsonWrapper.getGsonPrettyPrinting().toJson(attrInfoMap));
        Map<String, AttrReferent> attrMap = new HashMap<String, AttrReferent>();
        try {
            // LOOP: attribute referent
            for (String attrReferentKey : attrInfoMap.keySet()) {
                AttributeInfo attrReferentValue = attrInfoMap.get(attrReferentKey);
                WalletLogger.getInstance().d("attrReferentValue: " + GsonWrapper.getGsonPrettyPrinting().toJson(attrReferentValue));
                List<SubReferent> subList = new LinkedList<SubReferent>();

                for (Credential credential : credentialList) {
                    if (attrReferentValue.getRestrictions().size() > 0) {
                        for (Map<String, String> restrictionMap : attrReferentValue.getRestrictions()) {

                            if (restrictionMap.get("credDefId").equals(credential.getCredDefId())) {
                                LinkedHashMap<String, AttributeValue> values = credential.getValues();

                                for (String attrKey : values.keySet()) {
                                    AttributeValue attrValue = values.get(attrKey);
                                    WalletLogger.getInstance().d("attrKey: " + GsonWrapper.getGsonPrettyPrinting().toJson(attrKey));
                                    WalletLogger.getInstance().d("attrValue: " + GsonWrapper.getGsonPrettyPrinting().toJson(attrValue));

                                    if (attrKey.equals(attrReferentValue.getName())) {
                                        // attrReferent 생성
                                        subList.add(new SubReferent(attrValue.getRaw(), credential.getCredentialId(), credential.getCredDefId()));
                                        attrMap.put(attrReferentKey, new AttrReferent.Builder()
                                                .setName(attrKey)
                                                .setCheckRevealed(true)
                                                .setAttrSubReferent(subList)
                                                .build());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_NOT_FOUND_AVAILABLE_REQUEST_ATTRIBUTE);
        }

        return attrMap;
    }

    public static Map<String, PredicateReferent> addPredicateReferent(Map<String, PredicateInfo> predInfoMap, ArrayList<Credential> credentialList) throws WalletCoreException {

        WalletLogger.getInstance().d(predInfoMap + GsonWrapper.getGsonPrettyPrinting().toJson(predInfoMap));
        Map<String, PredicateReferent> predicateMap = new HashMap<String, PredicateReferent>();
        try {
            for (String predicateReferentKey : predInfoMap.keySet()) {
                PredicateInfo predicateReferentValue = predInfoMap.get(predicateReferentKey);
                WalletLogger.getInstance().d("predicateReferentValue: " + GsonWrapper.getGsonPrettyPrinting().toJson(predicateReferentValue));

                List<SubReferent> predicateSubList = new LinkedList<SubReferent>();

                for (Credential credential : credentialList) {

//                    WalletLogger.getInstance().d("key: " + ZkpGsonWrapper.getGsonPrettyPrinting().toJson(credInfo.getCredentialId()));
//                    WalletLogger.getInstance().d("value: " + ZkpGsonWrapper.getGsonPrettyPrinting().toJson(credInfo.getCredential()));

                    if (predicateReferentValue.getRestrictions().size() > 0) {

                        for (Map<String, String> restrictionMap : predicateReferentValue.getRestrictions()) {
                            if (restrictionMap.get("credDefId").equals(credential.getCredDefId())) {
                                LinkedHashMap<String, AttributeValue> values = credential.getValues();

                                for (String predicateKey : values.keySet()) {
                                    AttributeValue predValue = values.get(predicateKey);
                                    WalletLogger.getInstance().d("predicateKey: " + GsonWrapper.getGsonPrettyPrinting().toJson(predicateKey));
                                    WalletLogger.getInstance().d("predValue: " + GsonWrapper.getGsonPrettyPrinting().toJson(predValue));

                                    if (predicateKey.equals(predicateReferentValue.getName())) {

                                        /**
                                         *     GE(" >= "),
                                         *     LE(" <= "),
                                         *     GT(" > "),
                                         *     LT(" < "),
                                         **/
                                        int pValue = predicateReferentValue.getPValue();
                                        try {
                                            WalletLogger.getInstance().d("GET VALUE: " + predicateReferentValue.getPType().getValue());
                                            WalletLogger.getInstance().d("pValue: " + pValue);
                                            WalletLogger.getInstance().d("raw: " + Integer.parseInt(predValue.getRaw()));
                                        } catch (Exception e) {
                                            WalletLogger.getInstance().d("OMNI_ERROR_ZKP_NOT_SUPPORTED_PREDICATE_TYPE, " + "number format exception for input string {" + predValue.getRaw() + "}");
                                            continue;
//                                            throw new ZkpException(ZkpErrorCode.OMNI_ERROR_ZKP_NOT_SUPPORTED_PREDICATE_TYPE, "number format exception for input string {" + predValue.getRaw() + "}");
                                        }

                                        if (predicateReferentValue.getPType() == PredicateType.GE && Integer.parseInt(predValue.getRaw()) >= pValue) {
                                            predicateSubList.add(new SubReferent(predValue.getRaw(), credential.getCredentialId(), credential.getCredDefId()));
                                            predicateMap.put(predicateReferentKey, new PredicateReferent.Builder().setName(predicateKey).setCheckRevealed(false).setPredicateReferent(predicateSubList).build());

                                        } else if (predicateReferentValue.getPType() == PredicateType.LE && Integer.parseInt(predValue.getRaw()) <= pValue) {
                                            predicateSubList.add(new SubReferent(predValue.getRaw(), credential.getCredentialId(), credential.getCredDefId()));
                                            predicateMap.put(predicateReferentKey, new PredicateReferent.Builder().setName(predicateKey).setCheckRevealed(false).setPredicateReferent(predicateSubList).build());

                                        } else if (predicateReferentValue.getPType() == PredicateType.GT && Integer.parseInt(predValue.getRaw()) > pValue) {
                                            predicateSubList.add(new SubReferent(predValue.getRaw(), credential.getCredentialId(), credential.getCredDefId()));
                                            predicateMap.put(predicateReferentKey, new PredicateReferent.Builder().setName(predicateKey).setCheckRevealed(false).setPredicateReferent(predicateSubList).build());

                                        } else if (predicateReferentValue.getPType() == PredicateType.LT && Integer.parseInt(predValue.getRaw()) < pValue) {
                                            predicateSubList.add(new SubReferent(predValue.getRaw(), credential.getCredentialId(), credential.getCredDefId()));
                                            predicateMap.put(predicateReferentKey, new PredicateReferent.Builder().setName(predicateKey).setCheckRevealed(false).setPredicateReferent(predicateSubList).build());
                                        }
                                    }
                                }
                            } else {

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_PROVER_NOT_FOUND_AVAILABLE_PREDICATE_ATTRIBUTE);
        }

        return predicateMap;

    }
}