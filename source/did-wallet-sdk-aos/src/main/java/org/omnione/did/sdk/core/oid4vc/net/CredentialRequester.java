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
import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.communication.urlconnection.HttpUrlConnectionTask;
import org.omnione.did.sdk.core.oid4vc.crypto.CredentialResponseDecryptor;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.model.IssuedCredential;
import org.omnione.did.sdk.core.oid4vc.net.wire.CredentialResponseWire;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public final class CredentialRequester {

    @NonNull private final HttpUrlConnectionTask http;

    public CredentialRequester(@NonNull HttpUrlConnectionTask http) {
        this.http = http;
    }

    public CompletableFuture<IssuedCredential> request(@NonNull String credentialEndpoint,
                                    @NonNull String accessToken,
                                    @NonNull String configurationId,
                                    @Nullable String credentialIdentifier,
                                    @NonNull String format,
                                    @NonNull String proofJwt,
                                    @Nullable CredentialResponseDecryptor decryptor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject body = new JsonObject();
                if (credentialIdentifier != null && !credentialIdentifier.isEmpty()) {
                    body.addProperty("credential_identifier", credentialIdentifier);
                } else {
                    body.addProperty("credential_configuration_id", configurationId);
                }

                JsonObject proofs = new JsonObject();
                JsonArray jwtArray = new JsonArray();
                jwtArray.add(proofJwt);
                proofs.add("jwt", jwtArray);
                body.add("proofs", proofs);

                if (decryptor != null) {
                    body.add("credential_response_encryption", decryptor.requestParams());
                }

                String responseBody = http.makeHttpRequest(
                        credentialEndpoint, "POST", GsonWrapper.getGson().toJson(body), accessToken);

                String responseJson = decryptor != null ? decryptor.decrypt(responseBody) : responseBody;
                CredentialResponseWire wire = GsonWrapper.getGson().fromJson(responseJson, CredentialResponseWire.class);
                if (wire == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                            "Credential response empty");
                }
                String compact = wire.credential;
                if (compact == null && wire.credentials != null && !wire.credentials.isEmpty()
                        && wire.credentials.get(0) != null) {
                    compact = wire.credentials.get(0).credential;
                }
                if (compact == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                            "Credential response missing credential body");
                }

                return new IssuedCredential(format, compact);
            } catch (CommunicationException | WalletCoreException e) {
                throw new CompletionException(e);
            }
        });
    }
}
