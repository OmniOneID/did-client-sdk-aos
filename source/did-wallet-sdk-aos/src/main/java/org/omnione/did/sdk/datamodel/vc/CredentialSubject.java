/*
 * Copyright 2024-2025 OmniOne.
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

package org.omnione.did.sdk.datamodel.vc;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CredentialSubject {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("claims")
    @Expose
    private List<Claim> claims;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    public void fromJson(String val) {
        Gson gson = new Gson();
        CredentialSubject obj = gson.fromJson(val, CredentialSubject.class);

        this.id = obj.getId();
        this.claims = obj.getClaims();
    }
}
