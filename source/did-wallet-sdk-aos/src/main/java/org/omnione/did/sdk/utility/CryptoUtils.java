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

package org.omnione.did.sdk.utility;

import org.omnione.did.sdk.utility.DataModels.CipherInfo;
import org.omnione.did.sdk.utility.DataModels.EcKeyPair;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DataModels.ec.EcUtils;
import org.omnione.did.sdk.utility.Errors.UtilityErrorCode;
import org.omnione.did.sdk.utility.Errors.UtilityException;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    static {
        Security.removeProvider("SC");
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Generates a random nonce of the specified size.
     *
     * This method uses a cryptographically secure random number generator (SecureRandom)
     * to generate a random byte array (nonce) of the given size. The nonce can be used
     * in cryptographic operations to ensure uniqueness and randomness.
     *
     * @param size The size of the nonce to generate.
     * @return A byte array containing the generated nonce.
     * @throws Exception If the size is zero or if there is an error generating the nonce.
     */
    public static byte[] generateNonce(int size) throws UtilityException {
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_CREATE_RANDOM_KEY, e.getMessage());
        }
        if(size == 0){
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_INVALID_PARAMETER, "size");
        }
        byte[] nonce = new byte[size];
        random.nextBytes(nonce);

        return nonce;
    }

    /**
     * Generates an EC (Elliptic Curve) key pair for ECDH (Elliptic Curve Diffie-Hellman) purposes.
     *
     * This method generates a key pair using the specified elliptic curve type. The supported
     * types are SECP256_K1 and SECP256_R1. If an unsupported type is provided, an exception is thrown.
     *
     * @param ecType The elliptic curve type to use for key generation. Must be SECP256_R1.
     * @return An EcKeyPair object containing the generated private and public keys.
     * @throws Exception If an unsupported elliptic curve type is provided or if there is an error during key generation.
     */
    public static EcKeyPair generateECKeyPair(EcType.EC_TYPE ecType) throws UtilityException {
        if (!EcType.EC_TYPE.SECP256_K1.getValue().equals(ecType.getValue()) && !EcType.EC_TYPE.SECP256_R1.getValue().equals(ecType.getValue())) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_UNSUPPORTED_EC_TYPE);
        }
        EcKeyPair ecKeyPair = new EcKeyPair();
        BigInteger bNum = EcUtils.getOrCreatePrivKeyBigInteger(null);
        ecKeyPair.setPrivateKey(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, bNum.toByteArray()));
        ecKeyPair.setPublicKey(MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, EcUtils.getPubKey(bNum)));

        return ecKeyPair;
    }

    /**
     * Generates a shared secret key using ECDH (Elliptic Curve Diffie-Hellman).
     *
     * This method generates a shared secret key based on the provided elliptic curve type,
     * private key, and public key. The shared secret is derived from the multiplication of
     * the private key and the public key.
     *
     * @param ecType The elliptic curve type to use for key generation. Must be SECP256_R1.
     * @param privateKey The private key to use for generating the shared secret.
     * @param publicKey The public key to use for generating the shared secret.
     * @return A byte array containing the generated shared secret.
     * @throws Exception If any of the parameters are invalid or if there is an error during key generation.
     */
    public static byte[] generateSharedSecret(EcType.EC_TYPE ecType, byte[] privateKey, byte[] publicKey) throws UtilityException {

        if(privateKey == null){
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_INVALID_PARAMETER, "privateKey");
        }
        if(publicKey == null){
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_INVALID_PARAMETER, "publicKey");
        }
        if (!EcType.EC_TYPE.SECP256_K1.getValue().equals(ecType.getValue()) && !EcType.EC_TYPE.SECP256_R1.getValue().equals(ecType.getValue())) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_UNSUPPORTED_EC_TYPE);
        }
        try {
            BigInteger privKey = EcUtils.getOrCreatePrivKeyBigInteger(privateKey);
            ECPrivateKey ecPrivateKey = EcUtils.getPrivateKey(privKey.toByteArray(), ecType.getValue());
            ECPublicKey ecPublicKey = EcUtils.getPublicKey(publicKey, ecType.getValue());
            if (ecPublicKey == null)
                throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_CONVERT_PUBLIC_KEY_TO_EXTERNAL_REPRESENTATION);
            CipherParameters ciPubKey = null;

            ciPubKey = ECUtil.generatePublicKeyParameter(ecPublicKey);
            ECPublicKeyParameters pub = (ECPublicKeyParameters) ciPubKey;
            ECPrivateKeyParameters priv = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(ecPrivateKey);
            ECPoint P = pub.getQ().multiply(priv.getD()).normalize();
            if (P.isInfinity()) {
                throw new IllegalStateException("invalid ECDH");
            }
            BigInteger bitInt = P.getAffineXCoord().toBigInteger();
            return EcUtils.getBytes(bitInt);
        } catch (GeneralSecurityException e) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_GENERATE_SHARED_SECRET_USING_ECDH, e.getMessage());
        }

    }

    /**
     *  Derives a key based on a password using the PBKDF2 algorithm.
     *
     * This method derives a key from the given password and salt using the PBKDF2 algorithm.
     * The derived key is typically used for encryption purposes.
     *
     * @param password The password as a byte array. Throws an exception if null.
     * @param salt The salt as a byte array. Throws an exception if null.
     * @param iterations The number of iterations for the PBKDF2 algorithm.
     * @param derivedKeyLength The length of the derived key in bits.
     * @return The derived key as a byte array.
     * @throws Exception If the password or salt is null, or if key derivation fails.
     */
    public static byte[] pbkdf2(byte[] password, byte[] salt, int iterations, int derivedKeyLength) throws UtilityException {
        if (password == null){
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_INVALID_PARAMETER, "password");
        }
        if (salt == null){
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_INVALID_PARAMETER, "salt");
        }
        try {
            char[] seed = new char[password.length];
            for (int i = 0; i < password.length; i++) {
                seed[i] = (char) password[i];
            }
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(
                    seed,
                    salt,
                    iterations,
                    derivedKeyLength * 8
            );

            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

            byte[] dk = skeySpec.getEncoded();
            return dk;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_DERIVE_KEY_USING_PBKDF2);
        }
    }

    /**
     * Encrypts data using the specified cipher information.
     *
     * This method encrypts the given plaintext using the specified cipher type, mode, and padding.
     * It supports CBC and ECB modes of operation for the cipher.
     *
     * @param plain The plaintext to be encrypted as a byte array. Throws an exception if null.
     * @param info The CipherInfo object containing the type, mode, and padding of the cipher.
     * @param key The encryption key as a byte array. Throws an exception if null.
     * @param iv The initialization vector as a byte array. Used only in CBC mode. Throws an exception if null in CBC mode.
     * @return The encrypted data as a byte array.
     * @throws Exception If any of the parameters are null, or if encryption fails.
     */
    public static byte[] encrypt(byte[] plain, CipherInfo info, byte[] key, byte[] iv) throws UtilityException {
        try {
            String cipherInfo = info.getType().getValue() + "/" + info.getMode().getValue() + "/" + info.getPadding().getValue();
            SecretKey sKey = new SecretKeySpec(key, 0, key.length, info.getType().getValue());
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher encCipher = Cipher.getInstance(cipherInfo);
            switch (info.getMode().getValue()) {
                case "CBC":
                    encCipher.init(Cipher.ENCRYPT_MODE, sKey, ivSpec);
                    break;
                case "ECB":
                    encCipher.init(Cipher.ENCRYPT_MODE, sKey);
                    break;
                default:
                    throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_ENCRYPT_USING_AES);
            }
            byte[] encrypted = encCipher.doFinal(plain);
            if (encrypted == null) {
                throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_ENCRYPT_USING_AES);
            }
            return encrypted;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_ENCRYPT_USING_AES);
        }
    }

    /**
     * Decrypts data using the specified cipher information.
     *
     * This method decrypts the given ciphertext using the specified cipher type, mode, and padding.
     * It supports CBC and ECB modes of operation for the cipher.
     *
     * @param cipher The ciphertext to be decrypted as a byte array. Throws an exception if null.
     * @param info The CipherInfo object containing the type, mode, and padding of the cipher.
     * @param key The decryption key as a byte array. Throws an exception if null.
     * @param iv The initialization vector as a byte array. Used only in CBC mode. Throws an exception if null in CBC mode.
     * @return The decrypted data as a byte array.
     * @throws Exception If any of the parameters are null, or if decryption fails.
     */
    public static byte[] decrypt(byte[] cipher, CipherInfo info, byte[] key, byte[] iv) throws UtilityException {
        try {
            String cipherInfo = info.getType().getValue() + "/" + info.getMode().getValue() + "/" + info.getPadding().getValue();
            SecretKey sKey = new SecretKeySpec(key, 0, key.length, info.getType().getValue());
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher decCipher = Cipher.getInstance(cipherInfo);
            switch (info.getMode().getValue()) {
                case "CBC":
                    decCipher.init(Cipher.DECRYPT_MODE, sKey, ivSpec);
                    break;
                case "ECB":
                    decCipher.init(Cipher.DECRYPT_MODE, sKey);
                    break;
                default:
                    throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_DECRYPT_USING_AES);
            }
            byte[] decrypted = decCipher.doFinal(cipher);
            if (decrypted == null) {
                throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_DECRYPT_USING_AES);
            }

            return decrypted;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_CRYPTO_UTILS_FAIL_TO_DECRYPT_USING_AES);
        }
    }
}
