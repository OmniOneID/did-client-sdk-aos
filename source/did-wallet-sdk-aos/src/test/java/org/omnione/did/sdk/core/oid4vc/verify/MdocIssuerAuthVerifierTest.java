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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonObject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocParser;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocValidator;
import org.omnione.did.sdk.datamodel.oid4vc.Mdoc;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;

public class MdocIssuerAuthVerifierTest {
    private static KeyPair issuerKey;
    private static KeyPair holderKey;
    private static KeyPair otherKey;
    private static JsonObject holderJwk;

    private static final long NOW = Instant.parse("2026-08-10T00:00:00Z").getEpochSecond();

    @BeforeClass
    public static void generateKeys() throws Exception {
        issuerKey = MdocFixtures.generateKeyPair();
        holderKey = MdocFixtures.generateKeyPair();
        otherKey = MdocFixtures.generateKeyPair();
        holderJwk = MdocFixtures.holderJwk((ECPublicKey) holderKey.getPublic());
    }

    private static MdocFixtures.Builder credential() {
        return new MdocFixtures.Builder(issuerKey, holderKey);
    }

    private static MdocIssuerAuthVerifier.IssuerKeyResolver resolvesTo(PublicKey key) {
        return kid -> key;
    }

    private static void verify(String compact) throws Exception {
        MdocIssuerAuthVerifier.verifyInternal(compact, resolvesTo(issuerKey.getPublic()),
                holderJwk, MdocFixtures.DEFAULT_DOC_TYPE, NOW);
    }

    private static WalletCoreException expectFailure(String compact) throws Exception {
        try {
            verify(compact);
        } catch (WalletCoreException e) {
            return e;
        }
        fail("expected verification to fail");
        return null;
    }

    @Test
    public void acceptsTag24ItemEncoding() throws Exception {
        verify(credential().itemEncoding(MdocFixtures.ItemEncoding.TAG24).build());
    }

    @Test
    public void rejectsDigestsComputedOverBareByteStringEncoding() throws Exception {
        WalletCoreException e = expectFailure(
                credential().itemEncoding(MdocFixtures.ItemEncoding.BARE).build());
        assertTrue("message should mention valueDigests: " + e.getMessage(),
                e.getMessage().contains("valueDigests"));
    }

    @Test
    public void acceptsKidAsByteStringInUnprotectedHeader() throws Exception {
        verify(credential().kidPlacement(MdocFixtures.KidPlacement.UNPROTECTED_BSTR).build());
    }

    @Test
    public void acceptsKidAsTextStringInUnprotectedHeader() throws Exception {
        verify(credential().kidPlacement(MdocFixtures.KidPlacement.UNPROTECTED_TSTR).build());
    }

    @Test
    public void acceptsKidInProtectedHeader() throws Exception {
        verify(credential().kidPlacement(MdocFixtures.KidPlacement.PROTECTED_BSTR).build());
    }

    @Test
    public void acceptsCredentialWrappedInDeviceResponse() throws Exception {
        verify(credential().wrapInDeviceResponse().build());
    }

    @Test
    public void acceptsUnhyphenatedDigestAlgorithmName() throws Exception {
        verify(credential().digestAlgorithm("SHA-256").build());
    }

    @Test
    public void rejectsMissingKid() throws Exception {
        WalletCoreException e = expectFailure(
                credential().kidPlacement(MdocFixtures.KidPlacement.ABSENT).build());
        assertEquals(WalletCoreErrorCode.ERR_CODE_OID4VC_NOT_FOUND_KID.getCode(), e.getErrorCode());
    }

    @Test
    public void rejectsX5chainOnlyAndSaysDidIsRequired() throws Exception {
        WalletCoreException e = expectFailure(
                credential().kidPlacement(MdocFixtures.KidPlacement.X5CHAIN_ONLY).build());
        assertEquals(WalletCoreErrorCode.ERR_CODE_OID4VC_NOT_FOUND_KID.getCode(), e.getErrorCode());
        assertTrue("message should name x5chain: " + e.getMessage(),
                e.getMessage().contains("x5chain"));
    }

    @Test
    public void rejectsSignatureFromAnotherKey() throws Exception {
        String compact = credential().build();
        try {
            MdocIssuerAuthVerifier.verifyInternal(compact, resolvesTo(otherKey.getPublic()),
                    holderJwk, MdocFixtures.DEFAULT_DOC_TYPE, NOW);
            fail("expected verification to fail");
        } catch (WalletCoreException e) {
            assertEquals(WalletCoreErrorCode.ERR_CODE_OID4VC_FAILED_TO_VERIFY_SIGNATURE.getCode(),
                    e.getErrorCode());
        }
    }

