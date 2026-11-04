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

package org.omnione.did.sdk.core.oid4vc.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.oid4vc.MatchedCredential;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.CredentialSetQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.DCQLQuery;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DCQLCredentialMatcherSelectionTest {

    private static MatchedCredential mc(String queryId, String credentialId, String... claimCodes) {
        return new MatchedCredential(queryId, credentialId, Arrays.asList(claimCodes));
    }

    private static DCQLQuery noSets() {
        return new DCQLQuery();
    }

    private static DCQLQuery withRequiredSet(List<String>... options) {
        CredentialSetQuery set = new CredentialSetQuery();
        set.setOptions(Arrays.asList(options));
        set.setRequired(true);
        DCQLQuery query = new DCQLQuery();
        query.setCredentialSets(Collections.singletonList(set));
        return query;
    }

    private static final List<MatchedCredential> MATCHED = Arrays.asList(
            mc("q1", "cred-a", "name", "birth"),
            mc("q1", "cred-b", "name"),
            mc("q2", "cred-c", "address"));

    private static String assertRejected(List<MatchedCredential> selection,
                                         DCQLQuery query,
                                         WalletCoreErrorCode expected) {
        try {
            DCQLCredentialMatcher.validateSelection(selection, MATCHED, query);
            fail("expected " + expected.name());
            return null;
        } catch (WalletCoreException e) {
            assertEquals(expected.getCode(), e.getErrorCode());
            return e.getErrMsg();
        }
    }

    private static String assertRejected(List<MatchedCredential> selection) {
        return assertRejected(selection, noSets(),
                WalletCoreErrorCode.ERR_CODE_OID4VP_INVALID_SELECTED_CREDENTIALS);
    }

    @Test
    public void theUnchangedMatchingResultPasses() throws Exception {
        DCQLCredentialMatcher.validateSelection(MATCHED, MATCHED, noSets());
    }

    @Test
    public void droppingOneOfTwoCredentialsOfTheSameQueryPasses() throws Exception {
        DCQLCredentialMatcher.validateSelection(
                Arrays.asList(mc("q1", "cred-a", "name", "birth"), mc("q2", "cred-c", "address")),
                MATCHED, noSets());
    }

    @Test
    public void claimCodesBeyondTheMatchedSetAreLeftToThePresenter() throws Exception {
        DCQLCredentialMatcher.validateSelection(
                Arrays.asList(mc("q1", "cred-a", "name", "birth", "extra"), mc("q2", "cred-c", "address")),
                MATCHED, noSets());
    }

    @Test
    public void anUnmatchedQueryIsRejected() {
        String msg = assertRejected(Arrays.asList(mc("q9", "cred-a", "name"), mc("q2", "cred-c", "address")));
        assertTrue(msg, msg.contains("no matched credential for query 'q9'"));
    }

    @Test
    public void aCredentialThatDidNotMatchTheQueryIsRejected() {
        String msg = assertRejected(Arrays.asList(mc("q1", "cred-c", "name"), mc("q2", "cred-c", "address")));
        assertTrue(msg, msg.contains("credential 'cred-c' does not match query 'q1'"));
    }

    @Test
    public void theSameCredentialSelectedTwiceForOneQueryIsRejected() {
        String msg = assertRejected(Arrays.asList(
                mc("q1", "cred-a", "name", "birth"),
                mc("q1", "cred-a", "name", "birth"),
                mc("q2", "cred-c", "address")));
        assertTrue(msg, msg.contains("'cred-a' is selected twice for query 'q1'"));
    }

    @Test
    public void anEmptyClaimSelectionIsRejected() {
        String msg = assertRejected(Arrays.asList(mc("q1", "cred-a"), mc("q2", "cred-c", "address")));
        assertTrue(msg, msg.contains("no claim selected for credential 'cred-a' of query 'q1'"));
    }

    @Test
    public void narrowingBelowTheMatchedClaimsIsRejected() {
        String msg = assertRejected(Arrays.asList(mc("q1", "cred-a", "name"), mc("q2", "cred-c", "address")));
        assertTrue(msg, msg.contains("query 'q1' requires claim(s) birth of credential 'cred-a'"));
    }

    @Test
    public void droppingAMatchedQueryIsRejectedWhenThereAreNoCredentialSets() {
        String msg = assertRejected(Collections.singletonList(mc("q1", "cred-a", "name", "birth")));
        assertTrue(msg, msg.contains("no credential selected for required query(s) q2"));
    }

    @Test
    public void anEmptySelectionIsRejectedWhenThereAreNoCredentialSets() {
        String msg = assertRejected(Collections.emptyList());
        assertTrue(msg, msg.contains("q1, q2"));
    }

    @Test
    public void credentialSetsDecideWhichQueriesMayBeDropped() throws Exception {
        DCQLQuery query = withRequiredSet(Collections.singletonList("q1"), Collections.singletonList("q2"));
        DCQLCredentialMatcher.validateSelection(
                Collections.singletonList(mc("q1", "cred-a", "name", "birth")), MATCHED, query);

        assertRejected(Collections.emptyList(), query,
                WalletCoreErrorCode.ERR_CODE_OID4VP_CREDENTIAL_SETS_NOT_SATISFIED);
    }

    @Test
    public void anUnsatisfiedRequiredSetIsRejected() {
        assertRejected(Collections.singletonList(mc("q1", "cred-a", "name", "birth")),
                withRequiredSet(Collections.singletonList("q2")),
                WalletCoreErrorCode.ERR_CODE_OID4VP_CREDENTIAL_SETS_NOT_SATISFIED);
    }
}
