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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClientMetadata {

    @SerializedName("client_name")
    @Expose
    private String clientName;

    @SerializedName("jwks")
    @Expose
    private JsonObject jwks;

    @SerializedName("encrypted_response_enc_values_supported")
    @Expose
    private List<String> encryptedResponseEncValuesSupported;

    @SerializedName("authorization_encrypted_response_alg")
    @Expose
    private String authorizationEncryptedResponseAlg;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public JsonObject getJwks() {
        return jwks;
    }

    public void setJwks(JsonObject jwks) {
        this.jwks = jwks;
    }

    public List<String> getEncryptedResponseEncValuesSupported() {
        return encryptedResponseEncValuesSupported;
    }

    public void setEncryptedResponseEncValuesSupported(List<String> encryptedResponseEncValuesSupported) {
        this.encryptedResponseEncValuesSupported = encryptedResponseEncValuesSupported;
    }

    public String getAuthorizationEncryptedResponseAlg() {
        return authorizationEncryptedResponseAlg;
    }

    public void setAuthorizationEncryptedResponseAlg(String authorizationEncryptedResponseAlg) {
        this.authorizationEncryptedResponseAlg = authorizationEncryptedResponseAlg;
    }

    public List<JsonObject> jwkKeys() {
        List<JsonObject> keys = new ArrayList<>();
        if (jwks == null || !jwks.has("keys") || !jwks.get("keys").isJsonArray()) {
            return keys;
        }
        JsonArray arr = jwks.getAsJsonArray("keys");
        for (JsonElement el : arr) {
            if (el != null && el.isJsonObject()) {
                keys.add(el.getAsJsonObject());
            }
        }
        return keys;
    }
}
