/*
 * Copyright 2026 OmniOne.
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
package org.omnione.did.sdk.datamodel.common;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OIDV4VPChallenge extends BaseObject {
    @SerializedName("domain")
    @Expose
    String domain;
    @SerializedName("challenge")
    @Expose
    String challenge;

    public OIDV4VPChallenge(String domain, String challenge) {
        this.domain = domain;
        this.challenge = challenge;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    @Override
    public void fromJson(String val) {
        Gson gson = new Gson();
        OIDV4VPChallenge obj = gson.fromJson(val, OIDV4VPChallenge.class);
        domain = obj.getDomain();
        challenge = obj.getChallenge();
    }
}
