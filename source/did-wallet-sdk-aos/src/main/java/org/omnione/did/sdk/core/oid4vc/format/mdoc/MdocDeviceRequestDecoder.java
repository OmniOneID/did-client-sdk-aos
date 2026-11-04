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

import androidx.annotation.NonNull;

import com.upokecenter.cbor.CBOREncodeOptions;
import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MdocDeviceRequestDecoder {

    private static final CBOREncodeOptions KEEP_KEY_ORDER = new CBOREncodeOptions("keepkeyorder=true");

    private MdocDeviceRequestDecoder() {}

    public static final class DocRequest {
        private final String docType;
        private final Map<String, Map<String, Boolean>> nameSpaces;

        DocRequest(String docType, Map<String, Map<String, Boolean>> nameSpaces) {
            this.docType = docType;
            this.nameSpaces = nameSpaces;
        }

        public String getDocType() {
            return docType;
        }

        public Map<String, Map<String, Boolean>> getNameSpaces() {
            return nameSpaces;
        }
    }

    @NonNull
    public static List<DocRequest> decode(byte[] deviceRequest) throws WalletCoreException {
        if (deviceRequest == null || deviceRequest.length == 0) {
            throw invalid("DeviceRequest is empty");
        }
        CBORObject request;
        try {
            request = CBORObject.DecodeFromBytes(deviceRequest, KEEP_KEY_ORDER);
        } catch (RuntimeException e) {
            throw invalid("DeviceRequest is not valid CBOR : " + e.getMessage());
        }
        requireMap(request, "DeviceRequest");

        CBORObject docRequests = request.get(MdocConstants.DeviceRequest.DOC_REQUESTS);
        if (docRequests == null || docRequests.getType() != CBORType.Array || docRequests.size() == 0) {
            throw invalid("DeviceRequest has no docRequests");
        }

        List<DocRequest> result = new ArrayList<>(docRequests.size());
        for (int i = 0; i < docRequests.size(); i++) {
            result.add(decodeDocRequest(docRequests.get(i), i));
        }
        return Collections.unmodifiableList(result);
    }

    @NonNull
    private static DocRequest decodeDocRequest(@NonNull CBORObject docRequest, int index)
            throws WalletCoreException {
        String where = "docRequests[" + index + "]";
        requireMap(docRequest, where);

        CBORObject wrapped = docRequest.get(MdocConstants.DeviceRequest.ITEMS_REQUEST);
        if (wrapped == null) {
            throw invalid(where + " has no itemsRequest");
        }
        if (!wrapped.isTagged() || !wrapped.HasMostInnerTag(MdocConstants.TAG_ENCODED_CBOR)
                || wrapped.getType() != CBORType.ByteString) {
            throw invalid(where + ".itemsRequest is not #6.24(bstr)");
        }
        CBORObject itemsRequest;
        try {
            itemsRequest = CBORObject.DecodeFromBytes(wrapped.GetByteString(), KEEP_KEY_ORDER);
        } catch (RuntimeException e) {
            throw invalid(where + ".itemsRequest is not valid CBOR : " + e.getMessage());
        }
        requireMap(itemsRequest, where + ".itemsRequest");

        CBORObject docType = itemsRequest.get(MdocConstants.DeviceRequest.DOC_TYPE);
        if (docType == null || docType.getType() != CBORType.TextString) {
            throw invalid(where + ".itemsRequest has no docType");
        }

        CBORObject nameSpaces = itemsRequest.get(MdocConstants.DeviceRequest.NAME_SPACES);
        if (nameSpaces == null) {
            throw invalid(where + ".itemsRequest has no nameSpaces");
        }
        requireMap(nameSpaces, where + ".itemsRequest.nameSpaces");

        Map<String, Map<String, Boolean>> decoded = new LinkedHashMap<>();
        for (CBORObject nsKey : nameSpaces.getKeys()) {
            String nameSpace = requireTextString(nsKey, where + " nameSpace key");
            CBORObject elements = nameSpaces.get(nsKey);
            requireMap(elements, where + ".nameSpaces[" + nameSpace + "]");

            Map<String, Boolean> perNameSpace = new LinkedHashMap<>();
            for (CBORObject elementKey : elements.getKeys()) {
                String elementIdentifier = requireTextString(elementKey,
                        where + ".nameSpaces[" + nameSpace + "] element key");
                CBORObject intentToRetain = elements.get(elementKey);
                if (intentToRetain == null || intentToRetain.getType() != CBORType.Boolean) {
                    throw invalid(where + ".nameSpaces[" + nameSpace + "][" + elementIdentifier
                            + "] is not a boolean");
                }
                perNameSpace.put(elementIdentifier, intentToRetain.AsBoolean());
            }
            decoded.put(nameSpace, Collections.unmodifiableMap(perNameSpace));
        }
        return new DocRequest(docType.AsString(), Collections.unmodifiableMap(decoded));
    }

    private static void requireMap(@NonNull CBORObject value, @NonNull String what)
            throws WalletCoreException {
        if (value.getType() != CBORType.Map) {
            throw invalid(what + " is not a map but " + value.getType());
        }
    }

    @NonNull
    private static String requireTextString(@NonNull CBORObject value, @NonNull String what)
            throws WalletCoreException {
        if (value.getType() != CBORType.TextString) {
            throw invalid(what + " is not a text string but " + value.getType());
        }
        return value.AsString();
    }

    @NonNull
    private static WalletCoreException invalid(@NonNull String detail) {
        return new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_DEVICE_REQUEST, detail);
    }
}
