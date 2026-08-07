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

public final class CredentialResponseEncryptionSpec {

    @NonNull private final List<String> algValues;
    @NonNull private final List<String> encValues;
    private final boolean required;

    public CredentialResponseEncryptionSpec(@Nullable List<String> algValues,
                                            @Nullable List<String> encValues,
                                            boolean required) {
        this.algValues = algValues == null
                ? Collections.emptyList() : Collections.unmodifiableList(algValues);
        this.encValues = encValues == null
                ? Collections.emptyList() : Collections.unmodifiableList(encValues);
        this.required = required;
    }

    @NonNull public List<String> getAlgValues() { return algValues; }
    @NonNull public List<String> getEncValues() { return encValues; }
    public boolean isRequired() { return required; }

    @Nullable
    public String getFirstAlg() {
        return algValues.isEmpty() ? null : algValues.get(0);
    }

    @Nullable
    public String getFirstEnc() {
        return encValues.isEmpty() ? null : encValues.get(0);
    }
}
