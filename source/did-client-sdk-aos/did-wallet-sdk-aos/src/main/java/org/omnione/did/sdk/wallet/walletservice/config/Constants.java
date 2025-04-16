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

public class Constants {
    public final static String TITLE = "Mobile Driver's License";

    // DID Doc Type (1: device key, 2 : holder)
    public final static int DID_DOC_TYPE_DEVICE = 1;
    public final static int DID_DOC_TYPE_HOLDER = 2;

    // KEY ID
    public final static String KEY_ID_ASSERT = "assert";
    public final static String KEY_ID_AUTH = "auth";
    public final static String KEY_ID_KEY_AGREE = "keyagree";
    public final static String KEY_ID_PIN = "pin";
    public final static String KEY_ID_BIO = "bio";

    // wallet name
    public final static String WALLET_DEVICE = "device";
    public final static String WALLET_HOLDER = "holder";
}
