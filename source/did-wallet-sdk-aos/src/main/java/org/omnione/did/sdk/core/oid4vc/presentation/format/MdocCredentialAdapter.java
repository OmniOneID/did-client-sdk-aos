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

import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocClaimCodes;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.Mdoc;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.MdocMso;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class MdocCredentialAdapter {

    private MdocCredentialAdapter() {}

    public static ParsedCredential adapt(MdocCredentialItem item) {
        Mdoc mdoc = item.getMdoc();
        CredentialFormat format = item.getFormat();
        String formatValue = (format != null) ? format.getValue() : null;

        Set<String> claimCodes = new LinkedHashSet<>();
        Map<String, String> aliases = new LinkedHashMap<>();
        if (mdoc != null) {
            for (Map.Entry<String, Map<String, Object>> entry : mdoc.getNameSpaces().entrySet()) {
                String nameSpace = entry.getKey();
                for (String elementIdentifier : entry.getValue().keySet()) {
                    String code = MdocClaimCodes.of(nameSpace, elementIdentifier);
                    claimCodes.add(code);

                    String bare = MdocClaimCodes.stripNameSpacePrefix(nameSpace, elementIdentifier);
                    if (!bare.equals(elementIdentifier)) {
                        aliases.put(MdocClaimCodes.of(nameSpace, bare), code);
                    }
                }
            }
        }

        MdocMso mso = (mdoc != null) ? mdoc.getMso() : null;
        String docType = (mso != null && mso.getDocType() != null)
                ? mso.getDocType()
                : (mdoc != null ? mdoc.getDocType() : null);

        return new ParsedCredential(
                item.getId(),
                formatValue,
                docType,
                (mso != null) ? mso.getIssuerDid() : null,
                claimCodes,
                aliases);
    }
}
