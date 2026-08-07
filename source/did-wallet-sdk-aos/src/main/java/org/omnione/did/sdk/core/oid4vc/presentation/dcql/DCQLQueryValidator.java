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

package org.omnione.did.sdk.core.oid4vc.presentation.dcql;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.CredentialQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.CredentialSetQuery;
import org.omnione.did.sdk.datamodel.oid4vc.dcql.DCQLQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DCQLQueryValidator {

    private DCQLQueryValidator() {}

    public static void validate(DCQLQuery query) throws WalletCoreException {
        if (query == null || query.getCredentials() == null) {
            throw invalid("missing 'credentials'");
        }
        List<CredentialQuery> credentials = query.getCredentials();
        if (credentials.isEmpty()) {
            throw invalid("'credentials' is empty");
        }

        Set<String> ids = new HashSet<>();
        for (CredentialQuery cq : credentials) {
            if (cq == null) {
                throw invalid("null credential query");
            }
            if (cq.getId() == null || cq.getId().isEmpty()) {
                throw invalid("credential query missing 'id'");
            }
            if (cq.getFormat() == null || cq.getFormat().isEmpty()) {
                throw invalid("credential query '" + cq.getId() + "' missing 'format'");
            }
            if (!ids.add(cq.getId())) {
                throw invalid("duplicate credential query id '" + cq.getId() + "'");
            }
        }

        if (query.getCredentialSets() != null) {
            for (CredentialSetQuery set : query.getCredentialSets()) {
                if (set == null || set.getOptions() == null || set.getOptions().isEmpty()) {
                    throw invalid("credential_set has no options");
                }
                for (List<String> option : set.getOptions()) {
                    if (option == null || option.isEmpty()) {
                        throw invalid("credential_set option is empty");
                    }
                    for (String qid : option) {
                        if (!ids.contains(qid)) {
                            throw invalid("credential_set references unknown query id '" + qid + "'");
                        }
                    }
                }
            }
        }
    }

    private static WalletCoreException invalid(String detail) {
        return new WalletCoreException(WalletCoreErrorCode.ERR_CODE_OID4VP_INVALID_DCQL_QUERY, detail);
    }
}
