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

import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocCborReader;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocClaimCodes;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocConstants;
import org.omnione.did.sdk.core.oid4vc.verify.CoseSign1Verifier;
import org.omnione.did.sdk.datamodel.oid4vc.Mdoc;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.Encodings.Base64;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MdocPresenter {

    private static final byte[] EMPTY_AAD = new byte[0];
    private static final int RAW_SIGNATURE_LENGTH = 64;
    private static final int SESSION_TRANSCRIPT_ELEMENT_COUNT = 3;
    private static final byte CBOR_TAG_24_INITIAL = (byte) 0xD8;
    private static final byte CBOR_TAG_24_VALUE = (byte) 0x18;
    private static final byte CBOR_ARRAY_OF_3 = (byte) 0x83;
    private static final byte CBOR_ARRAY_OF_4 = (byte) 0x84;
    private static final byte[] EMPTY_DEVICE_NAME_SPACES = CBORObject.NewOrderedMap().EncodeToBytes();

    private MdocPresenter() {}

    public static String createVpToken(@NonNull Mdoc mdoc,
                                       @NonNull List<String> claimCodes,
                                       String clientId,
                                       String nonce,
                                       @Nullable byte[] jwkThumbprint,
                                       String responseUri,
                                       @NonNull SignDigest signDigest)
            throws WalletException, WalletCoreException, UtilityException {

        CBORObject sessionTranscript = buildSessionTranscript(clientId, nonce, jwkThumbprint,
                responseUri);
        return Base64.encodeUrlString(createDeviceResponse(
                mdoc, claimCodes, sessionTranscript.EncodeToBytes(), signDigest));
    }

    @NonNull
    public static byte[] createDeviceResponse(@NonNull Mdoc mdoc,
                                              @NonNull List<String> claimCodes,
                                              @NonNull byte[] sessionTranscript,
                                              @NonNull SignDigest signDigest)
            throws WalletException, WalletCoreException, UtilityException {
        byte[] transcript = resolveSessionTranscript(sessionTranscript);
        CBORObject deviceSignature = createDeviceSignature(transcript, docTypeOf(mdoc), signDigest);
        CBORObject document = buildDocument(mdoc, claimCodes, deviceSignature,
                WalletCoreErrorCode.ERR_CODE_OID4VP_NO_MATCHED_CREDENTIALS);
        return encodeDeviceResponse(Collections.singletonList(document));
    }

    @NonNull
    public static byte[] resolveSessionTranscript(@Nullable byte[] sessionTranscript)
            throws WalletCoreException {
        if (sessionTranscript == null || sessionTranscript.length == 0) {
            throw invalidTranscript("sessionTranscript is empty");
        }
        byte first = sessionTranscript[0];
        if (first == CBOR_TAG_24_INITIAL) {
            if (sessionTranscript.length < 2 || sessionTranscript[1] != CBOR_TAG_24_VALUE) {
                throw invalidTranscript("tagged sessionTranscript is not #6.24");
            }
            CBORObject tagged = decodeTranscript(sessionTranscript, "SessionTranscriptBytes");
            if (tagged.getType() != CBORType.ByteString) {
                throw invalidTranscript("#6.24 content is not a byte string but " + tagged.getType());
            }
            byte[] inner = tagged.GetByteString();
            requireTranscriptArray(decodeTranscript(inner, "SessionTranscript inside #6.24"),
                    "SessionTranscript inside #6.24");
            return inner;
        }
        if (first == CBOR_ARRAY_OF_3) {
            requireTranscriptArray(decodeTranscript(sessionTranscript, "SessionTranscript"),
                    "SessionTranscript");
            return sessionTranscript;
        }
        throw invalidTranscript(String.format("first byte 0x%02X is neither #6.24 nor a 3-element array",
                first & 0xFF));
    }

    @NonNull
    public static CBORObject createDeviceSignature(@NonNull byte[] sessionTranscript,
                                                   @NonNull String docType,
                                                   @NonNull SignDigest signDigest)
            throws WalletException, WalletCoreException, UtilityException {
        byte[] deviceAuthenticationBytes = deviceAuthenticationBytes(sessionTranscript, docType);

        CBORObject protectedHeader = CBORObject.NewOrderedMap();
        protectedHeader.Add(MdocConstants.Cose.HEADER_ALG, MdocConstants.Cose.ALG_ES256);
        byte[] protectedHeaderBytes = protectedHeader.EncodeToBytes();

        byte[] toBeSigned = CoseSign1Verifier.buildSigStructure(protectedHeaderBytes, EMPTY_AAD,
                deviceAuthenticationBytes);

        byte[] signature = signDigest.sign(toBeSigned);
        if (signature == null || signature.length != RAW_SIGNATURE_LENGTH) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_FAILED_TO_VERIFY_SIGNATURE,
                    "DeviceAuth signature must be 64 bytes (raw R||S)");
        }

        CBORObject coseSign1 = CBORObject.NewArray();
        coseSign1.Add(protectedHeaderBytes);
        coseSign1.Add(CBORObject.NewOrderedMap());
        coseSign1.Add(CBORObject.Null);
        coseSign1.Add(signature);
        return coseSign1;
    }

    @NonNull
    public static byte[] deviceAuthenticationBytes(@NonNull byte[] sessionTranscript,
                                                   @NonNull String docType) {
        ByteArrayOutputStream deviceAuthentication = new ByteArrayOutputStream();
        deviceAuthentication.write(CBOR_ARRAY_OF_4);
        writeAll(deviceAuthentication,
                CBORObject.FromObject(MdocConstants.DeviceAuthentication.DEVICE_AUTHENTICATION).EncodeToBytes());
        writeAll(deviceAuthentication, sessionTranscript);
        writeAll(deviceAuthentication, CBORObject.FromObject(docType).EncodeToBytes());
        writeAll(deviceAuthentication, deviceNameSpacesBytes().EncodeToBytes());

        return CBORObject.FromObject(deviceAuthentication.toByteArray())
                .WithTag(MdocConstants.TAG_ENCODED_CBOR)
                .EncodeToBytes();
    }

    @NonNull
    public static CBORObject buildDocument(@NonNull Mdoc mdoc,
                                           @NonNull List<String> claimCodes,
                                           @NonNull CBORObject deviceSignature,
                                           @NonNull WalletCoreErrorCode unmatchedClaimCodesError)
            throws WalletCoreException {
        CBORObject issuerSigned = MdocCborReader.toIssuerSigned(MdocCborReader.decode(mdoc.getCompact()));
        CBORObject issuerAuth = MdocCborReader.issuerAuth(issuerSigned);
        CBORObject nameSpaces = MdocCborReader.nameSpaces(issuerSigned);

        CBORObject filteredNameSpaces = filterNameSpaces(nameSpaces, claimCodes, unmatchedClaimCodesError);

        CBORObject presentedIssuerSigned = CBORObject.NewOrderedMap();
        presentedIssuerSigned.Add(MdocConstants.IssuerSigned.NAME_SPACES, filteredNameSpaces);
        presentedIssuerSigned.Add(MdocConstants.IssuerSigned.ISSUER_AUTH, issuerAuth);

        CBORObject deviceAuth = CBORObject.NewOrderedMap();
        deviceAuth.Add(MdocConstants.DeviceSigned.DEVICE_SIGNATURE, deviceSignature);

        CBORObject deviceSigned = CBORObject.NewOrderedMap();
        deviceSigned.Add(MdocConstants.DeviceSigned.NAME_SPACES, deviceNameSpacesBytes());
        deviceSigned.Add(MdocConstants.DeviceSigned.DEVICE_AUTH, deviceAuth);

        CBORObject document = CBORObject.NewOrderedMap();
        document.Add(MdocConstants.Document.DOC_TYPE, docTypeOf(mdoc));
        document.Add(MdocConstants.Document.ISSUER_SIGNED, presentedIssuerSigned);
        document.Add(MdocConstants.Document.DEVICE_SIGNED, deviceSigned);
        return document;
    }

    @NonNull
    public static byte[] encodeDeviceResponse(@NonNull List<CBORObject> documents) {
        CBORObject documentArray = CBORObject.NewArray();
        for (CBORObject document : documents) {
            documentArray.Add(document);
        }

        CBORObject deviceResponse = CBORObject.NewOrderedMap();
        deviceResponse.Add(MdocConstants.DeviceResponse.VERSION, MdocConstants.DeviceResponse.VERSION_1_0);
        deviceResponse.Add(MdocConstants.DeviceResponse.DOCUMENTS, documentArray);
        deviceResponse.Add(MdocConstants.DeviceResponse.STATUS, MdocConstants.DeviceResponse.STATUS_OK);
        return deviceResponse.EncodeToBytes();
    }

    @NonNull
    public static String docTypeOf(@NonNull Mdoc mdoc) throws WalletCoreException {
        String docType = mdoc.getDocType();
        if (docType == null || docType.isEmpty()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc has no docType");
        }
        return docType;
    }

    @NonNull
    static CBORObject buildSessionTranscript(String clientId,
                                             String nonce,
                                             @Nullable byte[] jwkThumbprint,
                                             String responseUri) throws UtilityException {
        CBORObject handoverInfo = CBORObject.NewArray();
        handoverInfo.Add(clientId);
        handoverInfo.Add(nonce);
        handoverInfo.Add(jwkThumbprint != null
                ? CBORObject.FromObject(jwkThumbprint)
                : CBORObject.Null);
        handoverInfo.Add(responseUri);

        byte[] handoverInfoHash = DigestUtils.getDigest(handoverInfo.EncodeToBytes(),
                DigestEnum.DIGEST_ENUM.SHA_256);

        CBORObject handover = CBORObject.NewArray();
        handover.Add(MdocConstants.DeviceAuthentication.OPENID4VP_HANDOVER);
        handover.Add(handoverInfoHash);

        CBORObject sessionTranscript = CBORObject.NewArray();
        sessionTranscript.Add(CBORObject.Null);
        sessionTranscript.Add(CBORObject.Null);
        sessionTranscript.Add(handover);
        return sessionTranscript;
    }

    @NonNull
    private static CBORObject deviceNameSpacesBytes() {
        return CBORObject.FromObject(EMPTY_DEVICE_NAME_SPACES).WithTag(MdocConstants.TAG_ENCODED_CBOR);
    }

    @NonNull
    private static CBORObject filterNameSpaces(@NonNull CBORObject nameSpaces,
                                               @NonNull List<String> claimCodes,
                                               @NonNull WalletCoreErrorCode unmatchedClaimCodesError)
            throws WalletCoreException {
        Map<String, Set<String>> requestedElements = MdocClaimCodes.toRequestedElements(claimCodes);
        int requestedCount = 0;
        for (Set<String> elements : requestedElements.values()) {
            requestedCount += elements.size();
        }
        if (requestedCount == 0) {
            throw new WalletCoreException(unmatchedClaimCodesError, String.valueOf(claimCodes));
        }

        int keptCount = 0;
        Map<String, Set<String>> found = new LinkedHashMap<>();
        CBORObject filtered = CBORObject.NewOrderedMap();
        for (CBORObject nsKey : nameSpaces.getKeys()) {
            CBORObject entries = nameSpaces.get(nsKey);
            if (entries == null || entries.getType() != CBORType.Array) {
                continue;
            }
            String nameSpace = MdocCborReader.keyToString(nsKey);
            Set<String> elements = requestedElements.get(nameSpace);
            if (elements == null) {
                continue;
            }

            CBORObject kept = CBORObject.NewArray();
            for (int i = 0; i < entries.size(); i++) {
                CBORObject entry = entries.get(i);
                CBORObject item = CBORObject.DecodeFromBytes(MdocCborReader.issuerSignedItemBytes(entry));
                if (item.getType() != CBORType.Map) {
                    continue;
                }
                CBORObject elementIdentifier = item.get(MdocConstants.IssuerSignedItem.ELEMENT_IDENTIFIER);
                if (elementIdentifier == null || elementIdentifier.getType() != CBORType.TextString) {
                    continue;
                }
                if (elements.contains(elementIdentifier.AsString())) {
                    kept.Add(entry);
                    keptCount++;
                    found.computeIfAbsent(nameSpace, k -> new LinkedHashSet<>())
                            .add(elementIdentifier.AsString());
                }
            }
            if (kept.size() > 0) {
                filtered.Add(nsKey, kept);
            }
        }
        List<String> undisclosable = new ArrayList<>();
        for (String code : claimCodes) {
            String nameSpace = MdocClaimCodes.nameSpaceOf(code);
            String elementIdentifier = MdocClaimCodes.elementIdentifierOf(code);
            if (nameSpace == null || elementIdentifier == null) {
                continue;
            }
            Set<String> elements = found.get(nameSpace);
            if (elements == null || !elements.contains(elementIdentifier)) {
                undisclosable.add(code);
            }
        }
        if (!undisclosable.isEmpty()) {
            Collections.sort(undisclosable);
            throw new WalletCoreException(unmatchedClaimCodesError, "claim(s) "
                    + String.join(", ", undisclosable) + " are not disclosable in the credential");
        }
        if (keptCount != requestedCount) {
            throw new WalletCoreException(unmatchedClaimCodesError,
                    "requested " + requestedCount + " elements but the document holds " + keptCount
                            + " of " + claimCodes);
        }
        return filtered;
    }

    @NonNull
    private static CBORObject decodeTranscript(@NonNull byte[] bytes, @NonNull String what)
            throws WalletCoreException {
        try {
            return CBORObject.DecodeFromBytes(bytes);
        } catch (RuntimeException e) {
            throw invalidTranscript(what + " is not valid CBOR : " + e.getMessage());
        }
    }

    private static void requireTranscriptArray(@NonNull CBORObject value, @NonNull String what)
            throws WalletCoreException {
        if (value.getType() != CBORType.Array) {
            throw invalidTranscript(what + " is not an array but " + value.getType());
        }
        if (value.size() != SESSION_TRANSCRIPT_ELEMENT_COUNT) {
            throw invalidTranscript(what + " has " + value.size() + " elements, expected "
                    + SESSION_TRANSCRIPT_ELEMENT_COUNT);
        }
    }

    private static void writeAll(@NonNull ByteArrayOutputStream out, @NonNull byte[] bytes) {
        out.write(bytes, 0, bytes.length);
    }

    @NonNull
    private static WalletCoreException invalidTranscript(@NonNull String detail) {
        return new WalletCoreException(WalletCoreErrorCode.ERR_CODE_PROXIMITY_INVALID_SESSION_TRANSCRIPT, detail);
    }
}
