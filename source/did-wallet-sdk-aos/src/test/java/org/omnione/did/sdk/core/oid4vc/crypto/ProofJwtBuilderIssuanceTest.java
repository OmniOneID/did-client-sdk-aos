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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ProofJwtBuilderIssuanceTest {
    private static final String DID = "did:omn:holder";
    private static final String VERSION = "3";
    private static final String AUDIENCE = "https://issuer.example.com";
    private static final String NONCE = "c-nonce-1";
    private static final long IAT = 1791937200L;

    private static JsonObject part(String signingInput, int index) {
        String json = new String(Base64.getUrlDecoder().decode(signingInput.split("\\.")[index]), StandardCharsets.UTF_8);
        return JsonParser.parseString(json).getAsJsonObject();
    }

    private static JsonObject expectedHeader(String keyId) {
        JsonObject header = new JsonObject();
        header.addProperty("typ", "openid4vci-proof+jwt");
        header.addProperty("alg", "ES256");
        header.addProperty("kid", DID + "?versionId=" + VERSION + "#" + keyId);
        return header;
    }

    @Test
    public void headerCarriesHolderKidInsteadOfJwk() {
        for (String keyId : new String[] {"pin", "bio"}) {
            JsonObject header = part(ProofJwtBuilder.buildSigningInput(DID, VERSION, keyId, AUDIENCE, NONCE, IAT), 0);
            assertEquals(expectedHeader(keyId), header);
            assertFalse(header.has("jwk"));
        }
    }

    @Test
    public void payloadIsExactlyAudIatNonceWithoutIss() {
        JsonObject expected = new JsonObject();
        expected.addProperty("aud", AUDIENCE);
        expected.addProperty("iat", IAT);
        expected.addProperty("nonce", NONCE);
        JsonObject payload = part(ProofJwtBuilder.buildSigningInput(DID, VERSION, "pin", AUDIENCE, NONCE, IAT), 1);
        assertEquals(expected, payload);
        assertFalse(payload.has("iss"));
    }

    @Test
    public void kidVersionFollowsInput() {
        String signingInput = ProofJwtBuilder.buildSigningInput(DID, "7", "pin", AUDIENCE, NONCE, IAT);
        assertEquals(DID + "?versionId=7#pin", part(signingInput, 0).get("kid").getAsString());
    }

    @Test
    public void issuanceAndRevocationShareKid() {
        String issuance = ProofJwtBuilder.buildSigningInput(DID, VERSION, "bio", AUDIENCE, NONCE, IAT);
        String revocation = ProofJwtBuilder.buildRevocationSigningInput(DID, VERSION, "bio", NONCE, AUDIENCE, 0, IAT);
        assertEquals(part(issuance, 0).get("kid"), part(revocation, 0).get("kid"));
    }
}
