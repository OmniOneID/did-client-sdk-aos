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

import com.upokecenter.cbor.CBORObject;

import org.omnione.did.sdk.core.oid4vc.crypto.EcSignatureUtils;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocConstants;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.Mdoc;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.utility.Encodings.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.LinkedHashMap;
import java.util.Map;

final class MdocProximityFixtures {

    static final String SC_PROVIDER = "SC";
    static final String MDL_DOC_TYPE = "org.iso.18013.5.1.mDL";
    static final String MDL_NAME_SPACE = "org.iso.18013.5.1";
    static final String GIVEN_NAME = "given_name";
    static final String FAMILY_NAME = "family_name";
    static final String AGE_OVER_18 = "age_over_18";
    static final byte[] FIXED_RANDOM = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    static {
        if (Security.getProvider(SC_PROVIDER) == null) {
            Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
        }
    }

    private MdocProximityFixtures() {}

    static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", SC_PROVIDER);
        generator.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
        return generator.generateKeyPair();
    }

    static SignDigest es256Signer(KeyPair keyPair) {
        return data -> {
            try {
                Signature signer = Signature.getInstance("SHA256withECDSA", SC_PROVIDER);
                signer.initSign(keyPair.getPrivate());
                signer.update(data);
                return EcSignatureUtils.derToRaw(signer.sign(), 32);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        };
    }

    static Mdoc mdoc(String docType, String nameSpace, Map<String, String> elements) {
        CBORObject entries = CBORObject.NewArray();
        Map<String, Object> parsedElements = new LinkedHashMap<>();
        int digestId = 0;
        for (Map.Entry<String, String> element : elements.entrySet()) {
            entries.Add(issuerSignedItemEntry(digestId++, element.getKey(), element.getValue()));
            parsedElements.put(element.getKey(), element.getValue());
        }

        CBORObject nameSpaces = CBORObject.NewMap();
        nameSpaces.Add(nameSpace, entries);

        CBORObject issuerAuth = CBORObject.NewArray();
        issuerAuth.Add(CBORObject.FromObject(new byte[0]));
        issuerAuth.Add(CBORObject.NewMap());
        issuerAuth.Add(CBORObject.FromObject(new byte[0]));
        issuerAuth.Add(CBORObject.FromObject(new byte[0]));

        CBORObject issuerSigned = CBORObject.NewMap();
        issuerSigned.Add(MdocConstants.IssuerSigned.NAME_SPACES, nameSpaces);
        issuerSigned.Add(MdocConstants.IssuerSigned.ISSUER_AUTH, issuerAuth);

        Map<String, Map<String, Object>> parsedNameSpaces = new LinkedHashMap<>();
        parsedNameSpaces.put(nameSpace, parsedElements);
        return new Mdoc(Base64.encodeUrlString(issuerSigned.EncodeToBytes()), docType, parsedNameSpaces, null);
    }

    static Mdoc mdlWithGivenAndFamilyName() {
        Map<String, String> elements = new LinkedHashMap<>();
        elements.put(GIVEN_NAME, "Raon");
        elements.put(FAMILY_NAME, "Kim");
        return mdoc(MDL_DOC_TYPE, MDL_NAME_SPACE, elements);
    }

    static MdocCredentialItem item(String id, Mdoc mdoc) {
        return new MdocCredentialItem(id, CredentialFormat.MSO_MDOC, "mdl", "kid-" + id, "mdl", mdoc);
    }

    static CBORObject issuerSignedItemEntry(int digestId, String elementIdentifier, String elementValue) {
        CBORObject item = CBORObject.NewMap();
        item.Add(MdocConstants.IssuerSignedItem.DIGEST_ID, digestId);
        item.Add(MdocConstants.IssuerSignedItem.RANDOM, FIXED_RANDOM);
        item.Add(MdocConstants.IssuerSignedItem.ELEMENT_IDENTIFIER, elementIdentifier);
        item.Add(MdocConstants.IssuerSignedItem.ELEMENT_VALUE, elementValue);
        return CBORObject.FromObject(item.EncodeToBytes()).WithTag(MdocConstants.TAG_ENCODED_CBOR);
    }

    static CBORObject docRequest(String docType, Map<String, Map<String, Boolean>> nameSpaces) {
        CBORObject nameSpacesCbor = CBORObject.NewOrderedMap();
        for (Map.Entry<String, Map<String, Boolean>> ns : nameSpaces.entrySet()) {
            CBORObject elements = CBORObject.NewOrderedMap();
            for (Map.Entry<String, Boolean> element : ns.getValue().entrySet()) {
                elements.Add(element.getKey(), element.getValue());
            }
            nameSpacesCbor.Add(ns.getKey(), elements);
        }
        CBORObject itemsRequest = CBORObject.NewOrderedMap();
        itemsRequest.Add(MdocConstants.DeviceRequest.DOC_TYPE, docType);
        itemsRequest.Add(MdocConstants.DeviceRequest.NAME_SPACES, nameSpacesCbor);

        CBORObject docRequest = CBORObject.NewOrderedMap();
        docRequest.Add(MdocConstants.DeviceRequest.ITEMS_REQUEST,
                CBORObject.FromObject(itemsRequest.EncodeToBytes()).WithTag(MdocConstants.TAG_ENCODED_CBOR));
        return docRequest;
    }

    static CBORObject mdlDocRequest(Map<String, Boolean> elements) {
        Map<String, Map<String, Boolean>> nameSpaces = new LinkedHashMap<>();
        nameSpaces.put(MDL_NAME_SPACE, elements);
        return docRequest(MDL_DOC_TYPE, nameSpaces);
    }

    static byte[] deviceRequest(String version, CBORObject... docRequests) {
        CBORObject docRequestArray = CBORObject.NewArray();
        for (CBORObject docRequest : docRequests) {
            docRequestArray.Add(docRequest);
        }
        CBORObject deviceRequest = CBORObject.NewOrderedMap();
        deviceRequest.Add("version", version);
        deviceRequest.Add(MdocConstants.DeviceRequest.DOC_REQUESTS, docRequestArray);
        return deviceRequest.EncodeToBytes();
    }

    static Map<String, Boolean> elements(Object... nameAndRetain) {
        Map<String, Boolean> elements = new LinkedHashMap<>();
        for (int i = 0; i < nameAndRetain.length; i += 2) {
            elements.put((String) nameAndRetain[i], (Boolean) nameAndRetain[i + 1]);
        }
        return elements;
    }

    static byte[] bareSessionTranscript() {
        CBORObject transcript = CBORObject.NewArray();
        transcript.Add(CBORObject.FromObject(new byte[] {0x01, 0x02}).WithTag(MdocConstants.TAG_ENCODED_CBOR));
        transcript.Add(CBORObject.FromObject(new byte[] {0x03, 0x04}).WithTag(MdocConstants.TAG_ENCODED_CBOR));
        transcript.Add(CBORObject.FromObject(new byte[] {0x05}));
        return transcript.EncodeToBytes();
    }

    static byte[] tag24(byte[] inner) {
        return CBORObject.FromObject(inner).WithTag(MdocConstants.TAG_ENCODED_CBOR).EncodeToBytes();
    }
}
