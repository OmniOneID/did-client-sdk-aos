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

public final class MdocConstants {
    private MdocConstants() {}

    public static final int TAG_ENCODED_CBOR = 24;

    public static final class IssuerSigned {
        public static final String NAME_SPACES = "nameSpaces";
        public static final String ISSUER_AUTH = "issuerAuth";

        private IssuerSigned() {}
    }

    public static final class Document {
        public static final String DOC_TYPE = "docType";
        public static final String ISSUER_SIGNED = "issuerSigned";
        public static final String DEVICE_SIGNED = "deviceSigned";

        private Document() {}
    }

    public static final class DeviceSigned {
        public static final String NAME_SPACES = "nameSpaces";
        public static final String DEVICE_AUTH = "deviceAuth";
        public static final String DEVICE_SIGNATURE = "deviceSignature";

        private DeviceSigned() {}
    }

    public static final class DeviceAuthentication {
        public static final String DEVICE_AUTHENTICATION = "DeviceAuthentication";
        public static final String OPENID4VP_HANDOVER = "OpenID4VPHandover";

        private DeviceAuthentication() {}
    }

    public static final class DeviceRequest {
        public static final String DOC_REQUESTS = "docRequests";
        public static final String ITEMS_REQUEST = "itemsRequest";
        public static final String DOC_TYPE = "docType";
        public static final String NAME_SPACES = "nameSpaces";

        private DeviceRequest() {}
    }

    public static final class DeviceResponse {
        public static final String VERSION = "version";
        public static final String DOCUMENTS = "documents";
        public static final String STATUS = "status";

        public static final String VERSION_1_0 = "1.0";
        public static final int STATUS_OK = 0;

        private DeviceResponse() {}
    }

    public static final class IssuerSignedItem {
        public static final String DIGEST_ID = "digestID";
        public static final String RANDOM = "random";
        public static final String ELEMENT_IDENTIFIER = "elementIdentifier";
        public static final String ELEMENT_VALUE = "elementValue";

        private IssuerSignedItem() {}
    }

    public static final class Mso {
        public static final String VERSION = "version";
        public static final String DIGEST_ALGORITHM = "digestAlgorithm";
        public static final String DOC_TYPE = "docType";
        public static final String VALUE_DIGESTS = "valueDigests";
        public static final String DEVICE_KEY_INFO = "deviceKeyInfo";
        public static final String VALIDITY_INFO = "validityInfo";

        public static final String VERSION_1_0 = "1.0";
        public static final String SHA_256 = "SHA-256";

        private Mso() {}
    }

    public static final class ValidityInfo {
        public static final String SIGNED = "signed";
        public static final String VALID_FROM = "validFrom";
        public static final String VALID_UNTIL = "validUntil";
        public static final String EXPECTED_UPDATE = "expectedUpdate";

        private ValidityInfo() {}
    }

    public static final class DeviceKeyInfo {
        public static final String DEVICE_KEY = "deviceKey";

        private DeviceKeyInfo() {}
    }

    public static final class Cose {
        public static final String SIGNATURE1 = "Signature1";

        public static final int SIGN1_ELEMENT_COUNT = 4;
        public static final int SIGN1_INDEX_PROTECTED = 0;
        public static final int SIGN1_INDEX_UNPROTECTED = 1;
        public static final int SIGN1_INDEX_PAYLOAD = 2;
        public static final int SIGN1_INDEX_SIGNATURE = 3;

        public static final int HEADER_ALG = 1;

        public static final int HEADER_KID = 4;

        public static final int HEADER_X5CHAIN = 33;

        public static final int ALG_ES256 = -7;

        private Cose() {}
    }

    public static final class CoseKey {
        public static final int LABEL_KTY = 1;
        public static final int LABEL_CRV = -1;
        public static final int LABEL_X = -2;
        public static final int LABEL_Y = -3;

        public static final int KTY_EC2 = 2;
        public static final int CRV_P256 = 1;

        private CoseKey() {}
    }
}
