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
import androidx.annotation.Nullable;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocClaimCodes;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocDeviceRequestDecoder;
import org.omnione.did.sdk.datamodel.oid4vc.Mdoc;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.MdocRequestedDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MdocProximityMatcher {

    private MdocProximityMatcher() {}

    @NonNull
    public static List<MdocRequestedDocument> match(@Nullable byte[] deviceRequest,
                                                    @Nullable List<MdocCredentialItem> stored)
            throws WalletCoreException {
        List<MdocDeviceRequestDecoder.DocRequest> docRequests = MdocDeviceRequestDecoder.decode(deviceRequest);

        List<MdocRequestedDocument> result = new ArrayList<>();
        for (int index = 0; index < docRequests.size(); index++) {
            MdocDeviceRequestDecoder.DocRequest docRequest = docRequests.get(index);
            List<MdocRequestedDocument> candidates = new ArrayList<>();
            if (stored != null) {
                for (MdocCredentialItem item : stored) {
                    MdocRequestedDocument candidate = matchOne(index, docRequest, item);
                    if (candidate != null) {
                        candidates.add(candidate);
                    }
                }
            }
            candidates.sort(Comparator.comparing(MdocRequestedDocument::getCredentialId));
            result.addAll(candidates);
        }
        if (result.isEmpty()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_NO_MATCHED_DOCUMENTS);
        }
        return Collections.unmodifiableList(result);
    }

    public static void validateSelection(@NonNull List<MdocRequestedDocument> matched,
                                         @Nullable List<MdocRequestedDocument> selected)
            throws WalletCoreException {
        if (selected == null || selected.isEmpty()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_EMPTY_SELECTION);
        }

        Map<String, MdocRequestedDocument> matchedByKey = new LinkedHashMap<>();
        for (MdocRequestedDocument m : matched) {
            matchedByKey.put(key(m.getDocRequestIndex(), m.getCredentialId()), m);
        }

        Set<String> seen = new HashSet<>();
        for (MdocRequestedDocument s : selected) {
            if (s == null || s.getCredentialId() == null) {
                throw invalidSelection("a selected document is null or has no credentialId");
            }
            String key = key(s.getDocRequestIndex(), s.getCredentialId());
            MdocRequestedDocument m = matchedByKey.get(key);
            if (m == null) {
                throw invalidSelection("document '" + s.getCredentialId() + "' does not answer docRequest "
                        + s.getDocRequestIndex());
            }
            if (m.getDocType() == null || !m.getDocType().equals(s.getDocType())) {
                throw invalidSelection("docType of document '" + s.getCredentialId() + "' was changed");
            }
            if (!seen.add(key)) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_DUPLICATE_SELECTED_DOCUMENT);
            }

            List<String> claimCodes = s.getClaimCodes();
            if (claimCodes == null || claimCodes.isEmpty()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_EMPTY_CLAIM_CODES);
            }
            if (new HashSet<>(claimCodes).size() != claimCodes.size()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_DUPLICATE_CLAIM_CODE);
            }
        }
    }

    @Nullable
    private static MdocRequestedDocument matchOne(int index,
                                                  @NonNull MdocDeviceRequestDecoder.DocRequest docRequest,
                                                  @Nullable MdocCredentialItem item) {
        if (item == null || item.getId() == null) {
            return null;
        }
        Mdoc mdoc = item.getMdoc();
        if (mdoc == null || !docRequest.getDocType().equals(mdoc.getDocType())) {
            return null;
        }

        List<String> claimCodes = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        Map<String, Boolean> intentToRetain = new LinkedHashMap<>();
        Map<String, Map<String, Object>> held = mdoc.getNameSpaces();
        for (Map.Entry<String, Map<String, Boolean>> ns : docRequest.getNameSpaces().entrySet()) {
            Map<String, Object> heldElements = held.get(ns.getKey());
            for (Map.Entry<String, Boolean> element : ns.getValue().entrySet()) {
                String code = MdocClaimCodes.of(ns.getKey(), element.getKey());
                intentToRetain.put(code, element.getValue());
                if (heldElements != null && heldElements.containsKey(element.getKey())) {
                    claimCodes.add(code);
                } else {
                    missing.add(code);
                }
            }
        }
        if (claimCodes.isEmpty()) {
            return null;
        }
        return new MdocRequestedDocument(index, docRequest.getDocType(), item.getId(),
                Collections.unmodifiableList(claimCodes),
                Collections.unmodifiableMap(intentToRetain),
                Collections.unmodifiableList(missing));
    }

    @NonNull
    private static String key(int docRequestIndex, @NonNull String credentialId) {
        return docRequestIndex + ":" + credentialId;
    }

    @NonNull
    private static WalletCoreException invalidSelection(@NonNull String detail) {
        return new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS, detail);
    }
}
