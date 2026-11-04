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

package org.omnione.did.sdk.core.oid4vc.format.mdoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MdocClaimCodes {

    public static final String SEPARATOR = "/";

    private MdocClaimCodes() {}

    @NonNull
    public static String of(@NonNull String nameSpace, @NonNull String elementIdentifier) {
        return nameSpace + SEPARATOR + elementIdentifier;
    }

    @Nullable
    public static String nameSpaceOf(@Nullable String claimCode) {
        int cut = (claimCode != null) ? claimCode.indexOf(SEPARATOR) : -1;
        return cut > 0 ? claimCode.substring(0, cut) : null;
    }

    @Nullable
    public static String elementIdentifierOf(@Nullable String claimCode) {
        int cut = (claimCode != null) ? claimCode.indexOf(SEPARATOR) : -1;
        return cut > 0 && cut < claimCode.length() - 1 ? claimCode.substring(cut + 1) : null;
    }

    @NonNull
    public static String stripNameSpacePrefix(@NonNull String nameSpace,
                                              @NonNull String elementIdentifier) {
        String prefix = nameSpace + ".";
        return elementIdentifier.startsWith(prefix) && elementIdentifier.length() > prefix.length()
                ? elementIdentifier.substring(prefix.length())
                : elementIdentifier;
    }

    @NonNull
    public static Map<String, Set<String>> toRequestedElements(@Nullable List<String> claimCodes) {
        Map<String, Set<String>> requested = new LinkedHashMap<>();
        if (claimCodes == null) {
            return requested;
        }
        for (String code : claimCodes) {
            String nameSpace = nameSpaceOf(code);
            String elementIdentifier = elementIdentifierOf(code);
            if (nameSpace == null || elementIdentifier == null) {
                continue;
            }
            requested.computeIfAbsent(nameSpace, k -> new LinkedHashSet<>()).add(elementIdentifier);
        }
        return requested;
    }
}
