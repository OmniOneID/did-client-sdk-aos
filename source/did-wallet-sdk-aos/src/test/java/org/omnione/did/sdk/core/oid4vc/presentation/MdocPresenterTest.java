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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.upokecenter.cbor.CBOREncodeOptions;
import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;

import org.junit.Test;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocCborReader;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocClaimCodes;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocConstants;
import org.omnione.did.sdk.core.oid4vc.verify.CoseSign1Verifier;
import org.omnione.did.sdk.datamodel.oid4vc.Mdoc;
import org.omnione.did.sdk.utility.Encodings.Base16;
import org.omnione.did.sdk.utility.Encodings.Base64;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MdocPresenterTest {

    private static final String DOC_TYPE = MdocProximityFixtures.MDL_DOC_TYPE;
    private static final String NAME_SPACE = MdocProximityFixtures.MDL_NAME_SPACE;
    private static final String GIVEN_NAME = MdocProximityFixtures.GIVEN_NAME;
    private static final String FAMILY_NAME = MdocProximityFixtures.FAMILY_NAME;

    private static final String CLIENT_ID = "x509_san_dns:verifier.example";
    private static final String NONCE = "n-0S6_WzA2Mj";
    private static final String RESPONSE_URI = "https://verifier.example/response";

    private static final byte[] RAW_SIGNATURE = new byte[64];

    private static final List<String> ALL_CLAIM_CODES = Arrays.asList(
            MdocClaimCodes.of(NAME_SPACE, GIVEN_NAME),
            MdocClaimCodes.of(NAME_SPACE, FAMILY_NAME));

    private static final String GOLDEN_DEVICE_RESPONSE_BASE64URL =
            "o2d2ZXJzaW9uYzEuMGlkb2N1bWVudHOBo2dkb2NUeXBldW9yZy5pc28uMTgwMTMuNS4xLm1ETGxpc3N1ZXJTaWdu"
            + "ZWSiam5hbWVTcGFjZXOhcW9yZy5pc28uMTgwMTMuNS4xgtgYWFKkZnJhbmRvbVABAgMEBQYHCAkKCwwNDg8QaGRp"
            + "Z2VzdElEAGxlbGVtZW50VmFsdWVkUmFvbnFlbGVtZW50SWRlbnRpZmllcmpnaXZlbl9uYW1l2BhYUqRmcmFuZG9t"
            + "UAECAwQFBgcICQoLDA0ODxBoZGlnZXN0SUQBbGVsZW1lbnRWYWx1ZWNLaW1xZWxlbWVudElkZW50aWZpZXJrZmFt"
            + "aWx5X25hbWVqaXNzdWVyQXV0aIRAoEBAbGRldmljZVNpZ25lZKJqbmFtZVNwYWNlc9gYQaBqZGV2aWNlQXV0aKFv"
            + "ZGV2aWNlU2lnbmF0dXJlhEOhASag9lhAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAGZzdGF0dXMA";

    static final String DIS_D51_SESSION_TRANSCRIPT_BYTES_HEX =
            "d81859024183d8185858a20063312e30018201d818584ba4010220012158205a88d182bce5f42efa59943f33"
            + "359d2e8a968ff289d93e5fa444b624343167fe225820b16e8cf858ddc7690407ba61d4c338237a8cfcf3de6a"
            + "a672fc60a557aa32fc67d818584ba40102200121582060e3392385041f51403051f2415531cb56dd3f999c71"
            + "687013aac6768bc8187e225820e58deb8fdbe907f7dd5368245551a34796f7d2215c440c339bb0f7b67beccd"
            + "fa8258c391020f487315d10209616301013001046d646f631a200c016170706c69636174696f6e2f766e642e"
            + "626c7565746f6f74682e6c652e6f6f6230081b28128b37282801021c015c1e580469736f2e6f72673a313830"
            + "31333a646576696365656e676167656d656e746d646f63a20063312e30018201d818584ba401022001215820"
            + "5a88d182bce5f42efa59943f33359d2e8a968ff289d93e5fa444b624343167fe225820b16e8cf858ddc76904"
            + "07ba61d4c338237a8cfcf3de6aa672fc60a557aa32fc6758cd91022548721591020263720102110204616301"
            + "013000110206616301036e6663005102046163010157001a201e016170706c69636174696f6e2f766e642e62"
            + "6c7565746f6f74682e6c652e6f6f6230081b28078080bf2801021c021107c832fff6d26fa0beb34dfcd555d4"
            + "823a1c11010369736f2e6f72673a31383031333a6e66636e6663015a172b016170706c69636174696f6e2f76"
            + "6e642e7766612e6e616e57030101032302001324fec9a70b97ac9684a4e326176ef5b981c5e8533e5f00298c"
            + "fccbc35e700a6b020414";

    static final String DIS_D53_DEVICE_AUTHENTICATION_BYTES_HEX =
            "d818590271847444657669636541757468656e7469636174696f6e83d8185858a20063312e30018201d81858"
            + "4ba4010220012158205a88d182bce5f42efa59943f33359d2e8a968ff289d93e5fa444b624343167fe225820"
            + "b16e8cf858ddc7690407ba61d4c338237a8cfcf3de6aa672fc60a557aa32fc67d818584ba401022001215820"
            + "60e3392385041f51403051f2415531cb56dd3f999c71687013aac6768bc8187e225820e58deb8fdbe907f7dd"
            + "5368245551a34796f7d2215c440c339bb0f7b67beccdfa8258c391020f487315d10209616301013001046d64"
            + "6f631a200c016170706c69636174696f6e2f766e642e626c7565746f6f74682e6c652e6f6f6230081b28128b"
            + "37282801021c015c1e580469736f2e6f72673a31383031333a646576696365656e676167656d656e746d646f"
            + "63a20063312e30018201d818584ba4010220012158205a88d182bce5f42efa59943f33359d2e8a968ff289d9"
            + "3e5fa444b624343167fe225820b16e8cf858ddc7690407ba61d4c338237a8cfcf3de6aa672fc60a557aa32fc"
            + "6758cd91022548721591020263720102110204616301013000110206616301036e6663005102046163010157"
            + "001a201e016170706c69636174696f6e2f766e642e626c7565746f6f74682e6c652e6f6f6230081b28078080"
            + "bf2801021c021107c832fff6d26fa0beb34dfcd555d4823a1c11010369736f2e6f72673a31383031333a6e66"
            + "636e6663015a172b016170706c69636174696f6e2f766e642e7766612e6e616e57030101032302001324fec9"
            + "a70b97ac9684a4e326176ef5b981c5e8533e5f00298cfccbc35e700a6b020414756f72672e69736f2e313830"
            + "31332e352e312e6d444cd81841a0";

    private static final String DIS_D53_DOC_TYPE_HEX = "756f72672e69736f2e31383031332e352e312e6d444c";
    private static final String DIS_D53_DEVICE_NAME_SPACES_BYTES_HEX = "d81841a0";

    @Test
    public void createVpTokenIsCreateDeviceResponseWrappedInBase64Url() throws Exception {
        Mdoc mdoc = fixtureMdoc();
        SignDigest sign = data -> RAW_SIGNATURE;

        String token = MdocPresenter.createVpToken(
                mdoc, ALL_CLAIM_CODES, CLIENT_ID, NONCE, null, RESPONSE_URI, sign);

        byte[] transcript = oid4vpTranscript();
        byte[] response = MdocPresenter.createDeviceResponse(mdoc, ALL_CLAIM_CODES, transcript, sign);

        assertEquals(token, Base64.encodeUrlString(response));

        CBORObject deviceResponse = CBORObject.DecodeFromBytes(response);
        assertEquals(MdocConstants.DeviceResponse.VERSION_1_0,
                deviceResponse.get(MdocConstants.DeviceResponse.VERSION).AsString());
        assertEquals(MdocConstants.DeviceResponse.STATUS_OK,
                deviceResponse.get(MdocConstants.DeviceResponse.STATUS).AsInt32Value());

        CBORObject documents = deviceResponse.get(MdocConstants.DeviceResponse.DOCUMENTS);
        assertEquals(CBORType.Array, documents.getType());
        assertEquals(1, documents.size());

        CBORObject document = documents.get(0);
        assertEquals(DOC_TYPE, document.get(MdocConstants.Document.DOC_TYPE).AsString());

        CBORObject issuerSigned = document.get(MdocConstants.Document.ISSUER_SIGNED);
        assertNotNull(issuerSigned.get(MdocConstants.IssuerSigned.NAME_SPACES));
        assertNotNull(issuerSigned.get(MdocConstants.IssuerSigned.ISSUER_AUTH));
        assertEquals(2, issuerSigned.get(MdocConstants.IssuerSigned.NAME_SPACES).get(NAME_SPACE).size());

        CBORObject deviceSigned = document.get(MdocConstants.Document.DEVICE_SIGNED);
        CBORObject deviceAuth = deviceSigned.get(MdocConstants.DeviceSigned.DEVICE_AUTH);
        CBORObject deviceSignature = deviceAuth.get(MdocConstants.DeviceSigned.DEVICE_SIGNATURE);
        assertNotNull(deviceSignature);
        assertArrayEquals(RAW_SIGNATURE,
                deviceSignature.get(MdocConstants.Cose.SIGN1_INDEX_SIGNATURE).GetByteString());
    }

    @Test
    public void createDeviceResponseWireFormatIsPinnedToGoldenBytesRegeneratedForCddlOrderedMapsAndRawTranscriptJoin()
            throws Exception {
        Mdoc mdoc = fixtureMdoc();
        SignDigest sign = data -> RAW_SIGNATURE;

        byte[] response = MdocPresenter.createDeviceResponse(mdoc, ALL_CLAIM_CODES, oid4vpTranscript(), sign);

        assertEquals(GOLDEN_DEVICE_RESPONSE_BASE64URL, Base64.encodeUrlString(response));
    }

    @Test
    public void createDeviceResponseEmitsMapsInCddlInsertionOrderNotRfc8949SortedOrder() throws Exception {
        byte[] response = MdocPresenter.createDeviceResponse(fixtureMdoc(), ALL_CLAIM_CODES,
                oid4vpTranscript(), data -> RAW_SIGNATURE);

        CBORObject deviceResponse = CBORObject.DecodeFromBytes(response, new CBOREncodeOptions("keepkeyorder=true"));
        assertEquals(Arrays.asList(MdocConstants.DeviceResponse.VERSION, MdocConstants.DeviceResponse.DOCUMENTS,
                MdocConstants.DeviceResponse.STATUS), keys(deviceResponse));
        CBORObject document = deviceResponse.get(MdocConstants.DeviceResponse.DOCUMENTS).get(0);
        assertEquals(Arrays.asList(MdocConstants.Document.DOC_TYPE, MdocConstants.Document.ISSUER_SIGNED,
                MdocConstants.Document.DEVICE_SIGNED), keys(document));
        assertEquals(Arrays.asList(MdocConstants.IssuerSigned.NAME_SPACES, MdocConstants.IssuerSigned.ISSUER_AUTH),
                keys(document.get(MdocConstants.Document.ISSUER_SIGNED)));
        assertEquals(Arrays.asList(MdocConstants.DeviceSigned.NAME_SPACES, MdocConstants.DeviceSigned.DEVICE_AUTH),
                keys(document.get(MdocConstants.Document.DEVICE_SIGNED)));
    }

    @Test
    public void createDeviceResponseFiltersNameSpacesToOnlyRequestedElements() throws Exception {
        Mdoc mdoc = fixtureMdoc();
        List<String> claimCodes = Collections.singletonList(MdocClaimCodes.of(NAME_SPACE, GIVEN_NAME));
        SignDigest sign = data -> RAW_SIGNATURE;

        byte[] response = MdocPresenter.createDeviceResponse(mdoc, claimCodes, oid4vpTranscript(), sign);

        CBORObject deviceResponse = CBORObject.DecodeFromBytes(response);
        CBORObject issuerSigned = deviceResponse
                .get(MdocConstants.DeviceResponse.DOCUMENTS).get(0)
                .get(MdocConstants.Document.ISSUER_SIGNED);
        CBORObject filteredEntries = issuerSigned
                .get(MdocConstants.IssuerSigned.NAME_SPACES).get(NAME_SPACE);

        assertEquals(1, filteredEntries.size());
        CBORObject onlyItem = CBORObject.DecodeFromBytes(
                MdocCborReader.issuerSignedItemBytes(filteredEntries.get(0)));
        assertEquals(GIVEN_NAME,
                onlyItem.get(MdocConstants.IssuerSignedItem.ELEMENT_IDENTIFIER).AsString());
    }

    @Test
    public void createDeviceResponseRejectsClaimCodeTheDocumentDoesNotHoldOnOid4vpPath() throws Exception {
        List<String> claimCodes = Arrays.asList(MdocClaimCodes.of(NAME_SPACE, GIVEN_NAME),
                MdocClaimCodes.of(NAME_SPACE, "not_issued"));
        try {
            MdocPresenter.createDeviceResponse(fixtureMdoc(), claimCodes, oid4vpTranscript(), data -> RAW_SIGNATURE);
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_OID4VP_NO_MATCHED_CREDENTIALS.getCode(), e.getErrorCode());
        }
    }

    @Test
    public void createDeviceResponseRejectsEmptyClaimCodesInsteadOfReturningEverything() throws Exception {
        try {
            MdocPresenter.createDeviceResponse(fixtureMdoc(), Collections.emptyList(), oid4vpTranscript(),
                    data -> RAW_SIGNATURE);
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_OID4VP_NO_MATCHED_CREDENTIALS.getCode(), e.getErrorCode());
        }
    }

    @Test
    public void deviceSignatureOverRawTranscriptJoinVerifiesAgainstIndependentlyEncodedDeviceAuthentication()
            throws Exception {
        KeyPair deviceKey = MdocProximityFixtures.generateKeyPair();
        byte[] transcript = oid4vpTranscript();

        byte[] response = MdocPresenter.createDeviceResponse(fixtureMdoc(), ALL_CLAIM_CODES, transcript,
                MdocProximityFixtures.es256Signer(deviceKey));

        CBORObject deviceAuthentication = CBORObject.NewArray();
        deviceAuthentication.Add(MdocConstants.DeviceAuthentication.DEVICE_AUTHENTICATION);
        deviceAuthentication.Add(CBORObject.DecodeFromBytes(transcript));
        deviceAuthentication.Add(DOC_TYPE);
        deviceAuthentication.Add(CBORObject.FromObject(CBORObject.NewMap().EncodeToBytes())
                .WithTag(MdocConstants.TAG_ENCODED_CBOR));
        byte[] expectedPayload = CBORObject.FromObject(deviceAuthentication.EncodeToBytes())
                .WithTag(MdocConstants.TAG_ENCODED_CBOR).EncodeToBytes();

        CBORObject deviceSignature = CBORObject.DecodeFromBytes(response)
                .get(MdocConstants.DeviceResponse.DOCUMENTS).get(0)
                .get(MdocConstants.Document.DEVICE_SIGNED)
                .get(MdocConstants.DeviceSigned.DEVICE_AUTH)
                .get(MdocConstants.DeviceSigned.DEVICE_SIGNATURE);
        assertTrue(deviceSignature.get(MdocConstants.Cose.SIGN1_INDEX_PAYLOAD).isNull());

        assertTrue(CoseSign1Verifier.verify(withPayload(deviceSignature, expectedPayload), deviceKey.getPublic()));
        assertFalse(CoseSign1Verifier.verify(withPayload(deviceSignature,
                MdocPresenter.deviceAuthenticationBytes(MdocProximityFixtures.tag24(transcript), DOC_TYPE)),
                deviceKey.getPublic()));
    }

    @Test
    public void deviceAuthenticationBytesEmbedsDisD51SessionTranscriptExactlyAsDisD53Does() throws Exception {
        byte[] sessionTranscriptBytes = Base16.toBytes(DIS_D51_SESSION_TRANSCRIPT_BYTES_HEX);
        byte[] disDeviceAuthenticationBytes = Base16.toBytes(DIS_D53_DEVICE_AUTHENTICATION_BYTES_HEX);
        assertEquals(582, sessionTranscriptBytes.length);
        assertEquals(630, disDeviceAuthenticationBytes.length);

        byte[] transcript = MdocPresenter.resolveSessionTranscript(sessionTranscriptBytes);
        assertEquals((byte) 0x83, transcript[0]);
        assertEquals(sessionTranscriptBytes.length - 5, transcript.length);

        byte[] ours = MdocPresenter.deviceAuthenticationBytes(transcript, "org.iso.18013.5.1.mDL");
        assertEquals(Base16.toHex(disDeviceAuthenticationBytes), Base16.toHex(ours));

        String innerHex = Base16.toHex(CBORObject.DecodeFromBytes(ours).GetByteString());
        String expectedPrefix = "84" + "74" + Base16.toHex("DeviceAuthentication".getBytes());
        assertTrue(innerHex.startsWith(expectedPrefix + Base16.toHex(transcript)));
        assertTrue(innerHex.endsWith(DIS_D53_DOC_TYPE_HEX + DIS_D53_DEVICE_NAME_SPACES_BYTES_HEX));
        assertFalse(innerHex.contains(Base16.toHex(sessionTranscriptBytes)));
    }

    @Test
    public void taggedAndBareSessionTranscriptsProduceTheSameDeviceAuthenticationBytes() throws Exception {
        byte[] bare = MdocProximityFixtures.bareSessionTranscript();
        byte[] tagged = MdocProximityFixtures.tag24(bare);

        assertArrayEquals(bare, MdocPresenter.resolveSessionTranscript(bare));
        assertArrayEquals(bare, MdocPresenter.resolveSessionTranscript(tagged));
        assertArrayEquals(MdocPresenter.deviceAuthenticationBytes(MdocPresenter.resolveSessionTranscript(bare), DOC_TYPE),
                MdocPresenter.deviceAuthenticationBytes(MdocPresenter.resolveSessionTranscript(tagged), DOC_TYPE));

        SignDigest sign = data -> RAW_SIGNATURE;
        assertArrayEquals(MdocPresenter.createDeviceResponse(fixtureMdoc(), ALL_CLAIM_CODES, bare, sign),
                MdocPresenter.createDeviceResponse(fixtureMdoc(), ALL_CLAIM_CODES, tagged, sign));
    }

    @Test
    public void resolveSessionTranscriptRejectsEverythingThatIsNeitherTag24Of3ArrayNorBare3Array() {
        CBORObject twoElements = CBORObject.NewArray();
        twoElements.Add(CBORObject.Null);
        twoElements.Add(CBORObject.Null);
        CBORObject fourElements = CBORObject.NewArray();
        fourElements.Add(CBORObject.Null);
        fourElements.Add(CBORObject.Null);
        fourElements.Add(CBORObject.Null);
        fourElements.Add(CBORObject.Null);

        assertInvalidTranscript(null);
        assertInvalidTranscript(new byte[0]);
        assertInvalidTranscript(twoElements.EncodeToBytes());
        assertInvalidTranscript(fourElements.EncodeToBytes());
        assertInvalidTranscript(MdocProximityFixtures.tag24(twoElements.EncodeToBytes()));
        assertInvalidTranscript(MdocProximityFixtures.tag24(CBORObject.NewMap().EncodeToBytes()));
        assertInvalidTranscript(CBORObject.FromObject("text").WithTag(MdocConstants.TAG_ENCODED_CBOR).EncodeToBytes());
        assertInvalidTranscript(CBORObject.FromObject(new byte[] {(byte) 0x83}).WithTag(25).EncodeToBytes());
        assertInvalidTranscript(new byte[] {(byte) 0x83, 0x00});
        assertInvalidTranscript(new byte[] {(byte) 0xD8, 0x18, 0x41});
        assertInvalidTranscript(CBORObject.NewMap().EncodeToBytes());
    }

    private static void assertInvalidTranscript(byte[] input) {
        try {
            MdocPresenter.resolveSessionTranscript(input);
            fail("accepted " + (input == null ? "null" : Base16.toHex(input)));
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SESSION_TRANSCRIPT.getCode(), e.getErrorCode());
        }
    }

    private static CBORObject withPayload(CBORObject coseSign1, byte[] payload) {
        CBORObject copy = CBORObject.NewArray();
        copy.Add(coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_PROTECTED));
        copy.Add(coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_UNPROTECTED));
        copy.Add(CBORObject.FromObject(payload));
        copy.Add(coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_SIGNATURE));
        return copy;
    }

    private static List<String> keys(CBORObject map) {
        List<String> keys = new java.util.ArrayList<>();
        for (CBORObject key : map.getKeys()) {
            keys.add(key.AsString());
        }
        return keys;
    }

    private static byte[] oid4vpTranscript() throws Exception {
        return MdocPresenter.buildSessionTranscript(CLIENT_ID, NONCE, null, RESPONSE_URI).EncodeToBytes();
    }

    private static Mdoc fixtureMdoc() {
        return MdocProximityFixtures.mdlWithGivenAndFamilyName();
    }
}
