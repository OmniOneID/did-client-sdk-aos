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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class ProofJwtBuilderRevocationTest {
    private static final String DID = "did:omn:holder";
    private static final String VERSION = "3";
    private static final String NONCE = "c-nonce-1";
    private static final String URI = "https://issuer.example.com/status-lists/1";
    private static final long IAT = 1791937200L;

    private static String signingInput(String keyId) {
        return ProofJwtBuilder.buildRevocationSigningInput(DID, VERSION, keyId, NONCE, URI, 42, IAT);
    }

    private static JsonObject part(String jws, int index) {
        String json = new String(Base64.getUrlDecoder().decode(jws.split("\\.")[index]), StandardCharsets.UTF_8);
        return JsonParser.parseString(json).getAsJsonObject();
    }

    private static JsonObject expectedHeader(String keyId) {
        JsonObject header = new JsonObject();
        header.addProperty("alg", "ES256");
        header.addProperty("typ", "opendid-credential-revocation+jwt");
        header.addProperty("kid", DID + "?versionId=" + VERSION + "#" + keyId);
        return header;
    }

    @Test
    public void j1_headerUsesPinOrBioKid() {
        assertEquals(expectedHeader("pin"), part(signingInput("pin"), 0));
        assertEquals(expectedHeader("bio"), part(signingInput("bio"), 0));
    }

    @Test
    public void j2_payloadCarriesExactlyTheRevocationClaims() {
        JsonObject payload = part(signingInput("pin"), 1);
        Set<String> expectedKeys = new HashSet<>();
        expectedKeys.add("iss");
        expectedKeys.add("iat");
        expectedKeys.add("exp");
        expectedKeys.add("nonce");
        expectedKeys.add("credential_status");
        assertEquals(expectedKeys, payload.keySet());
        assertEquals(DID, payload.get("iss").getAsString());
        assertEquals(NONCE, payload.get("nonce").getAsString());

        JsonObject statusList = payload.getAsJsonObject("credential_status").getAsJsonObject("status_list");
        assertEquals(URI, statusList.get("uri").getAsString());
        JsonElement idx = statusList.get("idx");
        assertTrue(idx.getAsJsonPrimitive().isNumber());
        assertEquals(42L, idx.getAsLong());
    }

    @Test
    public void j3_lifetimeIsAtMostFiveMinutes() {
        JsonObject payload = part(signingInput("pin"), 1);
        long iat = payload.get("iat").getAsLong();
        long exp = payload.get("exp").getAsLong();
        assertEquals(IAT, iat);
        assertTrue(exp > iat);
        assertTrue(exp - iat <= 300);
    }

    @Test
    public void j4_assembleRequiresRaw64ByteSignature() throws Exception {
        String input = signingInput("pin");
        String jws = ProofJwtBuilder.assemble(input, new byte[64]);
        String[] parts = jws.split("\\.");
        assertEquals(3, parts.length);
        assertEquals(64, Base64.getUrlDecoder().decode(parts[2]).length);

        try {
            ProofJwtBuilder.assemble(input, new byte[65]);
            fail("expected 65-byte signature to be rejected");
        } catch (WalletException e) {
            assertEquals(WalletErrorCode.ERR_CODE_WALLET_CREATE_PROOF_FAIL.getCode(), e.getErrorCode());
        }
    }
}
