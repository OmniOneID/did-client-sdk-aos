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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncryptedResponseSubmission {

    private final String response;

    public EncryptedResponseSubmission(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public String toFormData() {
        try {
            return "response=" + URLEncoder.encode(response, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
