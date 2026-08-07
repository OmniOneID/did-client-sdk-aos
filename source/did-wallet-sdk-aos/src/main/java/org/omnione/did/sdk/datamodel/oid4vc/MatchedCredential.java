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

public class MatchedCredential {

    private final String queryId;

    private final String credentialId;

    private final List<String> claimCodes;

    public MatchedCredential(String queryId,
                             String credentialId,
                             List<String> claimCodes) {
        this.queryId = queryId;
        this.credentialId = credentialId;
        this.claimCodes = claimCodes;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public List<String> getClaimCodes() {
        return claimCodes;
    }
}
