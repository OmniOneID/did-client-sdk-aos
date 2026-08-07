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

package org.omnione.did.sdk.core.oid4vc.format.sdjwt;

import androidx.annotation.NonNull;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;

public final class SdJwtValidator {

    private SdJwtValidator() {}

    public static void validateFormat(@NonNull String compact) throws WalletCoreException {
        if (compact.isEmpty()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_FORMAT,
                    "SD-JWT compact form is empty");
        }
        String jwtPart = compact;
        int firstTilde = compact.indexOf('~');
        if (firstTilde >= 0) {
            jwtPart = compact.substring(0, firstTilde);
        }
        int dots = 0;
        for (int i = 0; i < jwtPart.length(); i++) {
            if (jwtPart.charAt(i) == '.') dots++;
        }
        if (dots != 2) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_FORMAT,
                    "SD-JWT VC must contain a JWS (header.payload.signature)");
        }
    }
}
