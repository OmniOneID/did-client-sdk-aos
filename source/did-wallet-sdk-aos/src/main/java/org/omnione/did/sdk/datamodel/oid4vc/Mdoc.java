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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Mdoc {
    private final String compact;
    private final String docType;
    private final Map<String, Map<String, Object>> nameSpaces;
    private final MdocMso mso;

    public Mdoc(String compact, String docType,
                Map<String, Map<String, Object>> nameSpaces, MdocMso mso) {
        this.compact = compact;
        this.docType = docType;
        Map<String, Map<String, Object>> copy = new LinkedHashMap<>();
        if (nameSpaces != null) {
            for (Map.Entry<String, Map<String, Object>> entry : nameSpaces.entrySet()) {
                copy.put(entry.getKey(), Collections.unmodifiableMap(new LinkedHashMap<>(entry.getValue())));
            }
        }
        this.nameSpaces = Collections.unmodifiableMap(copy);
        this.mso = mso;
    }

    public String getCompact() {
        return compact;
    }

    public String getDocType() {
        return docType;
    }

    public Map<String, Map<String, Object>> getNameSpaces() {
        return nameSpaces;
    }

    public Map<String, Object> getFlatClaims() {
        Map<String, Object> flat = new LinkedHashMap<>();
        for (Map<String, Object> elements : nameSpaces.values()) {
            for (Map.Entry<String, Object> element : elements.entrySet()) {
                if (!flat.containsKey(element.getKey())) {
                    flat.put(element.getKey(), element.getValue());
                }
            }
        }
        return Collections.unmodifiableMap(flat);
    }

    public MdocMso getMso() {
        return mso;
    }
}
