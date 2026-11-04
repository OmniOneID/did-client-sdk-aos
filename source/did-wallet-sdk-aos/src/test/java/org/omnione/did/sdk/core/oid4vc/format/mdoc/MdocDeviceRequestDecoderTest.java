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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.upokecenter.cbor.CBORObject;

import org.junit.Test;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MdocDeviceRequestDecoderTest {

    private static final String MDL = "org.iso.18013.5.1.mDL";
    private static final String NS = "org.iso.18013.5.1";

    @Test
    public void decodesMultipleDocRequestsInOrderWithIntentToRetain() throws Exception {
        CBORObject first = docRequest(MDL, NS, "given_name", true, "family_name", false);
        CBORObject second = docRequest("org.example.other", "org.example", "x", true);

        List<MdocDeviceRequestDecoder.DocRequest> decoded = MdocDeviceRequestDecoder.decode(
                deviceRequest("1.0", first, second));

        assertEquals(2, decoded.size());
        assertEquals(MDL, decoded.get(0).getDocType());
        Map<String, Boolean> elements = decoded.get(0).getNameSpaces().get(NS);
        assertEquals(Arrays.asList("given_name", "family_name"), new java.util.ArrayList<>(elements.keySet()));
        assertEquals(Boolean.TRUE, elements.get("given_name"));
        assertEquals(Boolean.FALSE, elements.get("family_name"));
        assertEquals("org.example.other", decoded.get(1).getDocType());
        assertEquals(Boolean.TRUE, decoded.get(1).getNameSpaces().get("org.example").get("x"));
    }

    @Test
    public void ignoresUnknownKeysReaderAuthAndVersion11() throws Exception {
        CBORObject docRequest = docRequest(MDL, NS, "given_name", true);
        docRequest.Add("readerAuth", CBORObject.NewArray());
        docRequest.Add("docRequestInfo", CBORObject.NewMap());
        docRequest.Add("futureKey", 42);
        CBORObject itemsRequest = CBORObject.DecodeFromBytes(docRequest.get("itemsRequest").GetByteString());
        itemsRequest.Add("requestInfo", CBORObject.NewMap());
        itemsRequest.Add("rfu", "x");
        docRequest.Set("itemsRequest",
                CBORObject.FromObject(itemsRequest.EncodeToBytes()).WithTag(MdocConstants.TAG_ENCODED_CBOR));

        CBORObject request = CBORObject.NewOrderedMap();
        request.Add("version", "1.1");
        request.Add("docRequests", CBORObject.NewArray().Add(docRequest));
        request.Add("deviceRequestInfo", CBORObject.NewMap());
        request.Add("readerAuthAll", CBORObject.NewArray());
        request.Add("unknown", CBORObject.True);

        List<MdocDeviceRequestDecoder.DocRequest> decoded = MdocDeviceRequestDecoder.decode(request.EncodeToBytes());

        assertEquals(1, decoded.size());
        assertEquals(MDL, decoded.get(0).getDocType());
        assertEquals(Boolean.TRUE, decoded.get(0).getNameSpaces().get(NS).get("given_name"));
    }

    @Test
    public void rejectsMalformedRequestsWith06620AndDetail() {
        assertInvalid(null, "empty");
        assertInvalid(new byte[0], "empty");
        assertInvalid(new byte[] {(byte) 0xFF}, "not valid CBOR");
        assertInvalid(CBORObject.NewArray().EncodeToBytes(), "DeviceRequest is not a map");
        assertInvalid(CBORObject.NewMap().EncodeToBytes(), "no docRequests");
        assertInvalid(deviceRequest("1.0"), "no docRequests");

        assertInvalid(deviceRequest("1.0", CBORObject.NewMap()), "docRequests[0] has no itemsRequest");

        CBORObject untagged = CBORObject.NewMap();
        untagged.Add("itemsRequest", CBORObject.NewMap());
        assertInvalid(deviceRequest("1.0", untagged), "itemsRequest is not #6.24(bstr)");

        CBORObject noDocType = CBORObject.NewMap();
        noDocType.Add("nameSpaces", CBORObject.NewMap());
        assertInvalid(deviceRequest("1.0", wrap(noDocType)), "has no docType");

        CBORObject noNameSpaces = CBORObject.NewMap();
        noNameSpaces.Add("docType", MDL);
        assertInvalid(deviceRequest("1.0", wrap(noNameSpaces)), "has no nameSpaces");

        CBORObject nonBooleanRetain = CBORObject.NewMap();
        nonBooleanRetain.Add("docType", MDL);
        CBORObject elements = CBORObject.NewMap();
        elements.Add("given_name", 1);
        CBORObject nameSpaces = CBORObject.NewMap();
        nameSpaces.Add(NS, elements);
        nonBooleanRetain.Add("nameSpaces", nameSpaces);
        assertInvalid(deviceRequest("1.0", wrap(nonBooleanRetain)), "is not a boolean");

        CBORObject good = docRequest(MDL, NS, "given_name", true);
        CBORObject bad = CBORObject.NewMap();
        assertInvalid(deviceRequest("1.0", good, bad), "docRequests[1] has no itemsRequest");
    }

    private static void assertInvalid(byte[] input, String detailFragment) {
        try {
            MdocDeviceRequestDecoder.decode(input);
            fail();
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_DEVICE_REQUEST.getCode(), e.getErrorCode());
            assertTrue(e.getMessage(), e.getMessage().contains(detailFragment));
        }
    }

    private static CBORObject wrap(CBORObject itemsRequest) {
        CBORObject docRequest = CBORObject.NewMap();
        docRequest.Add("itemsRequest",
                CBORObject.FromObject(itemsRequest.EncodeToBytes()).WithTag(MdocConstants.TAG_ENCODED_CBOR));
        return docRequest;
    }

    private static CBORObject docRequest(String docType, String nameSpace, Object... nameAndRetain) {
        CBORObject elements = CBORObject.NewOrderedMap();
        for (int i = 0; i < nameAndRetain.length; i += 2) {
            elements.Add(nameAndRetain[i], nameAndRetain[i + 1]);
        }
        CBORObject nameSpaces = CBORObject.NewMap();
        nameSpaces.Add(nameSpace, elements);
        CBORObject itemsRequest = CBORObject.NewMap();
        itemsRequest.Add("docType", docType);
        itemsRequest.Add("nameSpaces", nameSpaces);
        return wrap(itemsRequest);
    }

    private static byte[] deviceRequest(String version, CBORObject... docRequests) {
        CBORObject array = CBORObject.NewArray();
        for (CBORObject docRequest : docRequests) {
            array.Add(docRequest);
        }
        CBORObject request = CBORObject.NewMap();
        request.Add("version", version);
        request.Add("docRequests", array);
        return request.EncodeToBytes();
    }
}
