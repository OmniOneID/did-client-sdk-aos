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

import java.math.BigInteger;

public final class EcSignatureUtils {
    private static final int P256_COMPONENT_LENGTH = 32;

    private EcSignatureUtils() {}

    @NonNull
    public static byte[] rawToDer(@NonNull byte[] raw) {
        if (raw.length != P256_COMPONENT_LENGTH * 2) {
            throw new IllegalArgumentException("Invalid ES256 signature length: " + raw.length);
        }
        byte[] r = new byte[P256_COMPONENT_LENGTH];
        byte[] s = new byte[P256_COMPONENT_LENGTH];
        System.arraycopy(raw, 0, r, 0, P256_COMPONENT_LENGTH);
        System.arraycopy(raw, P256_COMPONENT_LENGTH, s, 0, P256_COMPONENT_LENGTH);
        byte[] rBytes = new BigInteger(1, r).toByteArray();
        byte[] sBytes = new BigInteger(1, s).toByteArray();
        int length = 2 + rBytes.length + 2 + sBytes.length;
        byte[] der = new byte[2 + length];
        der[0] = 0x30;
        der[1] = (byte) length;
        int o = 2;
        der[o++] = 0x02;
        der[o++] = (byte) rBytes.length;
        System.arraycopy(rBytes, 0, der, o, rBytes.length);
        o += rBytes.length;
        der[o++] = 0x02;
        der[o++] = (byte) sBytes.length;
        System.arraycopy(sBytes, 0, der, o, sBytes.length);
        return der;
    }

    @NonNull
    public static byte[] derToRaw(@NonNull byte[] der, int componentLength) {
        if (der.length < 8 || der[0] != 0x30) {
            throw new IllegalArgumentException("Invalid DER signature");
        }
        int offset = 2;
        if ((der[1] & 0xFF) > 0x80) {
            offset += (der[1] & 0x7F);
        }
        if (der[offset] != 0x02) {
            throw new IllegalArgumentException("Invalid DER signature: expected INTEGER for R");
        }
        int rLength = der[offset + 1] & 0xFF;
        int rStart = offset + 2;
        int sTagOffset = rStart + rLength;
        if (der[sTagOffset] != 0x02) {
            throw new IllegalArgumentException("Invalid DER signature: expected INTEGER for S");
        }
        int sLength = der[sTagOffset + 1] & 0xFF;
        int sStart = sTagOffset + 2;

        byte[] raw = new byte[componentLength * 2];
        copyRightAligned(der, rStart, rLength, raw, 0, componentLength);
        copyRightAligned(der, sStart, sLength, raw, componentLength, componentLength);
        return raw;
    }

    private static void copyRightAligned(@NonNull byte[] src, int srcOffset, int srcLength,
                                         @NonNull byte[] dest, int destOffset, int destLength) {
        int start = srcOffset;
        int length = srcLength;
        while (length > destLength && src[start] == 0x00) {
            start++;
            length--;
        }
        if (length > destLength) {
            throw new IllegalArgumentException("DER component longer than " + destLength + " bytes");
        }
        System.arraycopy(src, start, dest, destOffset + (destLength - length), length);
    }
}