    @Test
    public void rejectsTamperedElementValue() throws Exception {
        WalletCoreException e = expectFailure(credential().tamperFirstElementValue().build());
        assertTrue("message should mention valueDigests: " + e.getMessage(),
                e.getMessage().contains("valueDigests"));
    }

    @Test
    public void rejectsDocTypeMismatch() throws Exception {
        WalletCoreException e = expectFailure(credential().docType("eu.europa.ec.eudi.pid.1").build());
        assertTrue("message should mention docType: " + e.getMessage(),
                e.getMessage().contains("docType"));
    }

    @Test
    public void rejectsExpiredCredential() throws Exception {
        long from = Instant.parse("2026-01-01T00:00:00Z").getEpochSecond();
        long until = Instant.parse("2026-02-01T00:00:00Z").getEpochSecond();
        WalletCoreException e = expectFailure(credential().validity(from, until).build());
        assertTrue("message should say expired: " + e.getMessage(), e.getMessage().contains("expired"));
    }

    @Test
    public void rejectsNotYetValidCredential() throws Exception {
        long from = Instant.parse("2027-01-01T00:00:00Z").getEpochSecond();
        long until = Instant.parse("2028-01-01T00:00:00Z").getEpochSecond();
        WalletCoreException e = expectFailure(credential().validity(from, until).build());
        assertTrue("message should say not yet valid: " + e.getMessage(),
                e.getMessage().contains("not yet valid"));
    }

    @Test
    public void rejectsMissingValidityInfo() throws Exception {
        WalletCoreException e = expectFailure(credential().omitValidityInfo().build());
        assertTrue("message should mention validityInfo: " + e.getMessage(),
                e.getMessage().contains("validityInfo"));
    }

    @Test
    public void rejectsDeviceKeyThatIsNotTheHolderProofKey() throws Exception {
        WalletCoreException e = expectFailure(credential().deviceKey(otherKey).build());
        assertTrue("message should mention deviceKey: " + e.getMessage(),
                e.getMessage().contains("deviceKey"));
    }

    @Test
    public void rejectsMissingDeviceKey() throws Exception {
        WalletCoreException e = expectFailure(credential().omitDeviceKey().build());
        assertTrue("message should mention deviceKeyInfo: " + e.getMessage(),
                e.getMessage().contains("deviceKeyInfo"));
    }

    @Test
    public void rejectsBrokenBase64WithoutLeakingCborException() throws Exception {
        try {
            verify("!!!not-base64!!!");
            fail("expected verification to fail");
        } catch (WalletCoreException expected) {
            assertNotNull(expected.getMessage());
        }
    }

    @Test
    public void rejectsValidBase64ThatIsNotMdocCbor() throws Exception {
        try {
            verify("YWJjZGVmZ2g");
            fail("expected verification to fail");
        } catch (WalletCoreException expected) {
            assertNotNull(expected.getMessage());
        }
    }

    @Test
    public void parsesNameSpacesAndValidityIntoTheDataModel() throws Exception {
        String compact = credential()
                .element("portrait", new byte[] {1, 2, 3})
                .build();
        MdocValidator.validateFormat(compact);
        Mdoc mdoc = MdocParser.parse(compact);

        assertEquals(MdocFixtures.DEFAULT_DOC_TYPE, mdoc.getDocType());
        assertEquals(compact, mdoc.getCompact());
        assertEquals("Kim", mdoc.getNameSpaces()
                .get(MdocFixtures.DEFAULT_NAMESPACE).get("family_name"));
        assertTrue("portrait should stay a byte array",
                mdoc.getFlatClaims().get("portrait") instanceof byte[]);
        assertEquals(MdocFixtures.DEFAULT_KID, mdoc.getMso().getIssuerKid());
        assertEquals("did:omn:issuer", mdoc.getMso().getIssuerDid());
        assertNotNull(mdoc.getMso().getValidityInfo().getValidUntilEpochSec());
    }

    @Test
    public void parsesBareByteStringItemEncoding() throws Exception {
        Mdoc mdoc = MdocParser.parse(
                credential().itemEncoding(MdocFixtures.ItemEncoding.BARE).build());
        assertEquals("Raon", mdoc.getNameSpaces()
                .get(MdocFixtures.DEFAULT_NAMESPACE).get("given_name"));
    }
}
