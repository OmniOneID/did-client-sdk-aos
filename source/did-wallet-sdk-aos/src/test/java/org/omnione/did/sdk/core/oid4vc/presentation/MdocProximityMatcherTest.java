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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.AGE_OVER_18;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.FAMILY_NAME;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.GIVEN_NAME;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.MDL_DOC_TYPE;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.MDL_NAME_SPACE;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.deviceRequest;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.elements;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.item;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.mdlDocRequest;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.mdlWithGivenAndFamilyName;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.mdoc;

import org.junit.Test;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocClaimCodes;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.MdocRequestedDocument;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MdocProximityMatcherTest {

    private static final String GIVEN = MdocClaimCodes.of(MDL_NAME_SPACE, GIVEN_NAME);
    private static final String FAMILY = MdocClaimCodes.of(MDL_NAME_SPACE, FAMILY_NAME);
    private static final String AGE = MdocClaimCodes.of(MDL_NAME_SPACE, AGE_OVER_18);

    private static List<MdocCredentialItem> twoMdlsAndOneOther() {
        Map<String, String> withAge = new LinkedHashMap<>();
        withAge.put(GIVEN_NAME, "Raon");
        withAge.put(AGE_OVER_18, "true");
        Map<String, String> other = new LinkedHashMap<>();
        other.put("x", "y");
        return Arrays.asList(
                item("cred-b", mdlWithGivenAndFamilyName()),
                item("cred-a", mdoc(MDL_DOC_TYPE, MDL_NAME_SPACE, withAge)),
                item("cred-other", mdoc("org.example.other", "org.example", other)));
    }

    @Test
    public void matchesEveryStoredDocumentOfTheDocTypeSplitsHeldAndMissingAndSortsDeterministically()
            throws Exception {
        byte[] request = deviceRequest("1.0",
                mdlDocRequest(elements(GIVEN_NAME, true, FAMILY_NAME, false, AGE_OVER_18, true)),
                mdlDocRequest(elements(FAMILY_NAME, false)));

        List<MdocRequestedDocument> matched = MdocProximityMatcher.match(request, twoMdlsAndOneOther());

        assertEquals(3, matched.size());

        MdocRequestedDocument first = matched.get(0);
        assertEquals(0, first.getDocRequestIndex());
        assertEquals("cred-a", first.getCredentialId());
        assertEquals(MDL_DOC_TYPE, first.getDocType());
        assertEquals(Arrays.asList(GIVEN, AGE), first.getClaimCodes());
        assertEquals(Collections.singletonList(FAMILY), first.getMissing());
        assertEquals(Arrays.asList(GIVEN, FAMILY, AGE), new java.util.ArrayList<>(first.getIntentToRetain().keySet()));
        assertEquals(Boolean.TRUE, first.getIntentToRetain().get(GIVEN));
        assertEquals(Boolean.FALSE, first.getIntentToRetain().get(FAMILY));
        assertEquals(Boolean.TRUE, first.getIntentToRetain().get(AGE));

        MdocRequestedDocument second = matched.get(1);
        assertEquals(0, second.getDocRequestIndex());
        assertEquals("cred-b", second.getCredentialId());
        assertEquals(Arrays.asList(GIVEN, FAMILY), second.getClaimCodes());
        assertEquals(Collections.singletonList(AGE), second.getMissing());

        MdocRequestedDocument third = matched.get(2);
        assertEquals(1, third.getDocRequestIndex());
        assertEquals("cred-b", third.getCredentialId());
        assertEquals(Collections.singletonList(FAMILY), third.getClaimCodes());
        assertTrue(third.getMissing().isEmpty());
        assertEquals(Collections.singletonMap(FAMILY, Boolean.FALSE), third.getIntentToRetain());
    }

    @Test
    public void docRequestWithoutAnyCandidateIsDroppedAndOthersRemain() throws Exception {
        byte[] request = deviceRequest("1.0",
                mdlDocRequest(elements("not_issued", true)),
                mdlDocRequest(elements(GIVEN_NAME, true)),
                MdocProximityFixtures.docRequest("org.example.unknown",
                        Collections.singletonMap("org.example", elements("x", true))));

        List<MdocRequestedDocument> matched = MdocProximityMatcher.match(request, twoMdlsAndOneOther());

        assertEquals(2, matched.size());
        assertEquals(1, matched.get(0).getDocRequestIndex());
        assertEquals("cred-a", matched.get(0).getCredentialId());
        assertEquals(1, matched.get(1).getDocRequestIndex());
        assertEquals("cred-b", matched.get(1).getCredentialId());
    }

    @Test
    public void throws06600WhenNoDocRequestHasACandidate() {
        byte[] request = deviceRequest("1.0", mdlDocRequest(elements("not_issued", true)));
        try {
            MdocProximityMatcher.match(request, twoMdlsAndOneOther());
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_PROXIMITY_NO_MATCHED_DOCUMENTS.getCode(), e.getErrorCode());
        }
        try {
            MdocProximityMatcher.match(deviceRequest("1.0", mdlDocRequest(elements(GIVEN_NAME, true))),
                    Collections.emptyList());
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_PROXIMITY_NO_MATCHED_DOCUMENTS.getCode(), e.getErrorCode());
        }
    }

    @Test
    public void matchPropagates06620ForUndecodableRequest() {
        try {
            MdocProximityMatcher.match(new byte[] {0x00}, twoMdlsAndOneOther());
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_DEVICE_REQUEST.getCode(), e.getErrorCode());
        }
    }

    private static List<MdocRequestedDocument> matchedForSelectionTests() throws Exception {
        byte[] request = deviceRequest("1.0",
                mdlDocRequest(elements(GIVEN_NAME, true, FAMILY_NAME, false, AGE_OVER_18, true)),
                mdlDocRequest(elements(FAMILY_NAME, false)));
        return MdocProximityMatcher.match(request, twoMdlsAndOneOther());
    }

    private static MdocRequestedDocument selected(int index, String credentialId, String docType, String... codes) {
        return new MdocRequestedDocument(index, docType, credentialId, Arrays.asList(codes), null, null);
    }

    private static void assertSelectionFails(List<MdocRequestedDocument> selected, WalletCoreErrorCode code)
            throws Exception {
        try {
            MdocProximityMatcher.validateSelection(matchedForSelectionTests(), selected);
            fail("expected " + code);
        } catch (WalletCoreException e) {
            assertEquals(code.getCode(), e.getErrorCode());
        }
    }

    @Test
    public void emptySelectionIs06610() throws Exception {
        assertSelectionFails(null, WalletCoreErrorCode.ERR_CODE_PROXIMITY_EMPTY_SELECTION);
        assertSelectionFails(Collections.emptyList(), WalletCoreErrorCode.ERR_CODE_PROXIMITY_EMPTY_SELECTION);
    }

    @Test
    public void documentOutsideRematchIs06611WithDetail() throws Exception {
        assertSelectionFails(Collections.singletonList(selected(2, "cred-b", MDL_DOC_TYPE, FAMILY)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS);
        assertSelectionFails(Collections.singletonList(selected(0, "cred-other", MDL_DOC_TYPE, GIVEN)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS);
        assertSelectionFails(Collections.singletonList(selected(0, "cred-b", "org.example.other", GIVEN)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS);
    }

    @Test
    public void claimCodesOutsideTheRequestAreAcceptedSoTheHolderCanAddOptionalElements() throws Exception {
        List<MdocRequestedDocument> matched = matchedForSelectionTests();

        MdocProximityMatcher.validateSelection(matched,
                Collections.singletonList(selected(1, "cred-b", MDL_DOC_TYPE, FAMILY, GIVEN)));
        MdocProximityMatcher.validateSelection(matched,
                Collections.singletonList(selected(1, "cred-b", MDL_DOC_TYPE, GIVEN)));
        MdocProximityMatcher.validateSelection(matched,
                Collections.singletonList(selected(0, "cred-a", MDL_DOC_TYPE, GIVEN, FAMILY)));
    }

    @Test
    public void membershipAndDocTypeAreCheckedBeforeDuplicateAndClaimCodeRules() throws Exception {
        assertSelectionFails(Arrays.asList(
                        selected(0, "cred-b", MDL_DOC_TYPE, GIVEN),
                        selected(0, "cred-b", MDL_DOC_TYPE, GIVEN),
                        selected(9, "cred-b", MDL_DOC_TYPE, GIVEN)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_DUPLICATE_SELECTED_DOCUMENT);
        assertSelectionFails(Collections.singletonList(selected(9, "cred-b", MDL_DOC_TYPE)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS);
        try {
            MdocProximityMatcher.validateSelection(matchedForSelectionTests(),
                    Collections.singletonList(selected(2, "cred-b", MDL_DOC_TYPE, FAMILY)));
            fail();
        } catch (WalletCoreException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("document 'cred-b' does not answer docRequest 2"));
        }
        try {
            MdocProximityMatcher.validateSelection(matchedForSelectionTests(),
                    Collections.singletonList(selected(0, "cred-b", "org.example.other", GIVEN)));
            fail();
        } catch (WalletCoreException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("docType of document 'cred-b' was changed"));
        }
    }

    @Test
    public void emptyClaimCodesIs06612() throws Exception {
        assertSelectionFails(Collections.singletonList(selected(0, "cred-b", MDL_DOC_TYPE)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_EMPTY_CLAIM_CODES);
        assertSelectionFails(Collections.singletonList(
                        new MdocRequestedDocument(0, MDL_DOC_TYPE, "cred-b", null, null, null)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_EMPTY_CLAIM_CODES);
    }

    @Test
    public void duplicateSelectedDocumentIs06613() throws Exception {
        assertSelectionFails(Arrays.asList(
                        selected(0, "cred-b", MDL_DOC_TYPE, GIVEN),
                        selected(0, "cred-b", MDL_DOC_TYPE, FAMILY)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_DUPLICATE_SELECTED_DOCUMENT);
    }

    @Test
    public void duplicateClaimCodeIs06614() throws Exception {
        assertSelectionFails(Collections.singletonList(selected(0, "cred-b", MDL_DOC_TYPE, GIVEN, GIVEN)),
                WalletCoreErrorCode.ERR_CODE_PROXIMITY_DUPLICATE_CLAIM_CODE);
    }

    @Test
    public void subsetsTwoCredentialsForOneRequestAndOneCredentialForTwoRequestsAreAllowed() throws Exception {
        List<MdocRequestedDocument> matched = matchedForSelectionTests();

        MdocProximityMatcher.validateSelection(matched, Arrays.asList(
                selected(0, "cred-a", MDL_DOC_TYPE, AGE),
                selected(0, "cred-b", MDL_DOC_TYPE, FAMILY, GIVEN)));

        MdocProximityMatcher.validateSelection(matched, Arrays.asList(
                selected(1, "cred-b", MDL_DOC_TYPE, FAMILY),
                selected(0, "cred-b", MDL_DOC_TYPE, GIVEN)));

        MdocProximityMatcher.validateSelection(matched, matched);
    }
}
