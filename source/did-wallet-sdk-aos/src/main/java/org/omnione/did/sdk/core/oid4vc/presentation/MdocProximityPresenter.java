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

package org.omnione.did.sdk.core.oid4vc.presentation;

import androidx.annotation.NonNull;

import com.upokecenter.cbor.CBORObject;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.MdocDeviceAuthMethod;
import org.omnione.did.sdk.datamodel.oid4vc.MdocDeviceResponse;
import org.omnione.did.sdk.datamodel.oid4vc.MdocRequestedDocument;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MdocProximityPresenter {

    private MdocProximityPresenter() {}

    @NonNull
    public static MdocDeviceResponse createDeviceResponse(@NonNull List<MdocRequestedDocument> selected,
                                                          @NonNull Map<String, MdocCredentialItem> storedById,
                                                          @NonNull Map<String, SignDigest> signersById,
                                                          @NonNull byte[] sessionTranscript)
            throws WalletException, WalletCoreException, UtilityException {
        byte[] transcript = MdocPresenter.resolveSessionTranscript(sessionTranscript);

        Map<String, CBORObject> deviceSignatures = new HashMap<>();
        Map<String, MdocDeviceAuthMethod> authMethods = new LinkedHashMap<>();
        List<CBORObject> documents = new ArrayList<>(selected.size());
        for (MdocRequestedDocument s : selected) {
            String credentialId = s.getCredentialId();
            MdocCredentialItem item = storedById.get(credentialId);
            if (item == null || item.getMdoc() == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS,
                        "document '" + credentialId + "' is not stored");
            }
            String docType = MdocPresenter.docTypeOf(item.getMdoc());

            CBORObject deviceSignature = deviceSignatures.get(credentialId);
            if (deviceSignature == null) {
                SignDigest signer = signersById.get(credentialId);
                if (signer == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VP_HOLDER_KEY_NOT_FOUND, credentialId);
                }
                deviceSignature = MdocPresenter.createDeviceSignature(transcript, docType, signer);
                deviceSignatures.put(credentialId, deviceSignature);
                authMethods.put(credentialId, MdocDeviceAuthMethod.DEVICE_SIGNATURE);
            }

            documents.add(MdocPresenter.buildDocument(item.getMdoc(), s.getClaimCodes(), deviceSignature,
                    WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS));
        }

        return new MdocDeviceResponse(MdocPresenter.encodeDeviceResponse(documents),
                Collections.unmodifiableMap(authMethods));
    }
}
