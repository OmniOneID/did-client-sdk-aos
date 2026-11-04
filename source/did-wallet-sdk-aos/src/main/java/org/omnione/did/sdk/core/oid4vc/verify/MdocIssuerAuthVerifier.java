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

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.crypto.CoseKeyUtils;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocCborReader;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocConstants;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocTdate;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public final class MdocIssuerAuthVerifier {
    private static final long CLOCK_SKEW_SECONDS = 300L;

    private MdocIssuerAuthVerifier() {}

    public interface IssuerKeyResolver {
        PublicKey resolve(String kid) throws Exception;
    }

    @NonNull
    public static CompletableFuture<Void> verify(@NonNull String compact, @NonNull String apiGatewayUrl,
                                                 JsonObject holderJwk, String expectedDocType) {
        return CompletableFuture.runAsync(() -> {
            try {
                verifyInternal(compact, kid -> DidVerificationKeyResolver.resolve(apiGatewayUrl, kid),
                        holderJwk, expectedDocType, System.currentTimeMillis() / 1000L);
            } catch (WalletCoreException e) {
                throw new CompletionException(e);
            } catch (Exception e) {
                throw new CompletionException(new WalletCoreException(
                        WalletCoreErrorCode.ERR_CODE_OID4VC_FAILED_TO_VERIFY_SIGNATURE, e.getMessage()));
            }
        });
    }

    static void verifyInternal(@NonNull String compact, @NonNull IssuerKeyResolver resolver,
                               JsonObject holderJwk, String expectedDocType, long nowEpochSec)
            throws Exception {
        CBORObject issuerSigned = MdocCborReader.toIssuerSigned(MdocCborReader.decode(compact));
        CBORObject issuerAuth = MdocCborReader.issuerAuth(issuerSigned);
        CBORObject nameSpaces = MdocCborReader.nameSpaces(issuerSigned);

        String kid = CoseSign1Verifier.kid(issuerAuth);
        WalletLogger.getInstance().d("MdocIssuerAuthVerifier: verify kid=" + kid);

        PublicKey issuerKey = resolver.resolve(kid);
        if (!CoseSign1Verifier.verify(issuerAuth, issuerKey)) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_FAILED_TO_VERIFY_SIGNATURE,
                    "mdoc IssuerAuth is not signed by the DID Document key of " + kid);
        }

        CBORObject mso = MdocCborReader.mso(issuerAuth);
        verifyDocType(mso, expectedDocType);
        verifyValueDigests(mso, nameSpaces);
        verifyValidityInfo(mso, nowEpochSec);
        verifyDeviceKeyBinding(mso, holderJwk);
    }

    private static void verifyDocType(@NonNull CBORObject mso, String expectedDocType)
            throws WalletCoreException {
        if (expectedDocType == null || expectedDocType.isEmpty()) {
            return;
        }
        CBORObject docType = mso.get(MdocConstants.Mso.DOC_TYPE);
        String actual = docType != null && docType.getType() == CBORType.TextString
                ? docType.AsString() : null;
        if (!expectedDocType.equals(actual)) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc docType mismatch: expected " + expectedDocType + " but got " + actual);
        }
    }

    private static void verifyValueDigests(@NonNull CBORObject mso, @NonNull CBORObject nameSpaces)
            throws Exception {
        CBORObject valueDigests = mso.get(MdocConstants.Mso.VALUE_DIGESTS);
        if (valueDigests == null || valueDigests.getType() != CBORType.Map) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc MSO has no valueDigests map");
        }
        MessageDigest digest = MessageDigest.getInstance(digestAlgorithm(mso));

        int verified = 0;

        for (CBORObject nsKey : nameSpaces.getKeys()) {
            String nameSpace = MdocCborReader.keyToString(nsKey);
            CBORObject entries = nameSpaces.get(nsKey);
            if (entries.getType() != CBORType.Array) {
                continue;
            }
            CBORObject nsDigests = valueDigests.get(nsKey);
            if (nsDigests == null || nsDigests.getType() != CBORType.Map) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                        "mdoc MSO has no valueDigests for namespace " + nameSpace);
            }

            for (int i = 0; i < entries.size(); i++) {
                CBORObject entry = entries.get(i);
                byte[] itemBytes = MdocCborReader.issuerSignedItemBytes(entry);
                CBORObject item = CBORObject.DecodeFromBytes(itemBytes);
                if (item.getType() != CBORType.Map) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                            "mdoc IssuerSignedItem is not a CBOR map in namespace " + nameSpace);
                }
                CBORObject digestIdObj = item.get(MdocConstants.IssuerSignedItem.DIGEST_ID);
                if (digestIdObj == null) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                            "mdoc IssuerSignedItem has no digestID in namespace " + nameSpace);
                }
                int digestId = digestIdObj.AsInt32Value();
                CBORObject expectedObj = nsDigests.get(CBORObject.FromObject(digestId));
                if (expectedObj == null || expectedObj.getType() != CBORType.ByteString) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                            "mdoc MSO has no digest for namespace " + nameSpace + " digestID " + digestId);
                }
                byte[] expected = expectedObj.GetByteString();

                byte[] computed = digest.digest(
                        CBORObject.FromObject(itemBytes).WithTag(MdocConstants.TAG_ENCODED_CBOR).EncodeToBytes());
                if (!MessageDigest.isEqual(computed, expected)) {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                            "mdoc valueDigests mismatch: namespace=" + nameSpace + " digestID=" + digestId
                                    + " element=" + elementIdentifier(item));
                }
                verified++;
            }
        }
        WalletLogger.getInstance().d("MdocIssuerAuthVerifier: verified " + verified + " value digest(s)");
    }

    private static void verifyValidityInfo(@NonNull CBORObject mso, long nowEpochSec)
            throws WalletCoreException {
        CBORObject validityInfo = mso.get(MdocConstants.Mso.VALIDITY_INFO);
        if (validityInfo == null || validityInfo.getType() != CBORType.Map) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc MSO has no validityInfo");
        }
        Long signed = MdocTdate.toEpochSeconds(validityInfo.get(MdocConstants.ValidityInfo.SIGNED));
        Long validFrom = MdocTdate.toEpochSeconds(validityInfo.get(MdocConstants.ValidityInfo.VALID_FROM));
        Long validUntil = MdocTdate.toEpochSeconds(validityInfo.get(MdocConstants.ValidityInfo.VALID_UNTIL));
        if (validFrom == null || validUntil == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc validityInfo has no validFrom/validUntil");
        }
        WalletLogger.getInstance().d("MdocIssuerAuthVerifier: validityInfo signed=" + signed
                + " validFrom=" + validFrom + " validUntil=" + validUntil + " now=" + nowEpochSec);

        if (validFrom > validUntil || (signed != null && signed > validUntil)) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc validityInfo is inconsistent: signed=" + signed
                            + " validFrom=" + validFrom + " validUntil=" + validUntil);
        }
        if (nowEpochSec + CLOCK_SKEW_SECONDS < validFrom) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc is not yet valid: validFrom=" + validFrom + " now=" + nowEpochSec);
        }
        if (nowEpochSec - CLOCK_SKEW_SECONDS > validUntil) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc is expired: validUntil=" + validUntil + " now=" + nowEpochSec);
        }
    }

    private static void verifyDeviceKeyBinding(@NonNull CBORObject mso, JsonObject holderJwk)
            throws WalletCoreException {
        if (holderJwk == null) {
            return;
        }
        byte[] holderCompressed = CoseKeyUtils.compressedFromJwk(holderJwk);

        CBORObject deviceKeyInfo = mso.get(MdocConstants.Mso.DEVICE_KEY_INFO);
        CBORObject deviceKey = deviceKeyInfo == null ? null
                : deviceKeyInfo.get(MdocConstants.DeviceKeyInfo.DEVICE_KEY);
        if (deviceKey == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc MSO has no deviceKeyInfo.deviceKey");
        }
        byte[] msoCompressed = CoseKeyUtils.compressedFromCoseKey(deviceKey);
        if (!Arrays.equals(msoCompressed, holderCompressed)) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc deviceKey does not match the holder proof key"
                            + " (mso=" + hexPrefix(msoCompressed) + " holder=" + hexPrefix(holderCompressed) + ")");
        }
        WalletLogger.getInstance().d("MdocIssuerAuthVerifier: deviceKey matches the holder proof key");
    }

    @NonNull
    private static String digestAlgorithm(@NonNull CBORObject mso) {
        CBORObject value = mso.get(MdocConstants.Mso.DIGEST_ALGORITHM);
        if (value == null || value.getType() != CBORType.TextString) {
            return MdocConstants.Mso.SHA_256;
        }
        String name = value.AsString().toUpperCase();

        if (name.startsWith("SHA") && name.length() > 3 && name.charAt(3) != '-') {
            return "SHA-" + name.substring(3);
        }
        return name;
    }

    @NonNull
    private static String elementIdentifier(@NonNull CBORObject item) {
        CBORObject value = item.get(MdocConstants.IssuerSignedItem.ELEMENT_IDENTIFIER);
        return value != null && value.getType() == CBORType.TextString ? value.AsString() : "unknown";
    }

    @NonNull
    private static String hexPrefix(@NonNull byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(8, bytes.length); i++) {
            sb.append(String.format("%02x", bytes[i]));
        }
        return sb.toString();
    }
}
