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

import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.utility.Encodings.Base64;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MdocCborReader {
    private MdocCborReader() {}

    @NonNull
    public static CBORObject decode(@NonNull String compact) throws WalletCoreException {
        byte[] bytes;
        try {
            bytes = Base64.decodeUrl(compact);
        } catch (RuntimeException urlSafeFailure) {
            try {
                bytes = Base64.decode(compact, Base64.DEFAULT);
            } catch (RuntimeException standardFailure) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                        "mdoc credential is not valid base64 : " + standardFailure.getMessage());
            }
        }
        try {
            return CBORObject.DecodeFromBytes(bytes);
        } catch (RuntimeException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc credential is not valid CBOR : " + e.getMessage());
        }
    }

    @NonNull
    public static CBORObject toIssuerSigned(@NonNull CBORObject decoded) throws WalletCoreException {
        if (decoded.getType() != CBORType.Map) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc credential is not a CBOR map but " + decoded.getType());
        }
        if (decoded.get(MdocConstants.IssuerSigned.ISSUER_AUTH) != null) {
            return decoded;
        }
        CBORObject documents = decoded.get(MdocConstants.DeviceResponse.DOCUMENTS);
        if (documents != null && documents.getType() == CBORType.Array && documents.size() > 0) {
            CBORObject issuerSigned = documents.get(0).get(MdocConstants.Document.ISSUER_SIGNED);
            if (issuerSigned != null) {
                return issuerSigned;
            }
        }
        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                "mdoc credential has neither issuerAuth nor documents[0].issuerSigned");
    }

    @NonNull
    public static CBORObject issuerAuth(@NonNull CBORObject issuerSigned) throws WalletCoreException {
        CBORObject issuerAuth = issuerSigned.get(MdocConstants.IssuerSigned.ISSUER_AUTH);
        if (issuerAuth == null || issuerAuth.getType() != CBORType.Array
                || issuerAuth.size() < MdocConstants.Cose.SIGN1_ELEMENT_COUNT) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_FORMAT,
                    "mdoc issuerAuth is not a COSE_Sign1 array of "
                            + MdocConstants.Cose.SIGN1_ELEMENT_COUNT + " elements");
        }
        return issuerAuth;
    }

    @NonNull
    public static CBORObject nameSpaces(@NonNull CBORObject issuerSigned) throws WalletCoreException {
        CBORObject nameSpaces = issuerSigned.get(MdocConstants.IssuerSigned.NAME_SPACES);
        if (nameSpaces == null || nameSpaces.getType() != CBORType.Map) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_UNSUPPORTED_FORMAT,
                    "mdoc issuerSigned has no nameSpaces map");
        }
        return nameSpaces;
    }

    @NonNull
    public static CBORObject mso(@NonNull CBORObject issuerAuth) throws WalletCoreException {
        CBORObject payload = issuerAuth.get(MdocConstants.Cose.SIGN1_INDEX_PAYLOAD);
        if (payload == null || payload.isNull() || payload.getType() != CBORType.ByteString) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc IssuerAuth has no payload byte string");
        }
        CBORObject mso;
        try {
            mso = CBORObject.DecodeFromBytes(payload.GetByteString());
        } catch (RuntimeException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc MSO payload is not valid CBOR : " + e.getMessage());
        }
        mso = unwrapTag24(mso);
        if (mso.getType() != CBORType.Map) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc MSO is not a CBOR map but " + mso.getType());
        }
        return mso;
    }

    @NonNull
    public static CBORObject unwrapTag24(@NonNull CBORObject value) {
        if (value.isTagged() && value.HasMostInnerTag(MdocConstants.TAG_ENCODED_CBOR)) {
            return CBORObject.DecodeFromBytes(value.GetByteString());
        }
        return value;
    }

    @NonNull
    public static byte[] issuerSignedItemBytes(@NonNull CBORObject entry) {
        if (entry.isTagged() && entry.HasMostInnerTag(MdocConstants.TAG_ENCODED_CBOR)) {
            return entry.GetByteString();
        }
        if (entry.getType() == CBORType.ByteString) {
            return entry.GetByteString();
        }
        return entry.EncodeToBytes();
    }

    public static Object toJava(CBORObject value) {
        if (value == null || value.isNull()) {
            return null;
        }
        CBORObject untagged = value;
        if (untagged.isTagged()) {
            if (untagged.HasMostInnerTag(MdocConstants.TAG_ENCODED_CBOR)) {
                return toJava(unwrapTag24(untagged));
            }
            untagged = untagged.Untag();
        }
        switch (untagged.getType()) {
            case Boolean:
                return untagged.AsBoolean();
            case ByteString:
                return untagged.GetByteString();
            case TextString:
                return untagged.AsString();
            case Integer:
                try {
                    return untagged.AsInt64Value();
                } catch (ArithmeticException outOfLongRange) {
                    return untagged.toString();
                }
            case FloatingPoint:
                return untagged.AsDoubleValue();
            case Array: {
                List<Object> list = new ArrayList<>(untagged.size());
                for (int i = 0; i < untagged.size(); i++) {
                    list.add(toJava(untagged.get(i)));
                }
                return list;
            }
            case Map: {
                Map<String, Object> map = new LinkedHashMap<>();
                for (CBORObject key : untagged.getKeys()) {
                    map.put(keyToString(key), toJava(untagged.get(key)));
                }
                return map;
            }
            default:
                return untagged.toString();
        }
    }

    @NonNull
    public static String keyToString(@NonNull CBORObject key) {
        if (key.getType() == CBORType.TextString) {
            return key.AsString();
        }
        return key.toString();
    }
}
