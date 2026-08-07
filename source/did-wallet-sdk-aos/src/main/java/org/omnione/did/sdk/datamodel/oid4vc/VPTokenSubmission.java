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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class VPTokenSubmission {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final Map<String, Object> vpToken;
    private final String state;

    public VPTokenSubmission(Map<String, Object> vpToken, String state) {
        this.vpToken = vpToken;
        this.state = state;
    }

    public Map<String, Object> getVpToken() {
        return vpToken;
    }

    public String getState() {
        return state;
    }

    public byte[] toJsonData() {
        return GSON.toJson(new Body(vpToken, state)).getBytes(StandardCharsets.UTF_8);
    }

    public String toFormData() {
        StringBuilder sb = new StringBuilder();
        sb.append("vp_token=").append(urlEncode(GSON.toJson(vpToken)));
        if (state != null) {
            sb.append("&state=").append(urlEncode(state));
        }
        return sb.toString();
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final class Body {
        private final Map<String, Object> vp_token;
        private final String state;

        Body(Map<String, Object> vpToken, String state) {
            this.vp_token = vpToken;
            this.state = state;
        }
    }
}
