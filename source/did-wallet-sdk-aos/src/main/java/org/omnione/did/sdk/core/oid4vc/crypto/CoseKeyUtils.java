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

package org.omnione.did.sdk.core.oid4vc.crypto;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.upokecenter.cbor.CBORObject;
import com.upokecenter.cbor.CBORType;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocConstants;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.ec.EcUtils;
import org.omnione.did.sdk.utility.Encodings.Base64;

import java.security.PublicKey;

public final class CoseKeyUtils {
    private static final int P256_COORDINATE_LENGTH = 32;

    private CoseKeyUtils() {}

    @NonNull
    public static String mapCoseAlgToJca(int coseAlg) {
        switch (coseAlg) {
            case -35:
                return "SHA384withECDSA";
            case -36:
                return "SHA512withECDSA";
            case MdocConstants.Cose.ALG_ES256:
            default:
                return "SHA256withECDSA";
        }
    }

    public static int componentLength(int coseAlg) {
        switch (coseAlg) {
            case -35:
                return 48;
            case -36:
                return 66;
            case MdocConstants.Cose.ALG_ES256:
            default:
                return P256_COORDINATE_LENGTH;
        }
    }

    @NonNull
    public static byte[] compressedFromCoseKey(@NonNull CBORObject coseKey) throws WalletCoreException {
        if (coseKey.getType() != CBORType.Map) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "COSE_Key is not a CBOR map but " + coseKey.getType());
        }
        CBORObject kty = coseKey.get(CBORObject.FromObject(MdocConstants.CoseKey.LABEL_KTY));
        if (kty == null || kty.AsInt32Value() != MdocConstants.CoseKey.KTY_EC2) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "COSE_Key kty is not EC2(2) but " + kty);
        }
        CBORObject crv = coseKey.get(CBORObject.FromObject(MdocConstants.CoseKey.LABEL_CRV));
        int curve = crv != null ? crv.AsInt32Value() : MdocConstants.CoseKey.CRV_P256;
        if (curve != MdocConstants.CoseKey.CRV_P256) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "COSE_Key curve is not P-256(1) but " + curve);
        }
        CBORObject xObj = coseKey.get(CBORObject.FromObject(MdocConstants.CoseKey.LABEL_X));
        CBORObject yObj = coseKey.get(CBORObject.FromObject(MdocConstants.CoseKey.LABEL_Y));
        if (xObj == null || yObj == null
                || xObj.getType() != CBORType.ByteString || yObj.getType() != CBORType.ByteString) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "COSE_Key has no x/y byte strings");
        }
        return compress(xObj.GetByteString(), yObj.GetByteString());
    }

    @NonNull
    public static PublicKey publicKeyFromCoseKey(@NonNull CBORObject coseKey) throws Exception {
        return EcUtils.getPublicKey(compressedFromCoseKey(coseKey), EcType.EC_TYPE.SECP256_R1.getValue());
    }

    @NonNull
    public static byte[] compressedFromJwk(@NonNull JsonObject jwk) throws WalletCoreException {
        if (jwk.get("x") == null || jwk.get("y") == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "holder JWK has no x/y coordinates");
        }
        try {
            byte[] x = Base64.decodeUrl(jwk.get("x").getAsString());
            byte[] y = Base64.decodeUrl(jwk.get("y").getAsString());
            return compress(x, y);
        } catch (WalletCoreException e) {
            throw e;
        } catch (Exception e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "holder JWK coordinates are not valid base64url : " + e.getMessage());
        }
    }

    @NonNull
    private static byte[] compress(@NonNull byte[] x, @NonNull byte[] y) throws WalletCoreException {
        if (x.length > P256_COORDINATE_LENGTH || y.length > P256_COORDINATE_LENGTH) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VC_INVALID_CREDENTIAL_RESPONSE,
                    "EC coordinate longer than P-256 field size: x=" + x.length + " y=" + y.length);
        }
        byte[] compressed = new byte[1 + P256_COORDINATE_LENGTH];
        boolean yIsEven = y.length == 0 || (y[y.length - 1] & 1) == 0;
        compressed[0] = (byte) (yIsEven ? 0x02 : 0x03);
        System.arraycopy(x, 0, compressed, 1 + (P256_COORDINATE_LENGTH - x.length), x.length);
        return compressed;
    }
}
