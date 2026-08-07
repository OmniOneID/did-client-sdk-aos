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

package org.omnione.did.sdk.datamodel.oid4vc;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Map;

public final class SdJwt {

    private final String compact;
    private final String credentialJwt;
    private final JsonObject jwtPayload;
    private final Map<String, Object> disclosedClaims;

    public SdJwt(String compact,
                 String credentialJwt,
                 JsonObject jwtPayload,
                 Map<String, Object> disclosedClaims) {
        this.compact = compact;
        this.credentialJwt = credentialJwt;
        this.jwtPayload = jwtPayload;
        this.disclosedClaims = Collections.unmodifiableMap(disclosedClaims);
    }

    public String getCompact() { return compact; }
    public String getCredentialJwt() { return credentialJwt; }
    public JsonObject getJwtPayload() { return jwtPayload; }
    public Map<String, Object> getDisclosedClaims() { return disclosedClaims; }

    public String getIssuer() {
        return jwtPayload.has("iss") ? jwtPayload.get("iss").getAsString() : null;
    }

    public String getVct() {
        return jwtPayload.has("vct") ? jwtPayload.get("vct").getAsString() : null;
    }
}
