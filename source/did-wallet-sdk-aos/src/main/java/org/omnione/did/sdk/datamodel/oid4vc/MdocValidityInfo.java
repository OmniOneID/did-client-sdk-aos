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

public final class MdocValidityInfo {
    private final Long signedEpochSec;
    private final Long validFromEpochSec;
    private final Long validUntilEpochSec;
    private final Long expectedUpdateEpochSec;

    public MdocValidityInfo(Long signedEpochSec, Long validFromEpochSec,
                            Long validUntilEpochSec, Long expectedUpdateEpochSec) {
        this.signedEpochSec = signedEpochSec;
        this.validFromEpochSec = validFromEpochSec;
        this.validUntilEpochSec = validUntilEpochSec;
        this.expectedUpdateEpochSec = expectedUpdateEpochSec;
    }

    public Long getSignedEpochSec() {
        return signedEpochSec;
    }

    public Long getValidFromEpochSec() {
        return validFromEpochSec;
    }

    public Long getValidUntilEpochSec() {
        return validUntilEpochSec;
    }

    public Long getExpectedUpdateEpochSec() {
        return expectedUpdateEpochSec;
    }
}
