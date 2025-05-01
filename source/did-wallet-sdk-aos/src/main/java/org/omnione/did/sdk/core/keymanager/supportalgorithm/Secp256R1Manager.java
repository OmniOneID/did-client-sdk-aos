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

package org.omnione.did.sdk.core.keymanager.supportalgorithm;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyGenerationInfo;
import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DataModels.ec.EcUtils;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Integer;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.x9.X9IntegerConverter;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.spec.ECNamedCurveParameterSpec;
import org.spongycastle.math.ec.ECAlgorithms;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Secp256R1Manager implements SignableInterface {

    static {
        Security.removeProvider("SC");
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }
    private String SIGNATURE_MANAGER_ALIAS_PREFIX = "opendid_wallet_signature_";

    public KeyGenerationInfo generateKey() {
        KeyGenerationInfo keyGenerationInfo = new KeyGenerationInfo();
        keyGenerationInfo.setAlgoType(AlgorithmType.ALGORITHM_TYPE.SECP256R1);
        BigInteger bNum = getOrCreatePrivKeyBigInteger(null);
        keyGenerationInfo.setPrivateKey((MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, bNum.toByteArray())));
        keyGenerationInfo.setPublicKey((MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, getPubKey(bNum))));

        return keyGenerationInfo;
    }

    @Override
    public byte[] sign(byte[] privateKey, byte[] digest) throws WalletCoreException {
        try {
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
            java.security.PrivateKey restoredPrivateKey = getECPrivateKeyFromBytes(privateKey, ecSpec);
            BigInteger bNum = getOrCreatePrivKeyBigInteger(privateKey);
            byte[] pubKey = getPubKey(bNum);
            Signature ecdsaSign = Signature.getInstance("NoneWithECDSA");
            ecdsaSign.initSign(restoredPrivateKey);
            ecdsaSign.update(digest);
            byte[] signature = ecdsaSign.sign();
            return compressedSignValue(signature, pubKey, digest);
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException | InvalidKeySpecException | IOException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SIGNABLE_CREATE_SIGNATURE);
        }
    }

    private java.security.PrivateKey getECPrivateKeyFromBytes(byte[] privateKeyBytes, ECGenParameterSpec ecSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        BigInteger s = new BigInteger(1, privateKeyBytes);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(ecSpec);
        KeyPair kp = kpg.generateKeyPair();
        ECParameterSpec params = ((java.security.interfaces.ECPublicKey) kp.getPublic()).getParams();

        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(s, params);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(privateKeySpec);
    }

    @Override
    public boolean verify(byte[] publicKey, byte[] digest, byte[] signature) throws WalletCoreException {
        try {
            if (signature.length != 65) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SIGNABLE_INVALID_PARAMETER, "signature");
            }
            byte[] unCompressedSignature = compressedToDer(signature);
            PublicKey pubKey = EcUtils.getPublicKey(publicKey, EcType.EC_TYPE.SECP256_R1.getValue());
            Signature ecdsaVerify = Signature.getInstance("NoneWithECDSA");
            ecdsaVerify.initVerify(pubKey);
            ecdsaVerify.update(digest);
            boolean verify = ecdsaVerify.verify(unCompressedSignature);
            return verify;
        } catch (GeneralSecurityException | IOException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SIGNABLE_VERIFY_SIGNATURE_FAILED);
        }
    }

    public void checkKeyPairMatch(byte[] privateKey, byte[] publicKey) throws WalletCoreException{
        BigInteger bNum = getOrCreatePrivKeyBigInteger(privateKey);
        byte[] pubKey = getPubKey(bNum);
        boolean result = Arrays.equals(publicKey, pubKey);
        if(!result){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SIGNABLE_NOT_DERIVED_KEY_FROM_PRIVATE_KEY);
        }
    }

    private byte[] compressedSignValue(byte[] derSignature, byte[] compressedPubKey, byte[] hashedsource) throws IOException, WalletCoreException {

        ASN1Sequence asn1Sequence = parseASN1Sequence(derSignature);
        BigInteger[] rs = extractRS(asn1Sequence);
        BigInteger integerR = rs[0];
        BigInteger integerS = adjustS(rs[1], EcType.EC_TYPE.SECP256_R1.getValue());

        byte[] r = padTo32Bytes(integerR);
        byte[] s = padTo32Bytes(integerS);

        byte recoveryId = getRecoveryId(r, s, hashedsource, compressedPubKey);

        byte[] combined = new byte[65];
        ByteBuffer buff = ByteBuffer.wrap(combined);
        buff.put((byte) (recoveryId + 27 + 4));
        buff.put(r);
        buff.put(s);
        return combined;
    }

    private static byte[] compressedToDer(byte[] compactSignature) throws IOException {

        byte[] rBytes = new byte[32];
        System.arraycopy(compactSignature, 1, rBytes, 0, 32);
        BigInteger r = new BigInteger(1, rBytes);

        byte[] sBytes = new byte[32];
        System.arraycopy(compactSignature, 1 + 32, sBytes, 0, 32);
        BigInteger s = new BigInteger(1, sBytes);

        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(r));
        v.add(new ASN1Integer(s));

        DERSequence sequence = new DERSequence(v);
        return sequence.getEncoded();
    }

    private static BigInteger getOrCreatePrivKeyBigInteger(byte[] value) {
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

    private static byte[] getPubKey(BigInteger bnum){
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
    private static ECPoint compressPoint(ECPoint point) {
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

    private byte getRecoveryId(byte[] sigR, byte[] sigS, byte[] hassedMessage, byte[] publicKey) throws WalletCoreException {
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(EcType.EC_TYPE.SECP256_R1.getValue());
        BigInteger pointN = spec.getN();
        int recoveryId = 0;
        for (recoveryId = 0; recoveryId < 2; recoveryId++) {
            BigInteger pointX = new BigInteger(1, sigR);
            ECPoint pointR = decodePoint(spec, pointX, recoveryId);

            if (!pointR.multiply(pointN).isInfinity()) {
                continue;
            }

            ECPoint pointQ = recoverPublicKey(spec, pointR, new BigInteger(1, sigS), new BigInteger(1, hassedMessage));

            if (Arrays.equals(publicKey, pointQ.getEncoded(true))) {
                return (byte) recoveryId;
            }
        }
        return (byte) 0;
    }
    private static ECPoint decodePoint(ECNamedCurveParameterSpec spec, BigInteger pointX, int recoveryId) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(pointX, 1 + x9.getByteLength(spec.getCurve()));
        compEnc[0] = (byte) ((recoveryId & 1) == 1 ? 0x03 : 0x02);
        return spec.getCurve().decodePoint(compEnc);
    }

    private ECPoint recoverPublicKey(ECNamedCurveParameterSpec spec, ECPoint pointR, BigInteger sigS, BigInteger message) {
        BigInteger pointN = spec.getN();
        BigInteger pointEInv = message.negate().mod(pointN);
        BigInteger pointRInv = pointR.getXCoord().toBigInteger().modInverse(pointN);
        BigInteger srInv = pointRInv.multiply(sigS).mod(pointN);
        BigInteger pointEInvRInv = pointRInv.multiply(pointEInv).mod(pointN);
        return ECAlgorithms.sumOfTwoMultiplies(spec.getG(), pointEInvRInv, pointR, srInv);
    }

    private ASN1Sequence parseASN1Sequence(byte[] signData) throws IOException {
//        ByteArrayInputStream inStream = new ByteArrayInputStream(signData);
//        ASN1InputStream asnInputStream = new ASN1InputStream(inStream);
//        ASN1Primitive asn1 = asnInputStream.readObject();
//        return (ASN1Sequence) asn1;
        try (
                ByteArrayInputStream inStream = new ByteArrayInputStream(signData);
                ASN1InputStream asnInputStream = new ASN1InputStream(inStream)
        ) {
            ASN1Primitive asn1 = asnInputStream.readObject();
            return (ASN1Sequence) asn1;
        }
    }

    private BigInteger[] extractRS(ASN1Sequence asn1Sequence) throws WalletCoreException {
        ASN1Encodable[] asn1Encodables = asn1Sequence.toArray();
        if (asn1Encodables.length != 2) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SIGNABLE_CREATE_SIGNATURE);
        }
        BigInteger integerR = ((ASN1Integer) asn1Encodables[0].toASN1Primitive()).getValue();
        BigInteger integerS = ((ASN1Integer) asn1Encodables[1].toASN1Primitive()).getValue();
        return new BigInteger[]{integerR, integerS};
    }

    private BigInteger adjustS(BigInteger s, String curveName) {
        BigInteger curveN = CustomNamedCurves.getByName(curveName).getN();
        BigInteger halfCurveOrder = curveN.shiftRight(1);
        if (s.compareTo(halfCurveOrder) > 0) {
            s = curveN.subtract(s);
        }
        return s;
    }

    private byte[] padTo32Bytes(BigInteger value) {
        byte[] result = new byte[32];
        byte[] byteArray = value.toByteArray();
        if (byteArray.length > 32) {
            System.arraycopy(byteArray, byteArray.length - 32, result, 0, 32);
        } else if (byteArray.length < 32) {
            System.arraycopy(byteArray, 0, result, 32 - byteArray.length, byteArray.length);
        } else {
            result = byteArray;
        }
        return result;
    }

