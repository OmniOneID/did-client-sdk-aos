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

import java.util.List;
import java.util.Map;

public final class MdocRequestedDocument {

    private final int docRequestIndex;

    private final String docType;

    private final String credentialId;

    private final List<String> claimCodes;

    private final Map<String, Boolean> intentToRetain;

    private final List<String> missing;

    public MdocRequestedDocument(int docRequestIndex,
                                 String docType,
                                 String credentialId,
                                 List<String> claimCodes,
                                 Map<String, Boolean> intentToRetain,
                                 List<String> missing) {
        this.docRequestIndex = docRequestIndex;
        this.docType = docType;
        this.credentialId = credentialId;
        this.claimCodes = claimCodes;
        this.intentToRetain = intentToRetain;
        this.missing = missing;
    }

    public int getDocRequestIndex() {
        return docRequestIndex;
    }

    public String getDocType() {
        return docType;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public List<String> getClaimCodes() {
        return claimCodes;
    }

    public Map<String, Boolean> getIntentToRetain() {
        return intentToRetain;
    }

    public List<String> getMissing() {
        return missing;
    }
}
