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

package org.omnione.did.sdk.core.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.zkp.Credential;
import org.omnione.did.sdk.datamodel.zkp.ProofParam;
import org.omnione.did.sdk.datamodel.zkp.ProofRequest;
import org.omnione.did.sdk.datamodel.zkp.ProveCredential;
import org.omnione.did.sdk.datamodel.zkp.ProveRevealedAttribute;
import org.omnione.did.sdk.datamodel.zkp.ProveUnrevealedAttribute;
import org.omnione.did.sdk.datamodel.zkp.ReferentAttributeValue;
import org.omnione.did.sdk.datamodel.zkp.UserReferent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ZKPManagerReferentTest {
    private static final Gson GSON = new Gson();
    private static final String A1 = "\"a1\":{\"name\":\"birth\"}";
    private static final String A2 = "\"a2\":{\"name\":\"birth\"}";
    private static final String A3 = "\"a3\":{\"name\":\"name\"}";
    private static final String P1 = "\"p1\":{\"name\":\"birth\",\"pType\":\"GE\",\"pValue\":19000101}";
    private static final String P2 = "\"p2\":{\"name\":\"birth\",\"pType\":\"LE\",\"pValue\":20101231}";
    private static final String P3 = "\"p3\":{\"name\":\"age\",\"pType\":\"GE\",\"pValue\":19}";

    private static Credential credential(String id) {
        return GSON.fromJson("{\"credentialId\":\"" + id + "\",\"values\":{\"birth\":{\"raw\":\"20000101\"},\"name\":{\"raw\":\"kim\"},\"age\":{\"raw\":\"26\"}}}", Credential.class);
    }

    private static ProofRequest request(List<String> attrs, List<String> preds) {
        return GSON.fromJson("{\"requestedAttributes\":{" + String.join(",", attrs) + "},\"requestedPredicates\":{" + String.join(",", preds) + "}}", ProofRequest.class);
    }

    private static UserReferent pick(String credentialId, String key, String name, boolean revealed) {
        return new UserReferent.Builder()
                .setCredentialId(credentialId)
                .setRaw("raw")
                .setReferentKey(key)
                .setReferentName(name)
                .setRevealed(revealed)
                .build();
    }

    private static List<ProveCredential> prove(ProofRequest request, List<Credential> credentials, List<List<UserReferent>> picksPerParam) throws Exception {
        List<ProofParam> params = new ArrayList<>();
        for (List<UserReferent> picks : picksPerParam) {
            params.add(new ProofParam.Builder()
                    .setReferentInfo(ZKPManager.createReferent(picks, credentials))
                    .build());
        }
        return ZKPManager.getProvingCredential(request, params, credentials);
    }

    private static ProveCredential proveOne(ProofRequest request, UserReferent... picks) throws Exception {
        List<ProveCredential> result = prove(request, List.of(credential("c1")), List.of(List.of(picks)));
        assertEquals(1, result.size());
        return result.get(0);
    }

    private static List<String> revealedKeys(ProveCredential c) {
        return c.getRevealedAttrs().stream().map(ProveRevealedAttribute::getReferentKey).collect(Collectors.toList());
    }

    private static List<String> unrevealedKeys(ProveCredential c) {
        return c.getUnrevealedAttrs().stream().map(ProveUnrevealedAttribute::getReferentKey).collect(Collectors.toList());
    }

    private static List<String> predicates(ProveCredential c) {
        return c.getPredicates().stream()
                .map(p -> p.getReferentKey() + ":" + p.getAttrName() + ":" + p.getPType() + ":" + p.getPValue())
                .collect(Collectors.toList());
    }

    private static void assertRejected(String code, String reason, ThrowingRunnable call) {
        WalletCoreException e = assertThrows(WalletCoreException.class, call);
        assertTrue(e.getMessage(), e.getMessage().startsWith("[MSDKWLT" + code + "]"));
        assertTrue(e.getMessage(), e.getMessage().contains(reason));
    }

    private static void assertRejected(String code, String reason, ProofRequest request, UserReferent... picks) {
        assertRejected(code, reason, () -> proveOne(request, picks));
    }

    @Test
    public void twoPredicatesOnOneAttributeAreBothKept() throws Exception {
        ProveCredential c = proveOne(request(List.of(), List.of(P1, P2, P3)),
                pick("c1", "p1", "birth", false), pick("c1", "p2", "birth", false), pick("c1", "p3", "age", false));

        assertEquals(List.of("p1:birth:GE:19000101", "p2:birth:LE:20101231", "p3:age:GE:19"), predicates(c));
    }

    @Test
    public void twoRevealedAttributesOnOneNameAreBothKept() throws Exception {
        ProveCredential c = proveOne(request(List.of(A1, A2, A3), List.of()),
                pick("c1", "a1", "birth", true), pick("c1", "a2", "birth", true), pick("c1", "a3", "name", true));

        assertEquals(List.of("a1", "a2", "a3"), revealedKeys(c));
        assertEquals(Set.of("birth", "name"), c.getRevealedAttrs().stream().map(ProveRevealedAttribute::getAttributeName).collect(Collectors.toSet()));
    }

    @Test
    public void predicateSelectedUnderAnotherAttributeNameIsRejected() {
        assertRejected("04501", "predicate name mismatch: p1", request(List.of(), List.of(P1)),
                pick("c1", "p1", "age", false));
    }

    @Test
    public void attributeSelectedUnderAnotherAttributeNameIsRejected() {
        assertRejected("04500", "attribute name mismatch: a1", request(List.of(A1), List.of()),
                pick("c1", "a1", "name", true));
    }

    @Test
    public void unrevealedAttributeAndPredicateShareOneHiddenValue() throws Exception {
        ProofRequest request = request(List.of(A1), List.of(P1));
        ProveCredential attributeFirst = proveOne(request, pick("c1", "a1", "birth", false), pick("c1", "p1", "birth", false));
        ProveCredential predicateFirst = proveOne(request, pick("c1", "p1", "birth", false), pick("c1", "a1", "birth", false));

        for (ProveCredential c : List.of(attributeFirst, predicateFirst)) {
            assertEquals(List.of(), revealedKeys(c));
            assertEquals(List.of("a1"), unrevealedKeys(c));
            assertEquals(List.of("p1:birth:GE:19000101"), predicates(c));
        }
    }

    @Test
    public void predicateOnRevealedAttributeIsRejected() {
        ProofRequest request = request(List.of(A1), List.of(P1));
        assertRejected("04501", "predicate on revealed attribute: p1", request,
                pick("c1", "a1", "birth", true), pick("c1", "p1", "birth", false));
        assertRejected("04501", "predicate on revealed attribute: p1", request,
                pick("c1", "p1", "birth", false), pick("c1", "a1", "birth", true));
    }

    @Test
    public void revealedAndUnrevealedReferentsOnOneAttributeAreRejected() {
        ProofRequest request = request(List.of(A1, A2), List.of());
        assertRejected("04500", "revealed and unrevealed on one attribute: birth", request,
                pick("c1", "a1", "birth", true), pick("c1", "a2", "birth", false));
        assertRejected("04500", "revealed and unrevealed on one attribute: birth", request,
                pick("c1", "a2", "birth", false), pick("c1", "a1", "birth", true));
    }

    @Test
    public void predicateOnAttributeMissingFromCredentialIsRejected() {
        assertRejected("04501", "predicate attribute missing from credential: p9",
                request(List.of(), List.of("\"p9\":{\"name\":\"salary\",\"pType\":\"GE\",\"pValue\":1}")),
                pick("c1", "p9", "salary", false));
    }

    @Test
    public void itemsSelectedForAnotherCredentialStayOutOfThisSubProof() throws Exception {
        List<ProveCredential> result = prove(request(List.of(A1, A2), List.of(P1)), List.of(credential("c1"), credential("c2")),
                List.of(List.of(pick("c1", "a1", "birth", true)),
                        List.of(pick("c2", "a2", "birth", false), pick("c2", "p1", "birth", false))));

        assertEquals(2, result.size());
        assertEquals(List.of("a1"), revealedKeys(result.get(0)));
        assertEquals(List.of(), unrevealedKeys(result.get(0)));
        assertEquals(List.of(), predicates(result.get(0)));
        assertEquals(List.of(), revealedKeys(result.get(1)));
        assertEquals(List.of("a2"), unrevealedKeys(result.get(1)));
        assertEquals(List.of("p1:birth:GE:19000101"), predicates(result.get(1)));
    }

    @Test
    public void unselectedRequestItemsAreRejected() {
        assertRejected("04500", "requested attributes not all selected", request(List.of(A1, A2), List.of()),
                pick("c1", "a1", "birth", true));
        assertRejected("04501", "requested predicates not all selected", request(List.of(), List.of(P1, P2)),
                pick("c1", "p1", "birth", false));
    }

    @Test
    public void distinctAttributesKeepTheirExistingShape() throws Exception {
        ProveCredential c = proveOne(request(List.of(A3, A1), List.of(P3)),
                pick("c1", "a3", "name", true), pick("c1", "a1", "birth", false), pick("c1", "p3", "age", false));

        assertEquals("c1", c.getCredentialId());
        assertEquals(List.of("a3"), revealedKeys(c));
        assertEquals("name", c.getRevealedAttrs().get(0).getAttributeName());
        assertEquals(List.of("a1"), unrevealedKeys(c));
        assertEquals(List.of("p3:age:GE:19"), predicates(c));
    }

    @Test
    public void credentialsInOneProofParamDoNotShareItems() throws Exception {
        List<ProveCredential> result = prove(request(List.of(A3), List.of(P3)), List.of(credential("c1"), credential("c2")),
                List.of(List.of(pick("c1", "a3", "name", true), pick("c2", "p3", "age", false))));

        assertEquals(2, result.size());
        assertEquals(List.of("a3"), revealedKeys(result.get(0)));
        assertEquals(List.of(), predicates(result.get(0)));
        assertEquals(List.of(), revealedKeys(result.get(1)));
        assertEquals(List.of("p3:age:GE:19"), predicates(result.get(1)));
    }

    @Test
    public void unknownReferentKeyIsRejected() {
        assertRejected("04000", "unknown referent key: x1", request(List.of(A1), List.of()),
                pick("c1", "a1", "birth", true), pick("c1", "x1", "birth", true));
    }

    @Test
    public void referentKeySelectedTwiceIsRejected() {
        assertRejected("04603", "duplicated referent key: a1", request(List.of(A1), List.of()),
                pick("c1", "a1", "birth", true), pick("c1", "a1", "birth", false));
        assertRejected("04603", "duplicated referent key: a1",
                () -> prove(request(List.of(A1), List.of()), List.of(credential("c1"), credential("c2")),
                        List.of(List.of(pick("c1", "a1", "birth", true), pick("c2", "a1", "birth", true)))));
    }

    @Test
    public void createReferentKeysAttributesByReferentKey() throws Exception {
        Map<String, ReferentAttributeValue> attrs = ZKPManager.createReferent(List.of(pick("c1", "p1", "birth", false), pick("c1", "p2", "birth", false)), List.of(credential("c1")))
                .getReferents().get("c1").getAttributes();

        assertEquals(List.of("p1", "p2"), new ArrayList<>(attrs.keySet()));
        assertEquals("birth", attrs.get("p1").getAttrName());
        assertEquals("p2", attrs.get("p2").getReferentKey());
    }
}
