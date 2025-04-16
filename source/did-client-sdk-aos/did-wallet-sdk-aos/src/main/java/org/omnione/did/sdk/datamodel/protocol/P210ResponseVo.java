/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.datamodel.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.omnione.did.sdk.datamodel.profile.IssueProfile;
import org.omnione.did.sdk.datamodel.security.AccEcdh;
import org.omnione.did.sdk.datamodel.security.E2e;
import org.omnione.did.sdk.datamodel.util.IntEnumAdapterFactory;
import org.omnione.did.sdk.datamodel.util.StringEnumAdapterFactory;

public class P210ResponseVo extends BaseResponseVo {
    private String refId;
    private AccEcdh accEcdh;
    private String iv;
    private String encStd;
    private String authNonce;
    private IssueProfile profile;
    private E2e e2e;

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public AccEcdh getAccEcdh() {
        return accEcdh;
    }

    public void setAccEcdh(AccEcdh accEcdh) {
        this.accEcdh = accEcdh;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEncStd() {
        return encStd;
    }

    public void setEncStd(String encStd) {
        this.encStd = encStd;
    }

    public String getAuthNonce() {
        return authNonce;
    }

    public void setAuthNonce(String authNonce) {
        this.authNonce = authNonce;
    }

    public IssueProfile getProfile() {
        return profile;
    }

    public void setProfile(IssueProfile profile) {
        this.profile = profile;
    }

    public E2e getE2e() {
        return e2e;
    }

    public void setE2e(E2e e2e) {
        this.e2e = e2e;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new IntEnumAdapterFactory())
                .registerTypeAdapterFactory(new StringEnumAdapterFactory())
                .disableHtmlEscaping()
                .create();

        String json = gson.toJson(this);
        return json;
    }
}
