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

package org.omnione.did.sdk.core.oid4vc.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public final class CredentialOfferResponse {

    @NonNull private final String credentialIssuer;
    @NonNull private final List<String> credentialConfigurationIds;
    @Nullable private final String preAuthorizedCode;
    @Nullable private final TxCodeSpec txCodeSpec;

    public CredentialOfferResponse(@NonNull String credentialIssuer,
                                  @NonNull List<String> credentialConfigurationIds,
                                  @Nullable String preAuthorizedCode,
                                  @Nullable TxCodeSpec txCodeSpec) {
        this.credentialIssuer = credentialIssuer;
        this.credentialConfigurationIds = Collections.unmodifiableList(credentialConfigurationIds);
        this.preAuthorizedCode = preAuthorizedCode;
        this.txCodeSpec = txCodeSpec;
    }

    @NonNull public String getCredentialIssuer() { return credentialIssuer; }
    @NonNull public List<String> getCredentialConfigurationIds() { return credentialConfigurationIds; }
    @Nullable public String getPreAuthorizedCode() { return preAuthorizedCode; }
    @Nullable public TxCodeSpec getTxCodeSpec() { return txCodeSpec; }

    public boolean hasPreAuthorizedCode() { return preAuthorizedCode != null; }
}
