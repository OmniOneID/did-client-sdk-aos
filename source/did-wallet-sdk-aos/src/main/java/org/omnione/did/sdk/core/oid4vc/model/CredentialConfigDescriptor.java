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

import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;

public final class CredentialConfigDescriptor {

    @NonNull private final String configurationId;
    @NonNull private final String format;
    @Nullable private final String vct;
    @Nullable private final String doctype;

    public CredentialConfigDescriptor(@NonNull String configurationId,
                                      @NonNull String format,
                                      @Nullable String vct,
                                      @Nullable String doctype) {
        this.configurationId = configurationId;
        this.format = format;
        this.vct = vct;
        this.doctype = doctype;
    }

    @NonNull public String getConfigurationId() { return configurationId; }
    @NonNull public String getFormat() { return format; }
    @Nullable public String getVct() { return vct; }
    @Nullable public String getDoctype() { return doctype; }

    public boolean isSdJwtVc() {
        return CredentialFormat.SD_JWT_VC.matches(format);
    }
    public boolean isMdoc() { return CredentialFormat.MSO_MDOC.matches(format); }
}
