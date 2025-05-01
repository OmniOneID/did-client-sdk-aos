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

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.zkp.other.type.AttributeType;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

public class CredentialValues {

    private Map<String, CredentialValue> attrValues;

    public CredentialValues() {
        this.attrValues = new TreeMap<String, CredentialValue>();
    }

    public Map<String, CredentialValue> getAttrValues() {
        return attrValues;
    }
    public void setAttrValues(Map<String, CredentialValue> attrValues) {
        this.attrValues = attrValues;
    }


    public void addHidden(String key, AttributeValue value) throws WalletCoreException {
        addHidden(key, value.getEncoded());
    }
    public void addHidden(String key, String decimalHexStrValue) throws WalletCoreException {
        addValue(AttributeType.Hidden, key, new BigInteger(decimalHexStrValue));
    }
    public void addHidden(String key, BigInteger value) throws WalletCoreException {
        addValue(AttributeType.Hidden, key, value);
    }

    public void addKnown(String key, AttributeValue value) throws WalletCoreException {
        addKnown(key, value.getEncoded());
    }
    public void addKnown(String key, String decimalHexStrValue) throws WalletCoreException {
        addValue(AttributeType.Known, key, new BigInteger(decimalHexStrValue));
    }
    public void addKnown(String key, BigInteger value) throws WalletCoreException {
        addValue(AttributeType.Known, key, value);
    }

    private void addValue(AttributeType type, String key, BigInteger value) throws WalletCoreException {
        if (attrValues.containsKey(key)) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_ZKP_DUPLICATED, key);
        }
        CredentialValue credentialValue = new CredentialValue(type, value);
        attrValues.put(key, credentialValue);
    }

    public CredentialValue get(String key) {
        return attrValues.get(key);
    }
    public CredentialValue get(Predicate predicate) {
        return attrValues.get(predicate.getAttrName());
    }
}
