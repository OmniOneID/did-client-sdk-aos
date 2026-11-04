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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class ParsedCredential {

    private final String credentialId;
    private final String format;
    private final String typeIdentifier;
    private final String issuer;
    private final Set<String> availableClaimNames;
    private final Map<String, String> claimAliases;

    public ParsedCredential(String credentialId,
                            String format,
                            String typeIdentifier,
                            String issuer,
                            Set<String> availableClaimNames) {
        this(credentialId, format, typeIdentifier, issuer, availableClaimNames, null);
    }

    public ParsedCredential(String credentialId,
                            String format,
                            String typeIdentifier,
                            String issuer,
                            Set<String> availableClaimNames,
                            Map<String, String> claimAliases) {
        this.credentialId = credentialId;
        this.format = format;
        this.typeIdentifier = typeIdentifier;
        this.issuer = issuer;
        this.availableClaimNames = availableClaimNames == null
                ? Collections.emptySet()
                : Collections.unmodifiableSet(new LinkedHashSet<>(availableClaimNames));
        this.claimAliases = claimAliases == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<>(claimAliases));
    }

    public String credentialId() { return credentialId; }
    public String format() { return format; }
    public String typeIdentifier() { return typeIdentifier; }
    public String issuer() { return issuer; }
    public Set<String> availableClaimNames() { return availableClaimNames; }

    public String resolveClaim(String claimName) {
        if (claimName == null) {
            return null;
        }
        if (availableClaimNames.contains(claimName)) {
            return claimName;
        }
        return claimAliases.get(claimName);
    }

    public boolean hasClaim(String claimName) {
        return resolveClaim(claimName) != null;
    }
}
