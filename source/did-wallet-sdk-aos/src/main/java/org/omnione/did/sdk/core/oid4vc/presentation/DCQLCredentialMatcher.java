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

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.format.mdoc.MdocClaimCodes;
import org.omnione.did.sdk.core.oid4vc.presentation.dcql.DCQLQueryValidator;
import org.omnione.did.sdk.core.oid4vc.presentation.format.MdocCredentialAdapter;
import org.omnione.did.sdk.core.oid4vc.presentation.format.ParsedCredential;
import org.omnione.did.sdk.core.oid4vc.presentation.format.SDJWTCredentialAdapter;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.MatchedCredential;
import org.omnione.did.sdk.datamodel.oid4vc.MdocCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwtCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.ClaimQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.CredentialQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.CredentialSetQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.DCQLQuery;
import org.omnione.did.sdk.datamodel.vc.Claim;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DCQLCredentialMatcher {

    private DCQLCredentialMatcher() {}

    public static final class MatchedClaim {
        private final String credentialId;
        private final List<String> claimCodes;

        public MatchedClaim(String credentialId, List<String> claimCodes) {
            this.credentialId = credentialId;
            this.claimCodes = claimCodes;
        }

        public String getCredentialId() { return credentialId; }
        public List<String> getClaimCodes() { return claimCodes; }
    }

    private interface ClaimNameExtractor {
        String nameOf(ClaimQuery claim);
    }

    private static final ClaimNameExtractor SD_JWT_CLAIM_NAME = DCQLCredentialMatcher::leafClaimName;
    private static final ClaimNameExtractor MDOC_CLAIM_NAME = DCQLCredentialMatcher::mdocClaimCode;

    public static Map<String, List<MatchedClaim>> matchCredentials(
            DCQLQuery query,
            List<VerifiableCredential> opendidVcStore,
            List<SdJwtCredentialItem> sdJwtStore,
            List<MdocCredentialItem> mdocStore) throws WalletCoreException {

        DCQLQueryValidator.validate(query);

        List<ParsedCredential> parsedSdJwt = new ArrayList<>();
        if (sdJwtStore != null) {
            for (SdJwtCredentialItem item : sdJwtStore) {
                parsedSdJwt.add(SDJWTCredentialAdapter.adapt(item));
            }
        }

        List<ParsedCredential> parsedMdoc = new ArrayList<>();
        if (mdocStore != null) {
            for (MdocCredentialItem item : mdocStore) {
                parsedMdoc.add(MdocCredentialAdapter.adapt(item));
            }
        }

        Map<String, List<MatchedClaim>> result = new LinkedHashMap<>();
        for (CredentialQuery cq : query.getCredentials()) {
            List<MatchedClaim> matches;
            String format = cq.getFormat();
            if (CredentialFormat.OPENDID_VC.matches(format) && opendidVcStore != null) {
                matches = matchOpendidVc(cq, opendidVcStore);
            } else if (CredentialFormat.SD_JWT_VC.matches(format) && !parsedSdJwt.isEmpty()) {
                matches = matchByParsedCredential(cq, parsedSdJwt, "vct_values", SD_JWT_CLAIM_NAME);
            } else if (CredentialFormat.MSO_MDOC.matches(format) && !parsedMdoc.isEmpty()) {
                matches = matchByParsedCredential(cq, parsedMdoc, "doctype_value", MDOC_CLAIM_NAME);
            } else {
                matches = Collections.emptyList();
            }
            if (!matches.isEmpty()) {
                result.put(cq.getId(), matches);
            }
        }

        if (result.isEmpty()) {
            throw new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VP_NO_MATCHED_CREDENTIALS);
        }

        requireCredentialSetsSatisfied(query, result.keySet());
        return result;
    }

    private static List<MatchedClaim> matchOpendidVc(CredentialQuery cq, List<VerifiableCredential> store) {
        List<String> schemaIds = cq.metaStringList("credential_schema_id_values");
        List<MatchedClaim> matches = new ArrayList<>();
        for (VerifiableCredential vc : store) {
            String schemaId = (vc.getCredentialSchema() != null) ? vc.getCredentialSchema().getId() : null;

            if (schemaIds.isEmpty() || (schemaId != null && schemaIds.contains(schemaId))) {

                matches.add(new MatchedClaim(vc.getId(), opendidClaimCodes(cq, vc)));
            }
        }
        return matches;
    }

    private static List<String> opendidClaimCodes(CredentialQuery cq, VerifiableCredential vc) {
        List<ClaimQuery> claims = cq.getClaims();
        List<String> codes = new ArrayList<>();
        if (claims != null && !claims.isEmpty()) {
            for (ClaimQuery claim : claims) {
                String code = leafClaimName(claim);
                if (code != null && !code.isEmpty() && !codes.contains(code)) {
                    codes.add(code);
                }
            }
            return codes;
        }
        if (vc.getCredentialSubject() != null && vc.getCredentialSubject().getClaims() != null) {
            for (Claim c : vc.getCredentialSubject().getClaims()) {
                String code = c.getCode();
                if (code != null && !code.isEmpty() && !codes.contains(code)) {
                    codes.add(code);
                }
            }
        }
        return codes;
    }

    private static List<MatchedClaim> matchByParsedCredential(CredentialQuery cq,
                                                              List<ParsedCredential> store,
                                                              String typeMetaKey,
                                                              ClaimNameExtractor extractor) {
        List<String> typeValues = cq.metaStringList(typeMetaKey);
        Set<String> trustedIssuers = trustedAuthorityValues(cq);

        List<MatchedClaim> matches = new ArrayList<>();
        for (ParsedCredential pc : store) {
            if (cq.getFormat() != null && !cq.getFormat().equals(pc.format())) {
                continue;
            }
            if (!typeValues.isEmpty()
                    && (pc.typeIdentifier() == null || !typeValues.contains(pc.typeIdentifier()))) {
                continue;
            }
            if (trustedIssuers != null && (pc.issuer() == null || !trustedIssuers.contains(pc.issuer()))) {
                continue;
            }
            List<String> claimCodes = resolveClaimCodes(cq, pc, extractor);
            if (claimCodes == null) {
                continue;
            }
            matches.add(new MatchedClaim(pc.credentialId(), claimCodes));
        }
        return matches;
    }

    private static List<String> resolveClaimCodes(CredentialQuery cq, ParsedCredential pc,
                                                  ClaimNameExtractor extractor) {
        List<ClaimQuery> claims = cq.getClaims();
        if (claims == null || claims.isEmpty()) {
            return new ArrayList<>(pc.availableClaimNames());
        }

        Map<String, String> idToClaimName = new LinkedHashMap<>();
        List<String> unIdentifiedClaimNames = new ArrayList<>();
        for (ClaimQuery claim : claims) {
            String claimName = extractor.nameOf(claim);
            if (claimName == null) {
                continue;
            }
            if (claim.getId() != null && !claim.getId().isEmpty()) {
                idToClaimName.put(claim.getId(), claimName);
            } else {
                unIdentifiedClaimNames.add(claimName);
            }
        }

        List<List<String>> claimSets = cq.getClaimSets();
        if (claimSets != null && !claimSets.isEmpty()) {
            for (List<String> option : claimSets) {
                List<String> codes = new ArrayList<>();
                boolean satisfiable = true;
                for (String claimId : option) {
                    String resolved = resolve(pc, idToClaimName.get(claimId));
                    if (resolved == null) {
                        satisfiable = false;
                        break;
                    }
                    codes.add(resolved);
                }
                if (satisfiable) {
                    return codes;
                }
            }
            return null;
        }

        List<String> codes = new ArrayList<>();
        for (String claimName : idToClaimName.values()) {
            String resolved = resolve(pc, claimName);
            if (resolved == null) {
                return null;
            }
            codes.add(resolved);
        }
        for (String claimName : unIdentifiedClaimNames) {
            String resolved = resolve(pc, claimName);
            if (resolved == null) {
                return null;
            }
            codes.add(resolved);
        }
        return codes;
    }

    private static String resolve(ParsedCredential pc, String claimName) {
        return (claimName != null) ? pc.resolveClaim(claimName) : null;
    }

    private static String leafClaimName(ClaimQuery claim) {
        if (claim.getPath() == null || claim.getPath().isEmpty()) {
            return null;
        }
        String leaf = null;
        for (Object segment : claim.getPath()) {
            if (segment instanceof String) {
                leaf = (String) segment;
            }
        }
        return leaf;
    }

    private static String mdocClaimCode(ClaimQuery claim) {
        if (claim.getNamespace() != null && !claim.getNamespace().isEmpty()
                && claim.getClaimName() != null && !claim.getClaimName().isEmpty()) {
            return MdocClaimCodes.of(claim.getNamespace(), claim.getClaimName());
        }
        List<Object> path = claim.getPath();
        if (path != null && path.size() == 2
                && path.get(0) instanceof String && path.get(1) instanceof String) {
            return MdocClaimCodes.of((String) path.get(0), (String) path.get(1));
        }
        return null;
    }

    private static Set<String> trustedAuthorityValues(CredentialQuery cq) {
        if (cq.getTrustedAuthorities() == null || cq.getTrustedAuthorities().isEmpty()) {
            return null;
        }
        Set<String> values = new LinkedHashSet<>();
        cq.getTrustedAuthorities().forEach(ta -> {
            if (ta.getValues() != null) {
                values.addAll(ta.getValues());
            }
        });
        return values;
    }

    public static void validateSelection(List<MatchedCredential> selection,
                                         List<MatchedCredential> matched,
                                         DCQLQuery dcqlQuery) throws WalletCoreException {
        Map<String, Map<String, Set<String>>> allowed = new LinkedHashMap<>();
        if (matched != null) {
            for (MatchedCredential m : matched) {
                allowed.computeIfAbsent(m.getQueryId(), k -> new LinkedHashMap<>())
                        .put(m.getCredentialId(), new LinkedHashSet<>(m.getClaimCodes()));
            }
        }

        Set<String> seen = new HashSet<>();
        for (MatchedCredential selected : (selection != null) ? selection : Collections.<MatchedCredential>emptyList()) {
            if (selected == null) {
                throw invalidSelection("a selected credential is null");
            }
            String queryId = selected.getQueryId();
            String credentialId = selected.getCredentialId();

            Map<String, Set<String>> credentials = allowed.get(queryId);
            if (credentials == null) {
                throw invalidSelection("no matched credential for query '" + queryId + "'");
            }
            Set<String> requiredClaims = credentials.get(credentialId);
            if (requiredClaims == null) {
                throw invalidSelection("credential '" + credentialId + "' does not match query '" + queryId + "'");
            }
            if (!seen.add(queryId + '\0' + credentialId)) {
                throw invalidSelection("credential '" + credentialId + "' is selected twice for query '" + queryId + "'");
            }

            List<String> claimCodes = selected.getClaimCodes();
            if (claimCodes == null || claimCodes.isEmpty()) {
                throw invalidSelection("no claim selected for credential '" + credentialId
                        + "' of query '" + queryId + "'");
            }

            List<String> missing = new ArrayList<>(requiredClaims);
            missing.removeAll(claimCodes);
            if (!missing.isEmpty()) {
                Collections.sort(missing);
                throw invalidSelection("query '" + queryId + "' requires claim(s) "
                        + String.join(", ", missing) + " of credential '" + credentialId + "'");
            }
        }

        requireSelectionCoversQueries(selection, allowed, dcqlQuery);
    }

    private static void requireSelectionCoversQueries(List<MatchedCredential> selection,
                                                      Map<String, Map<String, Set<String>>> allowed,
                                                      DCQLQuery dcqlQuery) throws WalletCoreException {
        Set<String> presentedQueryIds = new LinkedHashSet<>();
        if (selection != null) {
            for (MatchedCredential selected : selection) {
                presentedQueryIds.add(selected.getQueryId());
            }
        }

        List<CredentialSetQuery> sets = (dcqlQuery != null) ? dcqlQuery.getCredentialSets() : null;
        if (sets != null && !sets.isEmpty()) {
            requireCredentialSetsSatisfied(dcqlQuery, presentedQueryIds);
            return;
        }

        List<String> dropped = new ArrayList<>(allowed.keySet());
        dropped.removeAll(presentedQueryIds);
        if (!dropped.isEmpty()) {
            Collections.sort(dropped);
            throw invalidSelection("no credential selected for required query(s) " + String.join(", ", dropped));
        }
    }

    private static WalletCoreException invalidSelection(String detail) {
        return new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VP_INVALID_SELECTED_CREDENTIALS, detail);
    }

    private static void requireCredentialSetsSatisfied(DCQLQuery query, Set<String> matchedIds)
            throws WalletCoreException {
        List<CredentialSetQuery> sets = query.getCredentialSets();
        if (sets == null) {
            return;
        }
        for (CredentialSetQuery set : sets) {
            if (!set.isRequired()) {
                continue;
            }
            boolean satisfied = false;
            for (List<String> option : set.getOptions()) {
                if (matchedIds.containsAll(option)) {
                    satisfied = true;
                    break;
                }
            }
            if (!satisfied) {
                throw new WalletCoreException(
                        WalletCoreErrorCode.ERR_CODE_OID4VP_CREDENTIAL_SETS_NOT_SATISFIED,
                        set.getOptions().toString());
            }
        }
    }
}
