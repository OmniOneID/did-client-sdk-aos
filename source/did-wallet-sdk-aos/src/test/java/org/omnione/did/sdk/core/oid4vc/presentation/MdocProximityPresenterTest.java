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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.AGE_OVER_18;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.FAMILY_NAME;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.GIVEN_NAME;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.MDL_DOC_TYPE;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.MDL_NAME_SPACE;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.item;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.mdlWithGivenAndFamilyName;
import static org.omnione.did.sdk.core.oid4vc.presentation.MdocProximityFixtures.mdoc;

import com.upokecenter.cbor.CBORObject;

import org.junit.Test;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocCborReader;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocClaimCodes;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocConstants;
import org.omnione.did.sdk.core.oid4vc.verify.CoseSign1Verifier;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.MdocDeviceAuthMethod;
import org.omnione.did.sdk.datamodel.oid4vc.MdocDeviceResponse;
import org.omnione.did.sdk.datamodel.oid4vc.MdocRequestedDocument;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MdocProximityPresenterTest {

    private static final String GIVEN = MdocClaimCodes.of(MDL_NAME_SPACE, GIVEN_NAME);
    private static final String FAMILY = MdocClaimCodes.of(MDL_NAME_SPACE, FAMILY_NAME);
    private static final String AGE = MdocClaimCodes.of(MDL_NAME_SPACE, AGE_OVER_18);
    private static final byte[] RAW_SIGNATURE = new byte[64];

    private static Map<String, MdocCredentialItem> stored() {
        Map<String, String> withAge = new LinkedHashMap<>();
        withAge.put(GIVEN_NAME, "Raon");
        withAge.put(AGE_OVER_18, "true");
        Map<String, MdocCredentialItem> byId = new HashMap<>();
        byId.put("cred-a", item("cred-a", mdoc(MDL_DOC_TYPE, MDL_NAME_SPACE, withAge)));
        byId.put("cred-b", item("cred-b", mdlWithGivenAndFamilyName()));
        return byId;
    }

    private static MdocRequestedDocument selected(int index, String credentialId, String... codes) {
        return new MdocRequestedDocument(index, MDL_DOC_TYPE, credentialId, Arrays.asList(codes), null, null);
    }

    @Test
    public void documentsFollowSelectedOrderEachCredentialSignsOnceAndAuthMethodsAreAllDeviceSignature()
            throws Exception {
        AtomicInteger signsA = new AtomicInteger();
        AtomicInteger signsB = new AtomicInteger();
        Map<String, SignDigest> signers = new HashMap<>();
        signers.put("cred-a", data -> { signsA.incrementAndGet(); return RAW_SIGNATURE; });
        signers.put("cred-b", data -> { signsB.incrementAndGet(); return RAW_SIGNATURE; });

        List<MdocRequestedDocument> selected = Arrays.asList(
                selected(1, "cred-b", FAMILY),
                selected(0, "cred-a", AGE),
                selected(0, "cred-b", GIVEN, FAMILY));

        MdocDeviceResponse result = MdocProximityPresenter.createDeviceResponse(selected, stored(), signers,
                MdocProximityFixtures.bareSessionTranscript());

        assertEquals(1, signsA.get());
        assertEquals(1, signsB.get());

        Map<String, MdocDeviceAuthMethod> expectedMethods = new HashMap<>();
        expectedMethods.put("cred-a", MdocDeviceAuthMethod.DEVICE_SIGNATURE);
        expectedMethods.put("cred-b", MdocDeviceAuthMethod.DEVICE_SIGNATURE);
        assertEquals(expectedMethods, result.getAuthMethods());

        CBORObject deviceResponse = CBORObject.DecodeFromBytes(result.getResponse());
        assertEquals("1.0", deviceResponse.get(MdocConstants.DeviceResponse.VERSION).AsString());
        assertEquals(0, deviceResponse.get(MdocConstants.DeviceResponse.STATUS).AsInt32Value());
        assertEquals(3, deviceResponse.size());
        CBORObject documents = deviceResponse.get(MdocConstants.DeviceResponse.DOCUMENTS);
        assertEquals(3, documents.size());
        assertEquals(Collections.singletonList(FAMILY_NAME), presentedElements(documents.get(0)));
        assertEquals(Collections.singletonList(AGE_OVER_18), presentedElements(documents.get(1)));
        assertEquals(Arrays.asList(GIVEN_NAME, FAMILY_NAME), presentedElements(documents.get(2)));
        for (int i = 0; i < 3; i++) {
            assertEquals(MDL_DOC_TYPE, documents.get(i).get(MdocConstants.Document.DOC_TYPE).AsString());
            CBORObject deviceSigned = documents.get(i).get(MdocConstants.Document.DEVICE_SIGNED);
            assertArrayEquals(new byte[] {(byte) 0xd8, 0x18, 0x41, (byte) 0xa0},
                    deviceSigned.get(MdocConstants.DeviceSigned.NAME_SPACES).EncodeToBytes());
            assertEquals(1, deviceSigned.get(MdocConstants.DeviceSigned.DEVICE_AUTH).size());
        }
        assertArrayEquals(documents.get(0).get(MdocConstants.Document.DEVICE_SIGNED).EncodeToBytes(),
                documents.get(2).get(MdocConstants.Document.DEVICE_SIGNED).EncodeToBytes());
    }

    @Test
    public void deviceSignatureOfEachDocumentVerifiesWithItsOwnKeyOverTheBareTranscript() throws Exception {
        KeyPair keyA = MdocProximityFixtures.generateKeyPair();
        KeyPair keyB = MdocProximityFixtures.generateKeyPair();
        Map<String, SignDigest> signers = new HashMap<>();
        signers.put("cred-a", MdocProximityFixtures.es256Signer(keyA));
        signers.put("cred-b", MdocProximityFixtures.es256Signer(keyB));
        byte[] bare = MdocProximityFixtures.bareSessionTranscript();

        MdocDeviceResponse result = MdocProximityPresenter.createDeviceResponse(
                Arrays.asList(selected(0, "cred-a", GIVEN), selected(0, "cred-b", GIVEN)),
                stored(), signers, MdocProximityFixtures.tag24(bare));

        byte[] payload = MdocPresenter.deviceAuthenticationBytes(bare, MDL_DOC_TYPE);
        CBORObject documents = CBORObject.DecodeFromBytes(result.getResponse())
                .get(MdocConstants.DeviceResponse.DOCUMENTS);
        assertTrue(CoseSign1Verifier.verify(withPayload(deviceSignature(documents.get(0)), payload), keyA.getPublic()));
        assertTrue(CoseSign1Verifier.verify(withPayload(deviceSignature(documents.get(1)), payload), keyB.getPublic()));
    }

    @Test
    public void claimCodeNotHeldByTheDocumentIs06611OnTheProximityPath() throws Exception {
        Map<String, SignDigest> signers = new HashMap<>();
        signers.put("cred-a", data -> RAW_SIGNATURE);
        try {
            MdocProximityPresenter.createDeviceResponse(
                    Collections.singletonList(selected(0, "cred-a", GIVEN, FAMILY)),
                    stored(), signers, MdocProximityFixtures.bareSessionTranscript());
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS.getCode(), e.getErrorCode());
            assertTrue(e.getMessage(),
                    e.getMessage().contains("claim(s) " + FAMILY + " are not disclosable in the credential"));
        }
    }

    @Test
    public void claimCodeOutsideTheRequestIsCarriedIntoTheResponseWhenTheDocumentHoldsIt() throws Exception {
        Map<String, SignDigest> signers = new HashMap<>();
        signers.put("cred-b", data -> RAW_SIGNATURE);

        MdocDeviceResponse result = MdocProximityPresenter.createDeviceResponse(
                Collections.singletonList(selected(1, "cred-b", FAMILY, GIVEN)),
                stored(), signers, MdocProximityFixtures.bareSessionTranscript());

        CBORObject documents = CBORObject.DecodeFromBytes(result.getResponse())
                .get(MdocConstants.DeviceResponse.DOCUMENTS);
        assertEquals(1, documents.size());
        assertEquals(Arrays.asList(GIVEN_NAME, FAMILY_NAME), presentedElements(documents.get(0)));
    }

    @Test
    public void documentMissingFromStoreIs06611NotStored() throws Exception {
        Map<String, SignDigest> signers = new HashMap<>();
        signers.put("cred-x", data -> RAW_SIGNATURE);
        try {
            MdocProximityPresenter.createDeviceResponse(
                    Collections.singletonList(selected(0, "cred-x", GIVEN)),
                    stored(), signers, MdocProximityFixtures.bareSessionTranscript());
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SELECTED_DOCUMENTS.getCode(), e.getErrorCode());
            assertTrue(e.getMessage(), e.getMessage().contains("document 'cred-x' is not stored"));
        }
    }

    @Test
    public void invalidTranscriptIs06621BeforeAnySignature() throws Exception {
        AtomicInteger signs = new AtomicInteger();
        Map<String, SignDigest> signers = new HashMap<>();
        signers.put("cred-a", data -> { signs.incrementAndGet(); return RAW_SIGNATURE; });
        try {
            MdocProximityPresenter.createDeviceResponse(
                    Collections.singletonList(selected(0, "cred-a", GIVEN)),
                    stored(), signers, CBORObject.NewMap().EncodeToBytes());
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SESSION_TRANSCRIPT.getCode(), e.getErrorCode());
        }
        assertEquals(0, signs.get());
    }

    private static List<String> presentedElements(CBORObject document) {
        CBORObject entries = document.get(MdocConstants.Document.ISSUER_SIGNED)
                .get(MdocConstants.IssuerSigned.NAME_SPACES).get(MDL_NAME_SPACE);
        List<String> names = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            CBORObject item = CBORObject.DecodeFromBytes(MdocCborReader.issuerSignedItemBytes(entries.get(i)));
            names.add(item.get(MdocConstants.IssuerSignedItem.ELEMENT_IDENTIFIER).AsString());
        }
        return names;
    }

    private static CBORObject deviceSignature(CBORObject document) {
        return document.get(MdocConstants.Document.DEVICE_SIGNED)
                .get(MdocConstants.DeviceSigned.DEVICE_AUTH)
                .get(MdocConstants.DeviceSigned.DEVICE_SIGNATURE);
    }

    private static CBORObject withPayload(CBORObject coseSign1, byte[] payload) {
        CBORObject copy = CBORObject.NewArray();
        copy.Add(coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_PROTECTED));
        copy.Add(coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_UNPROTECTED));
        copy.Add(CBORObject.FromObject(payload));
        copy.Add(coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_SIGNATURE));
        return copy;
    }
}
