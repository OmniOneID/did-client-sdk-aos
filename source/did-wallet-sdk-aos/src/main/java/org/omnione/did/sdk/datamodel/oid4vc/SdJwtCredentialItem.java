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

public final class SdJwtCredentialItem {

    private final String id;
    private final CredentialFormat format;
    private final String configurationId;
    private final String kid;
    private final String credentialIdentifier;
    private final SdJwt sdjwt;

    public SdJwtCredentialItem(String id, CredentialFormat format, String configurationId,
                               String kid, String credentialIdentifier, SdJwt sdjwt) {
        this.id = id;
        this.format = format;
        this.configurationId = configurationId;
        this.kid = kid;
        this.credentialIdentifier = credentialIdentifier;
        this.sdjwt = sdjwt;
    }

    public String getId() { return id; }
    public CredentialFormat getFormat() { return format; }
    public String getConfigurationId() { return configurationId; }
    public String getKid() { return kid; }
    public String getCredentialIdentifier() { return credentialIdentifier; }
    public SdJwt getSdjwt() { return sdjwt; }
}