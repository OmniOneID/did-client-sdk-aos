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

package org.omnione.did.sdk.core.keymanager.supportalgorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;

public class Secp256R1ManagerTest {

    private static final BigInteger ORDER =
            new BigInteger("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551", 16);
    private static final BigInteger GX =
            new BigInteger("6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", 16);
    private static final BigInteger GY =
            new BigInteger("4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", 16);

    private static final String HIGH_BIT_KEY = "C0FFEE0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789";
    private static final String HIGH_BIT_PUB = "03FB1888E07699EF02F55A48BCDCC977CA45FBBE07B69A46E69B9C110E965F9C12";
    private static final String PADDED_KEY = "008A1B2C3D4E5F60718293A4B5C6D7E8F90A1B2C3D4E5F60718293A4B5C6D7E8F9";

    @Test
    public void restoredPrivateKeyMatchesInputScalarAndP256Params() throws Exception {
        for (String hex : new String[]{HIGH_BIT_KEY, PADDED_KEY}) {
            byte[] bytes = hex(hex);
            ECPrivateKey key = (ECPrivateKey) restore(bytes);
            assertEquals(new BigInteger(1, bytes), key.getS());
            ECParameterSpec params = key.getParams();
            assertEquals(ORDER, params.getOrder());
            assertEquals(GX, params.getGenerator().getAffineX());
            assertEquals(GY, params.getGenerator().getAffineY());
            assertEquals(1, params.getCofactor());
        }
    }

    @Test
    public void signThenVerifyRoundTrip() throws Exception {
        Secp256R1Manager manager = new Secp256R1Manager();
        byte[] digest = MessageDigest.getInstance("SHA-256").digest("kat".getBytes(StandardCharsets.UTF_8));
        byte[] signature = manager.sign(hex(HIGH_BIT_KEY), digest);
        assertTrue(manager.verify(hex(HIGH_BIT_PUB), digest, signature));
    }

    private static PrivateKey restore(byte[] bytes) throws Exception {
        Method m = Secp256R1Manager.class.getDeclaredMethod(
                "getECPrivateKeyFromBytes", byte[].class, ECGenParameterSpec.class);
        m.setAccessible(true);
        return (PrivateKey) m.invoke(new Secp256R1Manager(), bytes, new ECGenParameterSpec("secp256r1"));
    }

    private static byte[] hex(String s) {
        byte[] out = new byte[s.length() / 2];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return out;
    }
}
