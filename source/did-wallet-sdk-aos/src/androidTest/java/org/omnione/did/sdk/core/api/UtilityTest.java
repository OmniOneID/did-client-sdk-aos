/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.core.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnione.did.sdk.utility.CryptoUtils;
import org.omnione.did.sdk.utility.DataModels.CipherInfo;
import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.DataModels.EcKeyPair;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.DigestUtils;
import org.omnione.did.sdk.utility.MultibaseUtils;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UtilityTest {
    @Test
    public void cryptoUtils() throws Exception{
//        assertArrayEquals(a,b) : 배열 a와b가 일치함을 확인
//        assertEquals(a,b) : 객체 a와b의 값이 같은지 확인
//        assertSame(a,b) : 객체 a와b가 같은 객체임을 확인
//        assertTrue(a) : a가 참인지 확인
//        assertNotNull(a) : a객체가 null이 아님을 확인
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //assertEquals("org.omnione.did.sdk.utility", appContext.getPackageName());

        byte[] nonce = CryptoUtils.generateNonce(20);
        assertNotNull(nonce);
        Log.d("utilityTest","generateNonce - nonce : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, nonce)
                + " / length : " + nonce.length);
        EcKeyPair ecKeyPair = CryptoUtils.generateECKeyPair(EcType.EC_TYPE.SECP256_R1);
        assertNotNull(ecKeyPair);

        byte[] clientPrivateKey = MultibaseUtils.decode(ecKeyPair.getPrivateKey());
        byte[] clientPublicKey = MultibaseUtils.decode(ecKeyPair.getPublicKey());

        Log.d("utilityTest","generateECKeyPair - PrivateKey : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, clientPrivateKey)
            + " / length : " + clientPrivateKey.length);
        Log.d("utilityTest","generateECKeyPair - PublicKey : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, clientPublicKey)
            + " / length : " + clientPublicKey.length);

        byte[] yourPrivateKey = MultibaseUtils.decode("f262a051ac304ae86007e5f954860098b3cad236b2a715ced572344fb1d1b6739");
        byte[] yourPubKey = MultibaseUtils.decode("f02a562f660ede54c3ac05b1ea0baa61cf1b4c5b0a928a7c405586e735b83fab974");

        byte[] secretKey = CryptoUtils.generateSharedSecret(EcType.EC_TYPE.SECP256_R1, clientPrivateKey, yourPubKey);
        assertNotNull(secretKey);
        Log.d("utilityTest","generateSharedSecret - secretKey : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, secretKey)
             + " / length : " + secretKey.length);

        byte[] salt = MultibaseUtils.decode("mFVFPNa8pzyuccULBUeTmyorygTU=");
        byte[] pbkdf1 = CryptoUtils.pbkdf2("123456".getBytes(), salt, 10, 32);
        byte[] pbkdf2 = CryptoUtils.pbkdf2("123456".getBytes(), salt, 10, 32);
        byte[] pbkdf3 = CryptoUtils.pbkdf2("123456".getBytes(), salt, 11, 32);
        assertArrayEquals(pbkdf1, pbkdf2);
        assertNotEquals(pbkdf2, pbkdf3);

        Log.d("utilityTest","pbkdf2 - pbkdf : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, pbkdf1)
                + " / length : " + pbkdf1.length);

        byte[] plainData = "Plain".getBytes();
        CipherInfo cipherInfo = new CipherInfo(CipherInfo.ENCRYPTION_TYPE.AES, CipherInfo.ENCRYPTION_MODE.CBC, CipherInfo.SYMMETRIC_KEY_SIZE.AES_256, CipherInfo.SYMMETRIC_PADDING_TYPE.PKCS5);
        byte[] iv = CryptoUtils.generateNonce(16);

        byte[] encData = CryptoUtils.encrypt(
                plainData,
                cipherInfo,
                pbkdf1,
                iv);
        Log.d("utilityTest","encrypt - plain : " + new String(plainData));
        Log.d("utilityTest","encrypt - cipherInfo : " + cipherInfo.toJson());
        Log.d("utilityTest","encrypt - key : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, pbkdf1)
                + " / length : " + pbkdf1.length);
        Log.d("utilityTest","encrypt - iv : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, iv)
                + " / length : " + iv.length);
        Log.d("utilityTest","encrypt - encData : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, encData)
                + " / length : " + encData.length);

        byte[] decData = CryptoUtils.decrypt(
                encData,
                cipherInfo,
                pbkdf1,
                iv);
        Log.d("utilityTest","decrypt - plain : " + new String(plainData));
        Log.d("utilityTest","decrypt - cipherInfo : " + cipherInfo.toJson());
        Log.d("utilityTest","decrypt - key : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, pbkdf1)
                + " / length : " + pbkdf1.length);
        Log.d("utilityTest","decrypt - iv : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, iv)
                + " / length : " + iv.length);
        Log.d("utilityTest","decrypt - decData : " + new String(decData));
        assertArrayEquals(plainData, decData);
    }
    @Test
    public void multibaseUtils() throws Exception{
        byte[] random = CryptoUtils.generateNonce(20);
        String encodeBase16 = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_16, random);
        Log.d("utilityTest","encode - encodeBase16 : " + encodeBase16
                + " / length : " + encodeBase16.length());
        byte[] decodeBase16 = MultibaseUtils.decode(encodeBase16);
        assertArrayEquals(random, decodeBase16);

        String encodeBase16Upper = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_16_UPPER, decodeBase16);
        Log.d("utilityTest","encode - encodeBase16Upper : " + encodeBase16Upper
                + " / length : " + encodeBase16Upper.length());
        byte[] decodeBase16Upper = MultibaseUtils.decode(encodeBase16Upper);
        assertArrayEquals(decodeBase16, decodeBase16Upper);

        String encodeBase58 = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_58_BTC, decodeBase16Upper);
        Log.d("utilityTest","encode - encodeBase58 : " + encodeBase58
                + " / length : " + encodeBase58.length());
        byte[] decodeBase58 = MultibaseUtils.decode(encodeBase58);
        assertArrayEquals(decodeBase16Upper, decodeBase58);

        String encodeBase64 = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, decodeBase58);
        Log.d("utilityTest","encode - encodeBase64 : " + encodeBase64
                + " / length : " + encodeBase64.length());
        byte[] decodeBase64 = MultibaseUtils.decode(encodeBase64);
        assertArrayEquals(decodeBase58, decodeBase64);

        String encodeBase64Url = MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64_URL, decodeBase64);
        Log.d("utilityTest","encode - encodeBase64Url : " + encodeBase64Url
                + " / length : " + encodeBase64Url.length());
        byte[] decodeBase64Url = MultibaseUtils.decode(encodeBase64Url);
        assertArrayEquals(decodeBase64, decodeBase64Url);

    }
    @Test
    public void digestUtils() throws Exception {
        byte[] random = CryptoUtils.generateNonce(50);
        Log.d("utilityTest","getDigest - random : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, random)
                + " / length : " + random.length);
        byte[] sha256 = DigestUtils.getDigest(random, DigestEnum.DIGEST_ENUM.SHA_256);
        Log.d("utilityTest","getDigest - sha256 : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, sha256)
                + " / length : " + sha256.length);
        byte[] sha384 = DigestUtils.getDigest(random, DigestEnum.DIGEST_ENUM.SHA_384);
        Log.d("utilityTest","getDigest - sha384 : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, sha384)
                + " / length : " + sha384.length);
        byte[] sha512 = DigestUtils.getDigest(random, DigestEnum.DIGEST_ENUM.SHA_512);
        Log.d("utilityTest","getDigest - sha512 : " + MultibaseUtils.encode(MultibaseType.MULTIBASE_TYPE.BASE_64, sha512)
                + " / length : " + sha512.length);

    }
}