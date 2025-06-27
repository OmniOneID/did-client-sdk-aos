/*
 * Copyright 2024-2025 OmniOne.
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

package org.omnione.did.sdk.wallet.walletservice.config;

public class Config {
    public final static String API_GATEWAY_GET_DID_DOC = "/api-gateway/api/v1/did-doc?did=";
    public final static String TAS_GET_ALLOWED_CA = "/list/api/v1/allowed-ca/list?wallet=";
    public final static String WALLET_PKG_NAME = "org.omnione.did.sdk.wallet";
    public final static String DID_METHOD = "omn";
    public final static String DID_CONTROLLER = "did:omn:tas";

}
