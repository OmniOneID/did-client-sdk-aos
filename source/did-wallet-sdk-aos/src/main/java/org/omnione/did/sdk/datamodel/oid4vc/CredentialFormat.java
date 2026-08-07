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

public enum CredentialFormat {
    OPENDID_VC("opendid_vc"),
    SD_JWT_VC("dc+sd-jwt-did"),
    MSO_MDOC("mso_mdoc-did");

    private final String value;

    CredentialFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean matches(String rawFormat) {
        return value.equals(rawFormat);
    }

    public static CredentialFormat fromValue(String value) {
        for (CredentialFormat format : values()) {
            if (format.value.equals(value)) {
                return format;
            }
        }
        return null;
    }
}
