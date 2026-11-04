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

import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocParser;
import org.omnione.did.sdk.core.oid4vc.format.sdjwt.SdJwtParser;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.OID4VCCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.OID4VCICredential;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwtCredentialItem;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.util.ArrayList;
import java.util.List;

public final class OID4VCItemMapper {
    private OID4VCItemMapper() {}

    @NonNull
    public static List<OID4VCCredentialItem> map(List<OID4VCICredential> stored) {
        List<OID4VCCredentialItem> items = new ArrayList<>();
        if (stored == null) {
            return items;
        }
        for (OID4VCICredential c : stored) {
            try {
                if (CredentialFormat.SD_JWT_VC.matches(c.getFormat())) {
                    items.add(new SdJwtCredentialItem(c.getId(), CredentialFormat.fromValue(c.getFormat()),
                            c.getCredentialConfigurationId(), c.getKid(), c.getCredentialIdentifier(),
                            SdJwtParser.parse(c.getCredential())));
                } else if (CredentialFormat.MSO_MDOC.matches(c.getFormat())) {
                    items.add(new MdocCredentialItem(c.getId(), CredentialFormat.fromValue(c.getFormat()),
                            c.getCredentialConfigurationId(), c.getKid(), c.getCredentialIdentifier(),
                            MdocParser.parse(c.getCredential())));
                } else {
                    WalletLogger.getInstance().d("OID4VCItemMapper: skipping unsupported format "
                            + c.getFormat() + " (id=" + c.getId() + ")");
                }
            } catch (WalletCoreException e) {
                WalletLogger.getInstance().e("OID4VCItemMapper: skipping unparsable credential id="
                        + c.getId() + " format=" + c.getFormat() + " : " + e.getMessage());
            }
        }
        return items;
    }

    @NonNull
    public static List<SdJwtCredentialItem> mapSdJwtOnly(List<OID4VCICredential> stored)
            throws WalletCoreException {
        return SdJwtParser.mapToSdJwtItems(stored);
    }
}
