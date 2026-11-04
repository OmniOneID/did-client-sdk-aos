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

package org.omnione.did.sdk.core.oid4vc.format.mdoc;

import androidx.annotation.NonNull;

import com.upokecenter.cbor.CBORObject;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;

public final class MdocValidator {
    private MdocValidator() {}

    public static void validateFormat(@NonNull String compact) throws WalletCoreException {
        if (compact.isEmpty()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_FORMAT,
                    "mdoc credential is empty");
        }
        if (isCborUnavailable()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_FORMAT,
                    "CBOR library not on classpath: the app must declare"
                            + " implementation 'com.upokecenter:cbor' to use the mso_mdoc-did format");
        }
        CBORObject issuerSigned = MdocCborReader.toIssuerSigned(MdocCborReader.decode(compact));
        MdocCborReader.issuerAuth(issuerSigned);
        MdocCborReader.nameSpaces(issuerSigned);
    }

    private static boolean isCborUnavailable() {
        try {
            Class.forName("com.upokecenter.cbor.CBORObject");
            return false;
        } catch (ClassNotFoundException missing) {
            return true;
        }
    }
}
