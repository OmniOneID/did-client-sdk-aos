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

package org.omnione.did.sdk.core.oid4vc.verify;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.omnione.did.sdk.core.oid4vc.format.CredentialStatusListReader;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.OID4VCICredential;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Map;

public class CredentialStatusListReaderTest {
    private static final String URI = "https://issuer.example.com/status-lists/1";
    private static KeyPair issuerKey;
    private static KeyPair holderKey;

    @BeforeClass
    public static void generateKeys() throws Exception {
        issuerKey = MdocFixtures.generateKeyPair();
        holderKey = MdocFixtures.generateKeyPair();
    }

    private static String b64(String s) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static OID4VCICredential sdJwt(String payloadJson) {
        return credential(CredentialFormat.SD_JWT_VC,
                b64("{\"alg\":\"ES256\"}") + "." + b64(payloadJson) + ".c2ln~");
    }

    private static OID4VCICredential mdoc(MdocFixtures.Builder builder) throws Exception {
        return credential(CredentialFormat.MSO_MDOC, builder.build());
    }

    private static OID4VCICredential credential(CredentialFormat format, String compact) {
        OID4VCICredential credential = new OID4VCICredential();
        credential.setId("vc-1");
        credential.setFormat(format.getValue());
        credential.setCredential(compact);
        return credential;
    }

    @Test
    public void s1_sdJwtStatusListIsRead() throws Exception {
        Map.Entry<String, Long> ref = CredentialStatusListReader.read(sdJwt(
                "{\"iss\":\"did:omn:issuer\",\"status\":{\"status_list\":{\"uri\":\"" + URI + "\",\"idx\":42}}}"));
        assertEquals(URI, ref.getKey());
        assertEquals(Long.valueOf(42), ref.getValue());
    }

    @Test
    public void m1_mdocStatusListIsReadFromMso() throws Exception {
        Map.Entry<String, Long> ref = CredentialStatusListReader.read(
                mdoc(new MdocFixtures.Builder(issuerKey, holderKey).statusList(URI, 42)));
        assertEquals(URI, ref.getKey());
        assertEquals(Long.valueOf(42), ref.getValue());

        Map.Entry<String, Long> wrapped = CredentialStatusListReader.read(
                mdoc(new MdocFixtures.Builder(issuerKey, holderKey).wrapInDeviceResponse().statusList(URI, 7)));
        assertEquals(URI, wrapped.getKey());
        assertEquals(Long.valueOf(7), wrapped.getValue());
    }
}
