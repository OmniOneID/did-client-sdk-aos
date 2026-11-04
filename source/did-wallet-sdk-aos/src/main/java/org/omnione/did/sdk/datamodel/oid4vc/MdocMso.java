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

package org.omnione.did.sdk.datamodel.oid4vc;

public final class MdocMso {
    private final String version;
    private final String digestAlgorithm;
    private final String docType;
    private final String issuerKid;
    private final MdocValidityInfo validityInfo;

    public MdocMso(String version, String digestAlgorithm, String docType,
                   String issuerKid, MdocValidityInfo validityInfo) {
        this.version = version;
        this.digestAlgorithm = digestAlgorithm;
        this.docType = docType;
        this.issuerKid = issuerKid;
        this.validityInfo = validityInfo;
    }

    public String getVersion() {
        return version;
    }

    public String getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public String getDocType() {
        return docType;
    }

    public String getIssuerKid() {
        return issuerKid;
    }

    public String getIssuerDid() {
        if (issuerKid == null) {
            return null;
        }
        int cut = issuerKid.indexOf('?');
        if (cut < 0) {
            cut = issuerKid.indexOf('#');
        }
        return cut < 0 ? issuerKid : issuerKid.substring(0, cut);
    }

    public MdocValidityInfo getValidityInfo() {
        return validityInfo;
    }
}
