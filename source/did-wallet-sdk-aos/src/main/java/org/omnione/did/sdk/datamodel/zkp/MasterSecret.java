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

import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.math.BigInteger;
import java.util.UUID;

public class MasterSecret extends BaseObject {

    private String masterSecretId;

    //    @JsonIgnore
    private BigInteger masterSecret;

    public MasterSecret() {
        this.masterSecretId = UUID.randomUUID().toString();
    }

    public MasterSecret(String masterSecretId, BigInteger masterSecret) {
        this.masterSecretId = masterSecretId;
        this.masterSecret = masterSecret;
    }

    public String getMasterSecretId() {
        return masterSecretId;
    }
    public void setMasterSecretId(String masterSecretId) {
        this.masterSecretId = masterSecretId;
    }

    public BigInteger getMasterSecret() {
        return masterSecret;
    }
    public void setMasterSecret(BigInteger masterSecret) {
        this.masterSecret = masterSecret;
    }
    public String toJson() {
        return GsonWrapper.getGsonPrettyPrinting().toJson(this);
    }

    public void fromJson(String val) {

        GsonWrapper gson = new GsonWrapper();
        MasterSecret result = gson.fromJson(val, MasterSecret.class);
        this.masterSecretId = result.getMasterSecretId();
        this.masterSecret = result.getMasterSecret();
    }
}