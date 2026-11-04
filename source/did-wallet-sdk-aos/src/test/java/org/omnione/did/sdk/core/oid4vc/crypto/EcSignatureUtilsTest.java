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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

public class EcSignatureUtilsTest {
    private static final String SC_PROVIDER = "SC";
    private static final int COMPONENT_LENGTH = 32;

    @BeforeClass
    public static void registerProvider() {
        if (Security.getProvider(SC_PROVIDER) == null) {
            Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
        }
    }

    @Test
    public void roundTripsRawWithHighBitSetInBothComponents() {
        byte[] raw = new byte[COMPONENT_LENGTH * 2];
        Arrays.fill(raw, (byte) 0x7F);
        raw[0] = (byte) 0xFF;
        raw[COMPONENT_LENGTH] = (byte) 0xFF;
        assertArrayEquals(raw, EcSignatureUtils.derToRaw(EcSignatureUtils.rawToDer(raw), COMPONENT_LENGTH));
    }

    @Test
    public void roundTripsRawWithLeadingZeroComponents() {
        byte[] raw = new byte[COMPONENT_LENGTH * 2];
        raw[COMPONENT_LENGTH - 1] = 0x01;
        raw[COMPONENT_LENGTH * 2 - 1] = 0x02;
        assertArrayEquals(raw, EcSignatureUtils.derToRaw(EcSignatureUtils.rawToDer(raw), COMPONENT_LENGTH));
    }

    @Test
    public void rejectsSignatureOfWrongLength() {
        try {
            EcSignatureUtils.rawToDer(new byte[63]);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void derRoundTripKeepsSignatureVerifiable() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", SC_PROVIDER);
        generator.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
        KeyPair keyPair = generator.generateKeyPair();

        byte[] message = "issuer-signature-round-trip".getBytes("UTF-8");

        for (int i = 0; i < 20; i++) {
            Signature signer = Signature.getInstance("SHA256withECDSA", SC_PROVIDER);
            signer.initSign(keyPair.getPrivate());
            signer.update(message);
            byte[] der = signer.sign();

            byte[] raw = EcSignatureUtils.derToRaw(der, COMPONENT_LENGTH);
            byte[] reDer = EcSignatureUtils.rawToDer(raw);

            Signature verifier = Signature.getInstance("SHA256withECDSA", SC_PROVIDER);
            verifier.initVerify(keyPair.getPublic());
            verifier.update(message);
            assertTrue("round-tripped signature must still verify (iteration " + i + ")",
                    verifier.verify(reDer));
        }
    }
}
