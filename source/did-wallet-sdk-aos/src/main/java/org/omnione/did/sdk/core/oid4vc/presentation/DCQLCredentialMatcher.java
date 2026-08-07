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
import org.omnione.did.sdk.core.oid4vc.presentation.dcql.DCQLQueryValidator;
import org.omnione.did.sdk.core.oid4vc.presentation.format.ParsedCredential;
import org.omnione.did.sdk.core.oid4vc.presentation.format.SDJWTCredentialAdapter;
import org.omnione.did.sdk.datamodel.oid4vc.CredentialFormat;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwtCredentialItem;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.ClaimQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.CredentialQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.CredentialSetQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.DCQLQuery;
import org.omnione.did.sdk.datamodel.vc.Claim;
import org.omnione.did.sdk.datamodel.vc.VerifiableCredential;

import java.util.ArrayList;
import java.util.Collections;
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

    public static Map<String, List<MatchedClaim>> matchCredentials(
            DCQLQuery query,
            List<VerifiableCredential> opendidVcStore,
            List<SdJwtCredentialItem> sdJwtStore) throws WalletCoreException {

        DCQLQueryValidator.validate(query);

        List<ParsedCredential> parsedSdJwt = new ArrayList<>();
        if (sdJwtStore != null) {
            for (SdJwtCredentialItem item : sdJwtStore) {
                parsedSdJwt.add(SDJWTCredentialAdapter.adapt(item));
            }
        }

        Map<String, List<MatchedClaim>> result = new LinkedHashMap<>();
        for (CredentialQuery cq : query.getCredentials()) {
            List<MatchedClaim> matches;
            String format = cq.getFormat();
            if (CredentialFormat.OPENDID_VC.matches(format) && opendidVcStore != null) {
                matches = matchOpendidVc(cq, opendidVcStore);
            } else if (CredentialFormat.SD_JWT_VC.matches(format) && !parsedSdJwt.isEmpty()) {
                matches = matchSdJwt(cq, parsedSdJwt);
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

    private static List<MatchedClaim> matchSdJwt(CredentialQuery cq, List<ParsedCredential> store) {
        List<String> vctValues = cq.metaStringList("vct_values");
        Set<String> trustedIssuers = trustedAuthorityValues(cq);

        List<MatchedClaim> matches = new ArrayList<>();
        for (ParsedCredential pc : store) {
            if (cq.getFormat() != null && !cq.getFormat().equals(pc.format())) {
                continue;
            }
            if (!vctValues.isEmpty() && (pc.vct() == null || !vctValues.contains(pc.vct()))) {
                continue;
            }
            if (trustedIssuers != null && (pc.issuer() == null || !trustedIssuers.contains(pc.issuer()))) {
                continue;
            }
            List<String> claimCodes = resolveClaimCodes(cq, pc);
            if (claimCodes == null) {
                continue;
            }
            matches.add(new MatchedClaim(pc.credentialId(), claimCodes));
        }
        return matches;
    }

    private static List<String> resolveClaimCodes(CredentialQuery cq, ParsedCredential pc) {
        List<ClaimQuery> claims = cq.getClaims();
        if (claims == null || claims.isEmpty()) {
            return new ArrayList<>(pc.availableClaimNames());
        }

        Map<String, String> idToClaimName = new LinkedHashMap<>();
        List<String> unIdentifiedClaimNames = new ArrayList<>();
        for (ClaimQuery claim : claims) {
            String claimName = leafClaimName(claim);
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
                    String claimName = idToClaimName.get(claimId);
                    if (claimName == null || !pc.hasClaim(claimName)) {
                        satisfiable = false;
                        break;
                    }
                    codes.add(claimName);
                }
                if (satisfiable) {
                    return codes;
                }
            }
            return null;
        }

        List<String> codes = new ArrayList<>();
        for (String claimName : idToClaimName.values()) {
            if (!pc.hasClaim(claimName)) {
                return null;
            }
            codes.add(claimName);
        }
        for (String claimName : unIdentifiedClaimNames) {
            if (!pc.hasClaim(claimName)) {
                return null;
            }
            codes.add(claimName);
        }
        return codes;
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
