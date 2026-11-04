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

import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.crypto.CoseKeyUtils;
import org.omnione.did.sdk.core.oid4vc.crypto.EcSignatureUtils;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocConstants;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

public final class CoseSign1Verifier {
    private static final String SC_PROVIDER = "SC";
    private static final byte[] EMPTY_AAD = new byte[0];

    static {
        if (Security.getProvider(SC_PROVIDER) == null) {
            Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
        }
    }

    private CoseSign1Verifier() {}

    @NonNull
    public static String kid(@NonNull CBORObject coseSign1) throws WalletCoreException {
        CBORObject label = CBORObject.FromObject(MdocConstants.Cose.HEADER_KID);

        CBORObject unprotected = coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_UNPROTECTED);
        if (unprotected != null && unprotected.getType() == CBORType.Map) {
            String kid = readKid(unprotected.get(label));
            if (kid != null) {
                return kid;
            }
        }
        CBORObject protectedHeader = protectedHeader(coseSign1);
        if (protectedHeader != null) {
            String kid = readKid(protectedHeader.get(label));
            if (kid != null) {
                return kid;
            }
        }
        boolean hasX5chain = hasX5chain(coseSign1, unprotected, protectedHeader);
        throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_NOT_FOUND_KID,
                "mdoc IssuerAuth has no kid (COSE header label " + MdocConstants.Cose.HEADER_KID + ")"
                        + (hasX5chain ? "; x5chain(33) is present but DID based trust is required" : ""));
    }

    public static int algorithm(@NonNull CBORObject coseSign1) {
        CBORObject protectedHeader = protectedHeader(coseSign1);
        if (protectedHeader != null) {
            CBORObject alg = protectedHeader.get(CBORObject.FromObject(MdocConstants.Cose.HEADER_ALG));
            if (alg != null && alg.getType() == CBORType.Integer) {
                return alg.AsInt32Value();
            }
        }
        return MdocConstants.Cose.ALG_ES256;
    }

    public static boolean verify(@NonNull CBORObject coseSign1, @NonNull PublicKey publicKey) throws Exception {
        CBORObject protectedBytes = coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_PROTECTED);
        CBORObject payload = coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_PAYLOAD);
        CBORObject signature = coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_SIGNATURE);
        if (protectedBytes == null || protectedBytes.getType() != CBORType.ByteString
                || signature == null || signature.getType() != CBORType.ByteString) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "mdoc IssuerAuth is not a well formed COSE_Sign1");
        }

        byte[] toBeSigned = buildSigStructure(
                protectedBytes.GetByteString(),
                EMPTY_AAD,
                payload == null || payload.isNull() ? EMPTY_AAD : payload.GetByteString());

        int coseAlg = algorithm(coseSign1);
        Signature verifier = Signature.getInstance(CoseKeyUtils.mapCoseAlgToJca(coseAlg), SC_PROVIDER);
        verifier.initVerify(publicKey);
        verifier.update(toBeSigned);
        boolean ok = verifier.verify(EcSignatureUtils.rawToDer(signature.GetByteString()));
        WalletLogger.getInstance().d("CoseSign1Verifier: signature verify=" + ok + " alg=" + coseAlg);
        return ok;
    }

    @NonNull
    public static byte[] buildSigStructure(@NonNull byte[] protectedHeaderBytes,
                                           @NonNull byte[] externalAad,
                                           @NonNull byte[] payload) {
        CBORObject sigStructure = CBORObject.NewArray();
        sigStructure.Add(MdocConstants.Cose.SIGNATURE1);
        sigStructure.Add(protectedHeaderBytes);
        sigStructure.Add(externalAad);
        sigStructure.Add(payload);
        return sigStructure.EncodeToBytes();
    }

    private static String readKid(CBORObject value) {
        if (value == null) {
            return null;
        }
        if (value.getType() == CBORType.ByteString) {
            return new String(value.GetByteString(), StandardCharsets.UTF_8);
        }
        if (value.getType() == CBORType.TextString) {
            return value.AsString();
        }
        return null;
    }

    private static CBORObject protectedHeader(@NonNull CBORObject coseSign1) {
        CBORObject protectedBytes = coseSign1.get(MdocConstants.Cose.SIGN1_INDEX_PROTECTED);
        if (protectedBytes == null || protectedBytes.getType() != CBORType.ByteString) {
            return null;
        }
        byte[] bytes = protectedBytes.GetByteString();
        if (bytes.length == 0) {
            return null;
        }
        try {
            CBORObject decoded = CBORObject.DecodeFromBytes(bytes);
            return decoded.getType() == CBORType.Map ? decoded : null;
        } catch (RuntimeException notCbor) {
            return null;
        }
    }

    private static boolean hasX5chain(@NonNull CBORObject coseSign1,
                                      CBORObject unprotected, CBORObject protectedHeader) {
        CBORObject label = CBORObject.FromObject(MdocConstants.Cose.HEADER_X5CHAIN);
        if (unprotected != null && unprotected.getType() == CBORType.Map && unprotected.get(label) != null) {
            return true;
        }
        return protectedHeader != null && protectedHeader.get(label) != null;
    }
}
