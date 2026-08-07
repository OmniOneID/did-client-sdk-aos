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

package org.omnione.did.sdk.core.oid4vc.presentation.format;

import org.omnione.did.sdk.datamodel.oid4vc.SdJwt;

import java.util.Collections;
import java.util.Set;

public final class ParsedCredential {

    private final String credentialId;
    private final String format;
    private final String vct;
    private final String issuer;
    private final Set<String> availableClaimNames;
    private final SdJwt raw;

    public ParsedCredential(String credentialId,
                            String format,
                            String vct,
                            String issuer,
                            Set<String> availableClaimNames,
                            SdJwt raw) {
        this.credentialId = credentialId;
        this.format = format;
        this.vct = vct;
        this.issuer = issuer;
        this.availableClaimNames = availableClaimNames == null
                ? Collections.emptySet()
                : Collections.unmodifiableSet(availableClaimNames);
        this.raw = raw;
    }

    public String credentialId() { return credentialId; }
    public String format() { return format; }
    public String vct() { return vct; }
    public String issuer() { return issuer; }
    public Set<String> availableClaimNames() { return availableClaimNames; }
    public SdJwt raw() { return raw; }

    public boolean hasClaim(String claimName) {
        return availableClaimNames.contains(claimName);
    }
}
