/*
 * Copyright 2024-2025 OmniOne.
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

package org.omnione.did.sdk.utility.DataModels.ec;

import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.spec.ECNamedCurveParameterSpec;
import org.spongycastle.jce.spec.ECPublicKeySpec;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class EcUtils {
    public static BigInteger getOrCreatePrivKeyBigInteger(byte[] value) {
        if (null != value) {
            if (((value[0]) & 0x80) != 0) {
                return new BigInteger(1, value);
            }
            return new BigInteger(value);
        }
        String curveName = "secp256r1";
        ECNamedCurveParameterSpec ecParams = ECNamedCurveTable.getParameterSpec(curveName);
        BigInteger n = ecParams.getN();

        int nBitLength = n.bitLength();
        BigInteger privBigInteger;
        do {
            SecureRandom mSecRandom = new SecureRandom();
            byte[] bytes = new byte[nBitLength / 8];
            mSecRandom.nextBytes(bytes);
            bytes[0] = (byte) (bytes[0] & 0x7F);
            privBigInteger = new BigInteger(bytes);
        }
        while (privBigInteger.equals(BigInteger.ZERO) || (privBigInteger.compareTo(n) >= 0));
        return privBigInteger;
    }

    public static byte[] getPubKey(BigInteger bnum){
        String curveName = "secp256r1";
        ECNamedCurveParameterSpec ecParams = ECNamedCurveTable.getParameterSpec(curveName);

        ECPoint G = ecParams.getG();
        ECPoint Q = G.multiply(bnum);
        ECPoint compressedQ = compressPoint(Q);

        byte[] publicKeyBytes = new byte[33];
        byte[] xBytes = compressedQ.getXCoord().getEncoded();
        byte prefixByte = (byte) (compressedQ.getYCoord().toBigInteger().testBit(0) ? 0x03 : 0x02);

        publicKeyBytes[0] = prefixByte;
        System.arraycopy(xBytes, 0, publicKeyBytes, 1, xBytes.length);

        return publicKeyBytes;
    }
    public static ECPoint compressPoint(ECPoint point) {
        ECCurve curve = point.getCurve();
        ECFieldElement x = point.getX();
        ECFieldElement y = point.getY();

        byte[] xBytes = x.getEncoded();
        byte[] yBytes = y.getEncoded();

        byte prefixByte = (byte) (y.toBigInteger().testBit(0) ? 0x03 : 0x02);

        byte[] compressedBytes = new byte[xBytes.length + 1];
        compressedBytes[0] = prefixByte;
        System.arraycopy(xBytes, 0, compressedBytes, 1, xBytes.length);

        ECPoint compressedPoint = curve.decodePoint(compressedBytes);
        return compressedPoint;
    }

    public static ECPublicKey getPublicKey(byte[] pubKeyBytes, String ecType) throws GeneralSecurityException {
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(ecType); //todo : Secp256r1, secp256r1 확인
        ECPoint ecPoint = spec.getCurve().decodePoint(pubKeyBytes);
        ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, spec);
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "SC");
        return (ECPublicKey) keyFactory.generatePublic(publicKeySpec);
    }

    public static ECPrivateKey getPrivateKey(byte[] privateKey, String ecType) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, InvalidParameterSpecException {
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC", "SC");
        parameters.init(new ECGenParameterSpec(ecType));
        ECParameterSpec ecParameterSpec = parameters.getParameterSpec(ECParameterSpec.class);
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(new BigInteger(privateKey), ecParameterSpec);

        return (ECPrivateKey) KeyFactory.getInstance("ECDSA", "SC").generatePrivate(ecPrivateKeySpec);
    }

    public static byte[] getBytes(BigInteger bigint) {
        byte[] result = new byte[32];
        byte[] bytes = bigint.toByteArray();
        if (bytes.length <= result.length) {
            System.arraycopy(bytes, 0, result, result.length - bytes.length, bytes.length);
        } else {
            assert bytes.length == 33 && bytes[0] == 0;
            System.arraycopy(bytes, 1, result, 0, bytes.length - 1);
        }
        return result;
    }

    public static byte[] convertCompressedPrivateKey(byte[] uncompressedPrivateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(uncompressedPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        if (privateKey instanceof ECPrivateKey) {
            ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
            BigInteger s = ecPrivateKey.getS();
            return s.toByteArray();
        } else {
            return null;
        }
    }
}
