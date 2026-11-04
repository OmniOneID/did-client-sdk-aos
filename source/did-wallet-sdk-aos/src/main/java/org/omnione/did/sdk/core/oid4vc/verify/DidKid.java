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

import androidx.annotation.NonNull;

public final class DidKid {
    private final String did;
    private final String versionId;
    private final String fragment;

    private DidKid(@NonNull String did, String versionId, @NonNull String fragment) {
        this.did = did;
        this.versionId = versionId;
        this.fragment = fragment;
    }

    @NonNull
    public static DidKid parse(@NonNull String kid) {
        String fragment = "";
        String beforeFrag = kid;
        int hash = kid.indexOf('#');
        if (hash >= 0) {
            fragment = kid.substring(hash + 1);
            beforeFrag = kid.substring(0, hash);
        }
        String did = beforeFrag;
        String versionId = null;
        int q = beforeFrag.indexOf('?');
        if (q >= 0) {
            did = beforeFrag.substring(0, q);
            for (String kv : beforeFrag.substring(q + 1).split("&")) {
                int eq = kv.indexOf('=');
                if (eq > 0 && "versionId".equals(kv.substring(0, eq))) {
                    versionId = kv.substring(eq + 1);
                }
            }
        }
        return new DidKid(did, versionId, fragment);
    }

    @NonNull
    public String getDid() {
        return did;
    }

    public String getVersionId() {
        return versionId;
    }

    @NonNull
    public String getFragment() {
        return fragment;
    }
}
