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
import com.upokecenter.cbor.CBORType;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.verify.CoseSign1Verifier;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.Mdoc;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.MdocMso;
import org.omnione.did.sdk.datamodel.oid4vc.MdocValidityInfo;
import org.omnione.did.sdk.datamodel.oid4vc.OID4VCICredential;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MdocParser {
    private MdocParser() {}

    @NonNull
    public static Mdoc parse(@NonNull String compact) throws WalletCoreException {
        CBORObject issuerSigned = MdocCborReader.toIssuerSigned(MdocCborReader.decode(compact));
        CBORObject issuerAuth = MdocCborReader.issuerAuth(issuerSigned);
        CBORObject nameSpaces = MdocCborReader.nameSpaces(issuerSigned);
        CBORObject mso = MdocCborReader.mso(issuerAuth);

        String kid = null;
        try {
            kid = CoseSign1Verifier.kid(issuerAuth);
        } catch (WalletCoreException noKid) {
            WalletLogger.getInstance().d("MdocParser: issuerAuth has no kid : " + noKid.getMessage());
        }

        MdocMso parsedMso = new MdocMso(
                textOrNull(mso.get(MdocConstants.Mso.VERSION)),
                textOrNull(mso.get(MdocConstants.Mso.DIGEST_ALGORITHM)),
                textOrNull(mso.get(MdocConstants.Mso.DOC_TYPE)),
                kid,
                parseValidityInfo(mso.get(MdocConstants.Mso.VALIDITY_INFO)));

        return new Mdoc(compact, parsedMso.getDocType(), parseNameSpaces(nameSpaces), parsedMso);
    }

    @NonNull
    public static List<MdocCredentialItem> mapToMdocItems(List<OID4VCICredential> stored)
            throws WalletCoreException {
        List<MdocCredentialItem> items = new ArrayList<>();
        if (stored == null) {
            return items;
        }
        for (OID4VCICredential c : stored) {
            if (!CredentialFormat.MSO_MDOC.matches(c.getFormat())) {
                continue;
            }
            items.add(new MdocCredentialItem(c.getId(), CredentialFormat.fromValue(c.getFormat()),
                    c.getCredentialConfigurationId(), c.getKid(), c.getCredentialIdentifier(),
                    parse(c.getCredential())));
        }
        return items;
    }

    @NonNull
    private static Map<String, Map<String, Object>> parseNameSpaces(@NonNull CBORObject nameSpaces) {
        Map<String, Map<String, Object>> result = new LinkedHashMap<>();
        for (CBORObject nsKey : nameSpaces.getKeys()) {
            CBORObject entries = nameSpaces.get(nsKey);
            if (entries.getType() != CBORType.Array) {
                continue;
            }
            Map<String, Object> elements = new LinkedHashMap<>();
            for (int i = 0; i < entries.size(); i++) {
                CBORObject item = CBORObject.DecodeFromBytes(
                        MdocCborReader.issuerSignedItemBytes(entries.get(i)));
                if (item.getType() != CBORType.Map) {
                    continue;
                }
                String elementId = textOrNull(item.get(MdocConstants.IssuerSignedItem.ELEMENT_IDENTIFIER));
                if (elementId == null) {
                    continue;
                }
                elements.put(elementId,
                        MdocCborReader.toJava(item.get(MdocConstants.IssuerSignedItem.ELEMENT_VALUE)));
            }
            result.put(MdocCborReader.keyToString(nsKey), elements);
        }
        return result;
    }

    private static MdocValidityInfo parseValidityInfo(CBORObject validityInfo) {
        if (validityInfo == null || validityInfo.getType() != CBORType.Map) {
            return new MdocValidityInfo(null, null, null, null);
        }
        return new MdocValidityInfo(
                MdocTdate.toEpochSeconds(validityInfo.get(MdocConstants.ValidityInfo.SIGNED)),
                MdocTdate.toEpochSeconds(validityInfo.get(MdocConstants.ValidityInfo.VALID_FROM)),
                MdocTdate.toEpochSeconds(validityInfo.get(MdocConstants.ValidityInfo.VALID_UNTIL)),
                MdocTdate.toEpochSeconds(validityInfo.get(MdocConstants.ValidityInfo.EXPECTED_UPDATE)));
    }

    private static String textOrNull(CBORObject value) {
        return value != null && value.getType() == CBORType.TextString ? value.AsString() : null;
    }
}
