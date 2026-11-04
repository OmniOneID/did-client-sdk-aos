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

package org.omnione.did.sdk.core.oid4vc.net;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.communication.urlconnection.HttpUrlConnectionTask;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public final class RevocationRequester {

    @NonNull private final HttpUrlConnectionTask http;

    public RevocationRequester(@NonNull HttpUrlConnectionTask http) {
        this.http = http;
    }

    @NonNull
    public CompletableFuture<String> request(@NonNull String revocationEndpoint, @NonNull String revocationToken) {
        JsonObject body = new JsonObject();
        body.addProperty("revocation_token", revocationToken);
        return CompletableFuture.supplyAsync(() -> {
            try {
                return http.makeHttpRequest(revocationEndpoint, "POST", GsonWrapper.getGson().toJson(body), null);
            } catch (CommunicationException e) {
                throw new CompletionException(e);
            }
        });
    }
}
