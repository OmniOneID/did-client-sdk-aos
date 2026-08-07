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

package org.omnione.did.sdk.core.oid4vc.net;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.communication.urlconnection.HttpUrlConnectionTask;
import org.omnione.did.sdk.core.oid4vc.errors.Oid4vciException;
import org.omnione.did.sdk.core.oid4vc.model.TokenResponse;
import org.omnione.did.sdk.core.oid4vc.net.wire.ErrorResponseWire;
import org.omnione.did.sdk.core.oid4vc.net.wire.TokenResponseWire;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TokenRequester {

    private static final String GRANT_PRE_AUTH =
            "urn:ietf:params:oauth:grant-type:pre-authorized_code";
    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";

    @NonNull private final HttpUrlConnectionTask http;

    public TokenRequester(@NonNull HttpUrlConnectionTask http) {
        this.http = http;
    }

    public TokenResponse request(@NonNull String tokenEndpoint,
                                        @NonNull String preAuthorizedCode,
                                        @Nullable String txCode) throws CommunicationException {
        return request(tokenEndpoint, preAuthorizedCode, txCode, Collections.emptyList());
    }

    public TokenResponse request(@NonNull String tokenEndpoint,
                                        @NonNull String preAuthorizedCode,
                                        @Nullable String txCode,
                                        @NonNull List<String> credentialConfigurationIds) throws CommunicationException {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("grant_type", GRANT_PRE_AUTH);
        form.put("pre-authorized_code", preAuthorizedCode);
        if (txCode != null) {
            form.put("tx_code", txCode);
        }
        if (!credentialConfigurationIds.isEmpty()) {
            form.put("authorization_details", buildAuthorizationDetailsJson(credentialConfigurationIds));
        }

        String responseBody;
        try {
            responseBody = http.makeHttpRequest(tokenEndpoint, "POST", encodeForm(form), null, FORM_CONTENT_TYPE);
        } catch (CommunicationException e) {

            ErrorResponseWire err = tryParseError(extractErrorBody(e));
            if (err != null && err.error != null && isInvalidGrant(err.error)) {
                throw new Oid4vciException(err.errorDescription == null ? "invalid_grant" : err.errorDescription);
            }
            throw e;
        }

        TokenResponseWire wire = GsonWrapper.getGson().fromJson(responseBody, TokenResponseWire.class);
        if (wire == null || wire.accessToken == null) {
            throw new Oid4vciException("Token response missing access_token");
        }

        return new TokenResponse(
                wire.accessToken,
                wire.tokenType,
                wire.expiresIn,
                wire.cNonce,
                wire.cNonceExpiresIn);
    }

    @NonNull
    private static String encodeForm(@NonNull Map<String, String> form) {
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> entry : form.entrySet()) {
            if (body.length() > 0) body.append('&');
            body.append(urlEncode(entry.getKey())).append('=').append(urlEncode(entry.getValue()));
        }
        return body.toString();
    }

    @NonNull
    private static String urlEncode(@NonNull String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return value;
        }
    }

    @NonNull
    private static String extractErrorBody(@NonNull CommunicationException e) {
        String msg = e.getErrMsg();
        if (msg == null) return "";
        int brace = msg.indexOf('{');
        return brace >= 0 ? msg.substring(brace) : msg;
    }

    @NonNull
    private static String buildAuthorizationDetailsJson(@NonNull List<String> configurationIds) {
        JsonArray arr = new JsonArray();
        for (String id : configurationIds) {
            if (id == null || id.isEmpty()) continue;
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "openid_credential");
            obj.addProperty("credential_configuration_id", id);
            arr.add(obj);
        }
        return GsonWrapper.getGson().toJson(arr);
    }

    private static boolean isInvalidGrant(@NonNull String error) {
        return "invalid_grant".equalsIgnoreCase(error)
                || "invalid_pin".equalsIgnoreCase(error)
                || "invalid_tx_code".equalsIgnoreCase(error);
    }

    @Nullable
    private static ErrorResponseWire tryParseError(@NonNull String body) {
        try {
            return GsonWrapper.getGson().fromJson(body, ErrorResponseWire.class);
        } catch (Exception e) {
            return null;
        }
    }
}
