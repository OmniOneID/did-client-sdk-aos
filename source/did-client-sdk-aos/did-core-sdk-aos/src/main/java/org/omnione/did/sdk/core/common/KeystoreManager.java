/* 
 * Copyright 2024 Raonsecure
 */

package org.omnione.did.sdk.core.common;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.biometric.BiometricManager;

import org.omnione.did.sdk.datamodel.common.enums.AlgorithmType;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DataModels.ec.EcUtils;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyGenerationInfo;
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
import org.spongycastle.math.ec.ECPoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class KeystoreManager {
    private static int KEYSTORE_KEY_VALID_SECONDS = 5 * 600;
    private static String SECURE_ENCRPTOR_ALIAS_PREFIX = "opendid_wallet_encryption_";
    private static String SIGNATURE_MANAGER_ALIAS_PREFIX = "opendid_wallet_signature_";

    static {
        Security.removeProvider("SC");
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }

    public static KeyGenerationInfo generateKey(Context context, String prefix, String alias) throws WalletCoreException {
        try {
            KeyGenerationInfo keyGenerationInfo = new KeyGenerationInfo();
            keyGenerationInfo.setAlgoType(AlgorithmType.ALGORITHM_TYPE.SECP256R1);

            if (context == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_INVALID_PARAMETER, "context");
            }
            if (alias.isEmpty()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_INVALID_PARAMETER, "alias");
            }
            if (isKeySaved(prefix, alias)) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_DUPLICATED_PARAMETER, "alias");
            }
            //if (!isBiometric(context)) {
            //    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_NO_REGISTERED_BIO_AUTH_INFO);
            //}
            byte[] ecPubKeyX = generateECKeyWithKeyStore(prefix + alias, context);
            keyGenerationInfo.setPublicKey(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, ecPubKeyX));

            return keyGenerationInfo;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeySpecException | NoSuchProviderException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_CREATE_SECURE_KEY);
        }
    }

    public static boolean isKeySaved(String prefix, String alias) throws WalletCoreException {
        try {
            if (prefix.isEmpty()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_INVALID_PARAMETER, "prefix");
            }
            String key = prefix;
            if (alias != null) {
                key = prefix + alias;
            }
            boolean ret = false;

            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                if (aliases.nextElement().contains(key)) {
                    ret = true;
                    break;
                }
            }
            return ret;
        } catch(KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_FIND_SECURE_KEY_BY_CONDITION);
        }
    }

    public static byte[] getPublicKey(String prefix, String alias) throws WalletCoreException, KeyStoreException {
        try {
            byte[] pubKey = null;
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(prefix + alias, null);
            if (entry != null)
                pubKey = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey().getEncoded();
            byte[] ecPubkeyX = new byte[33];
            byte[] ecPubkeyY = new byte[32];
            if (pubKey == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_PUBLIC_KEY_REPRESENTATION);
            }
            System.arraycopy(pubKey, 27 + 32, ecPubkeyY, 0, 32);
            if ((ecPubkeyY[ecPubkeyY.length - 1] & 0x01) != 0) {
                ecPubkeyX[0] = (byte) 0x03;
            } else {
                ecPubkeyX[0] = (byte) 0x02;
            }
            System.arraycopy(pubKey, 27, ecPubkeyX, 1, 32);
            Arrays.fill(ecPubkeyY, (byte) 0x00);
            Arrays.fill(pubKey, (byte) 0x00);
            return ecPubkeyX;
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_PUBLIC_KEY_REPRESENTATION);
        }
    }
    public static void deleteKey(String prefix, String alias) throws WalletCoreException {
        try {
            List<String> aliasList = getKeystoreAliasList();
            if (prefix.isEmpty()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_INVALID_PARAMETER, "prefix");
            }
            String key = prefix;
            if (alias != null) {
                key = prefix + alias;
            }

            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            if (ks == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_DELETE_SECURE_KEY);
            }
            ks.load(null);
            if (aliasList != null && aliasList.size() != 0) {
                for (int i = 0; i < aliasList.size(); i++) {
                    if (aliasList.get(i).contains(key)) {
                        ks.deleteEntry(aliasList.get(i));
                    }
                }
            }
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_DELETE_SECURE_KEY);
        }
    }

    private static byte[] generateECKeyWithKeyStore(String alias, Context context) throws WalletCoreException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        KeyGenParameterSpec keyGenParameterSpec;
        if (isSupportStrongBox(context)) {
            keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                    .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                    .setDigests(KeyProperties.DIGEST_NONE)
                    // 생체 인증을 통해서만 keystore 접근 가능하게 옵션을 준다.
                    //.setUserAuthenticationRequired(true)
                    //.setUserAuthenticationValidityDurationSeconds(KEYSTORE_KEY_VALID_SECONDS)
                    .setIsStrongBoxBacked(true)
                    .build();
        } else {
            keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                    .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                    .setDigests(KeyProperties.DIGEST_NONE)
                    // keyOption 이 R1_KEYSTORE_ACCESS 인 경우 생체 인증을 통해서만 keystore 접근 가능하게 옵션을 준다.
                    //.setUserAuthenticationRequired((true))
                    //.setUserAuthenticationValidityDurationSeconds(KEYSTORE_KEY_VALID_SECONDS)
                    .build();
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
        keyPairGenerator.initialize(keyGenParameterSpec);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        if(keyPair == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_CREATE_SECURE_KEY);
        }
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        ECPublicKey ecPublicKey = (ECPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyPair.getPublic().getEncoded()));
        return copyPublicKey(ecPublicKey);

    }
    private static byte[] copyPublicKey(ECPublicKey ecPublicKey) throws WalletCoreException{

        byte[] ecPubkeyX = new byte[33];
        byte[] ecPubkeyY = new byte[32];
        byte[] pubKey = new byte[65];
        pubKey = ecPublicKey.getEncoded();
        if(pubKey == null) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_COPY_PUBLIC_KEY);
        }
        System.arraycopy(pubKey, 27 + 32, ecPubkeyY, 0, 32);
        if ((ecPubkeyY[ecPubkeyY.length - 1] & 0x01) != 0) {
            ecPubkeyX[0] = (byte) 0x03;
        } else {
            ecPubkeyX[0] = (byte) 0x02;
        }
        System.arraycopy(ecPublicKey.getEncoded(), 27, ecPubkeyX, 1, 32);
        Arrays.fill(ecPubkeyY, (byte) 0x00);
        Arrays.fill(pubKey, (byte) 0x00);
        return ecPubkeyX;
    }
    private static boolean isSupportStrongBox(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_STRONGBOX_KEYSTORE);
    }

    private static boolean isExistAliasInKeyStore(String strAlias) throws WalletCoreException, KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {

        boolean ret = false;
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        Enumeration<String> aliases = ks.aliases();
        if(aliases == null){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_FIND_SECURE_KEY_BY_CONDITION);
        }
        while (aliases.hasMoreElements()) {
            if (aliases.nextElement().equals(strAlias)) {
                ret = true;
                break;
            }
        }
        return ret;
    }
    private static boolean isBiometric(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);

        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                return false;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                return false;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                return false;
            default:
                return false;
        }
    }

    public static byte[] sign(String alias, byte[] digest) throws WalletCoreException {
        try {
            if (digest == null || digest.length == 0) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_INVALID_PARAMETER, "digest");
            }

            if (alias.isEmpty()) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_INVALID_PARAMETER, "alias");
            }
            byte[] signedMsgFromKeyStore = new byte[0];
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!isKeySaved(SIGNATURE_MANAGER_ALIAS_PREFIX, alias)) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_FIND_SECURE_KEY_BY_CONDITION);
            }

            java.security.PrivateKey privateKey = (java.security.PrivateKey) keyStore.getKey(SIGNATURE_MANAGER_ALIAS_PREFIX + alias, null);

            Signature ecdsaSign = Signature.getInstance("NoneWithECDSA");
            ecdsaSign.initSign(privateKey);
            ecdsaSign.update(digest);

            signedMsgFromKeyStore = ecdsaSign.sign();
            byte[] pubKey = getPublicKey(SIGNATURE_MANAGER_ALIAS_PREFIX, alias);
            return compressedSignValue(signedMsgFromKeyStore, pubKey, digest);
        } catch(KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | UnrecoverableEntryException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_FAILED_TO_SIGN);
        }

    }

    public static boolean verify(byte[] publicKey, byte[] digest, byte[] signature) throws WalletCoreException {
        try {
            if (signature.length != 65) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_INVALID_PARAMETER);
            }
            byte[] unCompressedSignature = compressedToDer(signature);
            PublicKey pubKey = EcUtils.getPublicKey(publicKey, EcType.EC_TYPE.SECP256_R1.getValue());
            Signature ecdsaVerify = Signature.getInstance("NoneWithECDSA");
            ecdsaVerify.initVerify(pubKey);
            ecdsaVerify.update(digest);
            boolean verify = ecdsaVerify.verify(unCompressedSignature);
            return verify;
        } catch (GeneralSecurityException | IOException e){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_SIGNABLE_VERIFY_SIGNATURE_FAILED);
        }
    }

    public static byte[] encrypt(String alias, byte[] plainData, Context context) throws WalletCoreException {
        try {
            SecretKey secretKey;
            if (isExistAliasInKeyStore(alias)) {
                KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null);

                if (keyEntry != null) {
                    secretKey = keyEntry.getSecretKey();
                } else {
                    throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_FIND_SECURE_KEY_BY_CONDITION);
                }
            } else {
                KeyGenParameterSpec keyGenParameterSpec;
                if (isSupportStrongBox(context)) {
                    keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false)
                            .setIsStrongBoxBacked(true)
                            .build();

                } else {
                    keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false)
                            .build();
                }

                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                keyGenerator.init(keyGenParameterSpec);
                secretKey = keyGenerator.generateKey();
            }

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            GCMParameterSpec params = cipher.getParameters().getParameterSpec(GCMParameterSpec.class);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream dataStream = new DataOutputStream(byteStream);

            dataStream.writeInt(params.getTLen());
            byte[] iv = params.getIV();
            dataStream.writeInt(iv.length);
            dataStream.write(iv);

            int cipherTextSize = 0;
            ByteArrayInputStream plaintextStream = new ByteArrayInputStream(plainData);
            final int chunkSize = 4 * 1024;
            byte[] buffer = new byte[chunkSize];
            int i = 0;
            while (plaintextStream.available() > chunkSize) {
                int readBytes = plaintextStream.read(buffer);
                byte[] ciphertextChunk = cipher.update(buffer, 0, readBytes);
                cipherTextSize += ciphertextChunk.length;
                dataStream.write(ciphertextChunk);
            }
            int readBytes = plaintextStream.read(buffer);
            byte[] ciphertextChunk = cipher.doFinal(buffer, 0, readBytes);
            if (ciphertextChunk == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_CREATE_ENCRYPTED_DATA);
            }
            cipherTextSize += ciphertextChunk.length;
            dataStream.write(ciphertextChunk);
            return byteStream.toByteArray();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | InvalidParameterSpecException | BadPaddingException | NoSuchProviderException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_CREATE_ENCRYPTED_DATA);
        }
    }

    public static byte[] decrypt(String alias, byte[] cipherData) throws WalletCoreException {
        try {
            if (!isExistAliasInKeyStore(alias)) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_FIND_SECURE_KEY_BY_CONDITION);
            }

            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null);

            if (keyEntry == null) {
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_FIND_SECURE_KEY_BY_CONDITION);
            }
            ByteArrayInputStream byteStream = new ByteArrayInputStream(cipherData);
            DataInputStream dataStream = new DataInputStream(byteStream);
            int tlen = dataStream.readInt();
            byte[] iv = new byte[dataStream.readInt()];
            dataStream.read(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            if (cipher == null)
                throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_CREATE_DECRYPTED_DATA);
            cipher.init(Cipher.DECRYPT_MODE, keyEntry.getSecretKey(), new GCMParameterSpec(tlen, iv));
            CipherInputStream cipherStream = new CipherInputStream(byteStream, cipher);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byteCopy(cipherStream, outputStream);

            return outputStream.toByteArray();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_CREATE_DECRYPTED_DATA);
        }
    }
    private static void byteCopy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length = 0;
        while((length = source.read(buf)) > 0){
            target.write(buf, 0, length);
        }
    }

    public static List<String> getKeystoreAliasList() throws WalletCoreException{
        try {
            List<String> aliasList = new ArrayList<String>();
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();
            aliasList = Collections.list(aliases);
            return aliasList;
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e ){
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_KEYSTORE_MANAGER_CANNOT_FIND_SECURE_KEY_BY_CONDITION);
        }
    }

    private static byte[] compressedSignValue(byte[] derSignature, byte[] compressedPubKey, byte[] hashedsource) throws IOException {

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

    private static byte getRecoveryId(byte[] sigR, byte[] sigS, byte[] hassedMessage, byte[] publicKey) {
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

    private static ASN1Sequence parseASN1Sequence(byte[] signData) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(signData);
        ASN1InputStream asnInputStream = new ASN1InputStream(inStream);
        ASN1Primitive asn1 = asnInputStream.readObject();
        return (ASN1Sequence) asn1;
    }

    private static BigInteger[] extractRS(ASN1Sequence asn1Sequence) {
        ASN1Encodable[] asn1Encodables = asn1Sequence.toArray();
        BigInteger integerR = ((ASN1Integer) asn1Encodables[0].toASN1Primitive()).getValue();
        BigInteger integerS = ((ASN1Integer) asn1Encodables[1].toASN1Primitive()).getValue();
        return new BigInteger[]{integerR, integerS};
    }

    private static BigInteger adjustS(BigInteger s, String curveName) {
        BigInteger curveN = CustomNamedCurves.getByName(curveName).getN();
        BigInteger halfCurveOrder = curveN.shiftRight(1);
        if (s.compareTo(halfCurveOrder) > 0) {
            s = curveN.subtract(s);
        }
        return s;
    }

    private static byte[] padTo32Bytes(BigInteger value) {
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

    private static ECPoint decodePoint(ECNamedCurveParameterSpec spec, BigInteger pointX, int recoveryId) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(pointX, 1 + x9.getByteLength(spec.getCurve()));
        compEnc[0] = (byte) ((recoveryId & 1) == 1 ? 0x03 : 0x02);
        return spec.getCurve().decodePoint(compEnc);
    }

    private static ECPoint recoverPublicKey(ECNamedCurveParameterSpec spec, ECPoint pointR, BigInteger sigS, BigInteger message) {
        BigInteger pointN = spec.getN();
        BigInteger pointEInv = message.negate().mod(pointN);
        BigInteger pointRInv = pointR.getXCoord().toBigInteger().modInverse(pointN);
        BigInteger srInv = pointRInv.multiply(sigS).mod(pointN);
        BigInteger pointEInvRInv = pointRInv.multiply(pointEInv).mod(pointN);
        return ECAlgorithms.sumOfTwoMultiplies(spec.getG(), pointEInvRInv, pointR, srInv);
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
}