//        public static byte[] compressPublicKey(byte[] unCompressedPublicKeyBytes) throws WalletCoreException{
//
//            //KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
//            //keyPairGenerator.initialize(keyGenParameterSpec);
//            //KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            KeyFactory keyFactory = KeyFactory.getInstance("EC");
//            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(unCompressedPublicKeyBytes);
//            ECPublicKey  ecPublicKey = (ECPublicKey) keyFactory.generatePublic(publicKeySpec);
//            //ECPublicKey ecPublicKey = (ECPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyPair.getPublic().getEncoded()));
//            byte[] ecPubkeyX = new byte[33];
//            byte[] ecPubkeyY = new byte[32];
//            byte[] pubKey = new byte[65];
//            pubKey = ecPublicKey.getEncoded();
//            System.arraycopy(ecPublicKey.getEncoded(), 27 + 32, ecPubkeyY, 0, 32);
//            if ((ecPubkeyY[ecPubkeyY.length - 1] & 0x01) != 0) {
//                ecPubkeyX[0] = (byte) 0x03;
//            } else {
//                ecPubkeyX[0] = (byte) 0x02;
//            }
//            System.arraycopy(ecPublicKey.getEncoded(), 27, ecPubkeyX, 1, 32);
//            Arrays.fill(ecPubkeyY, (byte) 0x00);
//            Arrays.fill(pubKey, (byte) 0x00);
//            return ecPubkeyX;
//    }
}









