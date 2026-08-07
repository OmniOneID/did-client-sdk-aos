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

public final class TokenResponse {

    @NonNull private final String accessToken;
    @Nullable private final String tokenType;
    @Nullable private final Integer expiresIn;
    @Nullable private final String cNonce;
    @Nullable private final Integer cNonceExpiresIn;

    public TokenResponse(@NonNull String accessToken,
                                @Nullable String tokenType,
                                @Nullable Integer expiresIn,
                                @Nullable String cNonce,
                                @Nullable Integer cNonceExpiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.cNonce = cNonce;
        this.cNonceExpiresIn = cNonceExpiresIn;
    }

    @NonNull public String getAccessToken() { return accessToken; }
    @Nullable public String getTokenType() { return tokenType; }
    @Nullable public Integer getExpiresIn() { return expiresIn; }
    @Nullable public String getCNonce() { return cNonce; }
    @Nullable public Integer getCNonceExpiresIn() { return cNonceExpiresIn; }
}