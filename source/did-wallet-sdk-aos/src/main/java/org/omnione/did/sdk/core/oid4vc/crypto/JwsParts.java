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

import com.nimbusds.jose.JOSEObject;
import com.nimbusds.jose.util.Base64URL;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public final class JwsParts {

    private static final int LENGTH_JWS_PARTS = 3;

    private final String header;
    private final String payload;
    private final String signature;
    private final String headerDecoded;
    private final String payloadDecoded;
    private final byte[] signatureBytes;

    private JwsParts(String header, String payload, String signature,
                     String headerDecoded, String payloadDecoded, byte[] signatureBytes) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
        this.headerDecoded = headerDecoded;
        this.payloadDecoded = payloadDecoded;
        this.signatureBytes = signatureBytes;
    }

    public static JwsParts parse(String jws) {
        if (jws == null) {
            throw new IllegalArgumentException("jws is null");
        }
        Base64URL[] parts;
        try {
            parts = JOSEObject.split(jws);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid JWS format", e);
        }
        if (parts.length != LENGTH_JWS_PARTS) {
            throw new IllegalArgumentException("Invalid JWS format");
        }
        return new JwsParts(
                parts[0].toString(),
                parts[1].toString(),
                parts[2].toString(),
                new String(parts[0].decode(), StandardCharsets.UTF_8),
                new String(parts[1].decode(), StandardCharsets.UTF_8),
                parts[2].decode());
    }

    public String header() { return header; }
    public String payload() { return payload; }
    public String signature() { return signature; }
    public String headerDecoded() { return headerDecoded; }
    public String payloadDecoded() { return payloadDecoded; }
    public byte[] signatureBytes() { return signatureBytes.clone(); }
}
