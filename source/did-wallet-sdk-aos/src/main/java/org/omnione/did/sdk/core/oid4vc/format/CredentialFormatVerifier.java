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

package org.omnione.did.sdk.core.oid4vc.format;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocValidator;
import org.omnione.did.sdk.core.oid4vc.format.sdjwt.SdJwtParser;
import org.omnione.did.sdk.core.oid4vc.format.sdjwt.SdJwtValidator;
import org.omnione.did.sdk.core.oid4vc.model.CredentialConfigDescriptor;
import org.omnione.did.sdk.core.oid4vc.verify.IssuerSignatureVerifier;
import org.omnione.did.sdk.core.oid4vc.verify.MdocIssuerAuthVerifier;

import java.util.concurrent.CompletableFuture;

public final class CredentialFormatVerifier {
    private CredentialFormatVerifier() {}

    @NonNull
    public static CompletableFuture<Void> verifyIssued(@NonNull String compact,
                                                       @NonNull CredentialConfigDescriptor config,
                                                       @NonNull String apiGatewayUrl,
                                                       JsonObject holderJwk) throws WalletCoreException {
        if (config.isSdJwtVc()) {
            SdJwtValidator.validateFormat(compact);
            SdJwtParser.parse(compact);
            return IssuerSignatureVerifier.verify(compact, apiGatewayUrl);
        }
        if (config.isMdoc()) {
            MdocValidator.validateFormat(compact);
            return MdocIssuerAuthVerifier.verify(compact, apiGatewayUrl, holderJwk, config.getDoctype());
        }
        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_FORMAT,
                config.getFormat());
    }
}
