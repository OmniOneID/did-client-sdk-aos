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

import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwt;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwtCredentialItem;

import java.util.LinkedHashSet;
import java.util.Set;

public final class SDJWTCredentialAdapter {

    private SDJWTCredentialAdapter() {}

    public static ParsedCredential adapt(SdJwtCredentialItem item) {
        SdJwt sdjwt = item.getSdjwt();
        CredentialFormat format = item.getFormat();
        String formatValue = (format != null) ? format.getValue() : null;
        Set<String> claimNames = new LinkedHashSet<>(sdjwt.getDisclosedClaims().keySet());
        return new ParsedCredential(
                item.getId(),
                formatValue,
                sdjwt.getVct(),
                sdjwt.getIssuer(),
                claimNames,
                sdjwt);
    }
}
