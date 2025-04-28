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

package org.omnione.did.sdk.core.zkp.other.helper;

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.zkp.other.util.ZkpConstants;
import org.omnione.did.sdk.datamodel.zkp.AttributeValue;
import org.omnione.did.sdk.datamodel.zkp.CredentialValues;
import org.omnione.did.sdk.datamodel.zkp.MasterSecret;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Set;

public class CredentialValueHelper {
    // Create Credential Value
    public static CredentialValues generateCredentialValues(LinkedHashMap<String, AttributeValue> credValues, MasterSecret masterSecret) throws WalletCoreException, UtilityException {

        CredentialValues credentialValues = new CredentialValues();
        credentialValues.addHidden(ZkpConstants.MASTER_SECRET_KEY, masterSecret.getMasterSecret());
        Set<String> set = credValues.keySet();
        for(String key: set) {
            AttributeValue value = credValues.get(key);
            try {
                credentialValues.addKnown(key, new BigInteger(value.getRaw()));
            } catch (NumberFormatException numberFormatException) {
                credentialValues.addKnown(key, new BigInteger(1, DigestUtils.getDigest(value.getRaw().getBytes(), DigestEnum.DIGEST_ENUM.SHA_256)));
            }
        }

        return credentialValues;

    }
}