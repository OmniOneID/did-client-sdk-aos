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

package org.omnione.did.sdk.datamodel.oid4vc.dcql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CredentialQuery {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("format")
    @Expose
    private String format;

    @SerializedName("meta")
    @Expose
    private Map<String, Object> meta;

    @SerializedName("claims")
    @Expose
    private List<ClaimQuery> claims;

    @SerializedName("claim_sets")
    @Expose
    private List<List<String>> claimSets;

    @SerializedName("trusted_authorities")
    @Expose
    private List<TrustedAuthority> trustedAuthorities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public List<ClaimQuery> getClaims() {
        return claims;
    }

    public void setClaims(List<ClaimQuery> claims) {
        this.claims = claims;
    }

    public List<List<String>> getClaimSets() {
        return claimSets;
    }

    public void setClaimSets(List<List<String>> claimSets) {
        this.claimSets = claimSets;
    }

    public List<TrustedAuthority> getTrustedAuthorities() {
        return trustedAuthorities;
    }

    public void setTrustedAuthorities(List<TrustedAuthority> trustedAuthorities) {
        this.trustedAuthorities = trustedAuthorities;
    }

    public List<String> metaStringList(String key) {
        List<String> result = new ArrayList<>();
        if (meta == null) {
            return result;
        }
        Object raw = meta.get(key);
        if (raw instanceof List) {
            for (Object o : (List<?>) raw) {
                if (o != null) {
                    result.add(String.valueOf(o));
                }
            }
        } else if (raw != null) {
            result.add(String.valueOf(raw));
        }
        return result;
    }
}
