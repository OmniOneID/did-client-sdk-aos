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

package org.omnione.did.sdk.core.oid4vc.verify;

import com.google.gson.JsonObject;
import com.upokecenter.cbor.CBORObject;

import org.omnione.did.sdk.core.oid4vc.crypto.EcSignatureUtils;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocConstants;
import org.omnione.did.sdk.utility.Encodings.Base64;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

final class MdocFixtures {
    static final String SC_PROVIDER = "SC";
    static final int COORDINATE_LENGTH = 32;
    static final String DEFAULT_DOC_TYPE = "org.iso.18013.5.1.mDL";
    static final String DEFAULT_NAMESPACE = "org.iso.18013.5.1";
    static final String DEFAULT_KID = "did:omn:issuer?versionId=1#assert";

    enum ItemEncoding {
        TAG24,

        BARE
    }

    enum KidPlacement {
        UNPROTECTED_BSTR,
        UNPROTECTED_TSTR,
        PROTECTED_BSTR,
        ABSENT,
        X5CHAIN_ONLY
    }

    static {
        if (Security.getProvider(SC_PROVIDER) == null) {
            Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
        }
    }

    private MdocFixtures() {}

    static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", SC_PROVIDER);
        generator.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
        return generator.generateKeyPair();
    }

    static final class Builder {
        private final KeyPair issuerKey;
        private KeyPair deviceKey;
        private ItemEncoding itemEncoding = ItemEncoding.TAG24;
        private KidPlacement kidPlacement = KidPlacement.UNPROTECTED_BSTR;
        private String kid = DEFAULT_KID;
        private String docType = DEFAULT_DOC_TYPE;
        private String nameSpace = DEFAULT_NAMESPACE;
        private String digestAlgorithm = MdocConstants.Mso.SHA_256;
        private boolean includeValidityInfo = true;
        private long signedEpochSec = Instant.parse("2026-08-01T00:00:00Z").getEpochSecond();
        private long validFromEpochSec = Instant.parse("2026-08-01T00:00:00Z").getEpochSecond();
        private long validUntilEpochSec = Instant.parse("2027-08-01T00:00:00Z").getEpochSecond();
        private boolean includeDeviceKey = true;
        private boolean wrapInDeviceResponse = false;
        private boolean tamperFirstElementValue = false;
        private String statusListUri;
        private long statusListIdx;
        private final Map<String, Object> elements = new LinkedHashMap<>();

        Builder(KeyPair issuerKey, KeyPair deviceKey) {
            this.issuerKey = issuerKey;
            this.deviceKey = deviceKey;
            elements.put("family_name", "Kim");
            elements.put("given_name", "Raon");
            elements.put("birth_date", "1990-01-01");
        }

        Builder itemEncoding(ItemEncoding value) { this.itemEncoding = value; return this; }
        Builder kidPlacement(KidPlacement value) { this.kidPlacement = value; return this; }
        Builder kid(String value) { this.kid = value; return this; }
        Builder docType(String value) { this.docType = value; return this; }
        Builder digestAlgorithm(String value) { this.digestAlgorithm = value; return this; }
        Builder deviceKey(KeyPair value) { this.deviceKey = value; return this; }
        Builder omitDeviceKey() { this.includeDeviceKey = false; return this; }
        Builder omitValidityInfo() { this.includeValidityInfo = false; return this; }
        Builder validity(long from, long until) {
            this.validFromEpochSec = from;
            this.validUntilEpochSec = until;
            this.signedEpochSec = Math.min(from, this.signedEpochSec);
            return this;
        }
        Builder wrapInDeviceResponse() { this.wrapInDeviceResponse = true; return this; }
        Builder tamperFirstElementValue() { this.tamperFirstElementValue = true; return this; }
        Builder statusList(String uri, long idx) {
            this.statusListUri = uri;
            this.statusListIdx = idx;
            return this;
        }
        Builder element(String id, Object value) { elements.put(id, value); return this; }

        String build() throws Exception {
            CBORObject entries = CBORObject.NewArray();
            CBORObject nsDigests = CBORObject.NewMap();
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);

            int digestId = 0;
            for (Map.Entry<String, Object> element : elements.entrySet()) {
                CBORObject item = CBORObject.NewMap();
                item.set(MdocConstants.IssuerSignedItem.DIGEST_ID, CBORObject.FromObject(digestId));
                item.set(MdocConstants.IssuerSignedItem.RANDOM, CBORObject.FromObject(random(16)));
                item.set(MdocConstants.IssuerSignedItem.ELEMENT_IDENTIFIER,
                        CBORObject.FromObject(element.getKey()));
                item.set(MdocConstants.IssuerSignedItem.ELEMENT_VALUE,
                        CBORObject.FromObject(element.getValue()));

                byte[] itemBytes = item.EncodeToBytes();
                CBORObject entry = itemEncoding == ItemEncoding.TAG24
                        ? CBORObject.FromObject(itemBytes).WithTag(MdocConstants.TAG_ENCODED_CBOR)
                        : CBORObject.FromObject(itemBytes);

                byte[] digestInput = itemEncoding == ItemEncoding.TAG24
                        ? entry.EncodeToBytes()
                        : entry.EncodeToBytes();
                nsDigests.set(CBORObject.FromObject(digestId), CBORObject.FromObject(digest.digest(digestInput)));

                if (tamperFirstElementValue && digestId == 0) {
                    item.set(MdocConstants.IssuerSignedItem.ELEMENT_VALUE, CBORObject.FromObject("TAMPERED"));
                    byte[] tampered = item.EncodeToBytes();
                    entry = itemEncoding == ItemEncoding.TAG24
                            ? CBORObject.FromObject(tampered).WithTag(MdocConstants.TAG_ENCODED_CBOR)
                            : CBORObject.FromObject(tampered);
                }
                entries.Add(entry);
                digestId++;
            }

            CBORObject nameSpaces = CBORObject.NewMap();
            nameSpaces.set(nameSpace, entries);

            CBORObject valueDigests = CBORObject.NewMap();
            valueDigests.set(nameSpace, nsDigests);

            CBORObject mso = CBORObject.NewMap();
            mso.set(MdocConstants.Mso.VERSION, CBORObject.FromObject(MdocConstants.Mso.VERSION_1_0));
            mso.set(MdocConstants.Mso.DIGEST_ALGORITHM, CBORObject.FromObject(digestAlgorithm));
            mso.set(MdocConstants.Mso.DOC_TYPE, CBORObject.FromObject(docType));
            mso.set(MdocConstants.Mso.VALUE_DIGESTS, valueDigests);
            if (includeDeviceKey) {
                CBORObject deviceKeyInfo = CBORObject.NewMap();
                deviceKeyInfo.set(MdocConstants.DeviceKeyInfo.DEVICE_KEY,
                        coseKey((ECPublicKey) deviceKey.getPublic()));
                mso.set(MdocConstants.Mso.DEVICE_KEY_INFO, deviceKeyInfo);
            }
            if (includeValidityInfo) {
                CBORObject validityInfo = CBORObject.NewMap();
                validityInfo.set(MdocConstants.ValidityInfo.SIGNED, tdate(signedEpochSec));
                validityInfo.set(MdocConstants.ValidityInfo.VALID_FROM, tdate(validFromEpochSec));
                validityInfo.set(MdocConstants.ValidityInfo.VALID_UNTIL, tdate(validUntilEpochSec));
                mso.set(MdocConstants.Mso.VALIDITY_INFO, validityInfo);
            }
            if (statusListUri != null) {
                CBORObject statusList = CBORObject.NewMap();
                statusList.set("uri", CBORObject.FromObject(statusListUri));
                statusList.set("idx", CBORObject.FromObject(statusListIdx));
                CBORObject status = CBORObject.NewMap();
                status.set("status_list", statusList);
                mso.set("status", status);
            }

            byte[] msoPayload = CBORObject.FromObject(mso.EncodeToBytes())
                    .WithTag(MdocConstants.TAG_ENCODED_CBOR).EncodeToBytes();

            CBORObject protectedMap = CBORObject.NewMap();
            protectedMap.set(CBORObject.FromObject(MdocConstants.Cose.HEADER_ALG),
                    CBORObject.FromObject(MdocConstants.Cose.ALG_ES256));
            if (kidPlacement == KidPlacement.PROTECTED_BSTR) {
                protectedMap.set(CBORObject.FromObject(MdocConstants.Cose.HEADER_KID),
                        CBORObject.FromObject(kid.getBytes("UTF-8")));
            }
            byte[] protectedBytes = protectedMap.EncodeToBytes();

            CBORObject unprotected = CBORObject.NewMap();
            switch (kidPlacement) {
                case UNPROTECTED_BSTR:
                    unprotected.set(CBORObject.FromObject(MdocConstants.Cose.HEADER_KID),
                            CBORObject.FromObject(kid.getBytes("UTF-8")));
                    break;
                case UNPROTECTED_TSTR:
                    unprotected.set(CBORObject.FromObject(MdocConstants.Cose.HEADER_KID),
                            CBORObject.FromObject(kid));
                    break;
                case X5CHAIN_ONLY:
                    unprotected.set(CBORObject.FromObject(MdocConstants.Cose.HEADER_X5CHAIN),
                            CBORObject.FromObject(random(64)));
                    break;
                default:
                    break;
            }

            byte[] toBeSigned = CoseSign1Verifier.buildSigStructure(protectedBytes, new byte[0], msoPayload);
            Signature signer = Signature.getInstance("SHA256withECDSA", SC_PROVIDER);
            signer.initSign(issuerKey.getPrivate());
            signer.update(toBeSigned);
            byte[] rawSignature = EcSignatureUtils.derToRaw(signer.sign(), COORDINATE_LENGTH);

            CBORObject issuerAuth = CBORObject.NewArray();
            issuerAuth.Add(CBORObject.FromObject(protectedBytes));
            issuerAuth.Add(unprotected);
            issuerAuth.Add(CBORObject.FromObject(msoPayload));
            issuerAuth.Add(CBORObject.FromObject(rawSignature));

            CBORObject issuerSigned = CBORObject.NewMap();
            issuerSigned.set(MdocConstants.IssuerSigned.NAME_SPACES, nameSpaces);
            issuerSigned.set(MdocConstants.IssuerSigned.ISSUER_AUTH, issuerAuth);

            CBORObject root = issuerSigned;
            if (wrapInDeviceResponse) {
                CBORObject document = CBORObject.NewMap();
                document.set(MdocConstants.Document.DOC_TYPE, CBORObject.FromObject(docType));
                document.set(MdocConstants.Document.ISSUER_SIGNED, issuerSigned);
                CBORObject documents = CBORObject.NewArray();
                documents.Add(document);
                root = CBORObject.NewMap();
                root.set("version", CBORObject.FromObject("1.0"));
                root.set(MdocConstants.DeviceResponse.DOCUMENTS, documents);
                root.set("status", CBORObject.FromObject(0));
            }
            return Base64.encodeUrlString(root.EncodeToBytes());
        }
    }

    static JsonObject holderJwk(ECPublicKey publicKey) {
        JsonObject jwk = new JsonObject();
        jwk.addProperty("kty", "EC");
        jwk.addProperty("crv", "P-256");
        jwk.addProperty("x", Base64.encodeUrlString(coordinate(publicKey.getW().getAffineX())));
        jwk.addProperty("y", Base64.encodeUrlString(coordinate(publicKey.getW().getAffineY())));
        return jwk;
    }

    static CBORObject coseKey(ECPublicKey publicKey) {
        CBORObject key = CBORObject.NewMap();
        key.set(CBORObject.FromObject(MdocConstants.CoseKey.LABEL_KTY),
                CBORObject.FromObject(MdocConstants.CoseKey.KTY_EC2));
        key.set(CBORObject.FromObject(MdocConstants.CoseKey.LABEL_CRV),
                CBORObject.FromObject(MdocConstants.CoseKey.CRV_P256));
        key.set(CBORObject.FromObject(MdocConstants.CoseKey.LABEL_X),
                CBORObject.FromObject(coordinate(publicKey.getW().getAffineX())));
        key.set(CBORObject.FromObject(MdocConstants.CoseKey.LABEL_Y),
                CBORObject.FromObject(coordinate(publicKey.getW().getAffineY())));
        return key;
    }

    static byte[] coordinate(BigInteger value) {
        byte[] bytes = value.toByteArray();
        if (bytes.length == COORDINATE_LENGTH) {
            return bytes;
        }
        byte[] padded = new byte[COORDINATE_LENGTH];
        if (bytes.length > COORDINATE_LENGTH) {
            System.arraycopy(bytes, bytes.length - COORDINATE_LENGTH, padded, 0, COORDINATE_LENGTH);
        } else {
            System.arraycopy(bytes, 0, padded, COORDINATE_LENGTH - bytes.length, bytes.length);
        }
        return padded;
    }

    private static CBORObject tdate(long epochSeconds) {
        return CBORObject.FromObject(Instant.ofEpochSecond(epochSeconds).toString()).WithTag(0);
    }

    private static byte[] random(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}
