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
import com.google.gson.JsonPrimitive;
import com.upokecenter.cbor.CBORObject;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocCborReader;
import org.omnione.did.sdk.core.oid4vc.format.sdjwt.SdJwtParser;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.OID4VCICredential;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.util.AbstractMap;
import java.util.Map;

public final class CredentialStatusListReader {

    private static final String STATUS = "status";
    private static final String STATUS_LIST = "status_list";
    private static final String URI = "uri";
    private static final String IDX = "idx";

    private CredentialStatusListReader() {}

    @NonNull
    public static Map.Entry<String, Long> read(@NonNull OID4VCICredential credential) throws WalletException, WalletCoreException {
        String format = credential.getFormat();
        String uri;
        long idx;
        if (CredentialFormat.SD_JWT_VC.matches(format)) {
            try {
                JsonObject statusList = SdJwtParser.parse(credential.getCredential()).getJwtPayload()
                        .getAsJsonObject(STATUS).getAsJsonObject(STATUS_LIST);
                JsonPrimitive uriValue = statusList.getAsJsonPrimitive(URI);
                JsonPrimitive idxValue = statusList.getAsJsonPrimitive(IDX);
                if (!uriValue.isString() || !idxValue.isNumber()) {
                    throw invalid();
                }
                uri = uriValue.getAsString();
                idx = idxValue.getAsBigDecimal().longValueExact();
            } catch (RuntimeException e) {
                throw invalid();
            }
        } else if (CredentialFormat.MSO_MDOC.matches(format)) {
            try {
                CBORObject mso = MdocCborReader.mso(MdocCborReader.issuerAuth(
                        MdocCborReader.toIssuerSigned(MdocCborReader.decode(credential.getCredential()))));
                CBORObject statusList = mso.get(STATUS).get(STATUS_LIST);
                uri = statusList.get(URI).AsString();
                idx = statusList.get(IDX).AsInt64Value();
            } catch (RuntimeException e) {
                throw invalid();
            }
        } else {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_FORMAT, format);
        }
        if (uri.isEmpty() || idx < 0) {
            throw invalid();
        }
        return new AbstractMap.SimpleImmutableEntry<>(uri, idx);
    }

    @NonNull
    private static WalletException invalid() {
        return new WalletException(WalletErrorCode.ERR_CODE_WALLET_VERIFY_PARAMETER_FAIL, STATUS_LIST);
    }
}
