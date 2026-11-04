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
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DidKidTest {
    @Test
    public void parsesDidVersionIdAndFragment() {
        DidKid kid = DidKid.parse("did:omn:issuer?versionId=1#assert");
        assertEquals("did:omn:issuer", kid.getDid());
        assertEquals("1", kid.getVersionId());
        assertEquals("assert", kid.getFragment());
    }

    @Test
    public void parsesWithoutQueryLeavesVersionIdNull() {
        DidKid kid = DidKid.parse("did:omn:issuer#assert");
        assertEquals("did:omn:issuer", kid.getDid());
        assertNull(kid.getVersionId());
        assertEquals("assert", kid.getFragment());
    }

    @Test
    public void picksVersionIdAmongMultipleQueryParameters() {
        DidKid kid = DidKid.parse("did:omn:i?a=1&versionId=3&b=2#key-1");
        assertEquals("did:omn:i", kid.getDid());
        assertEquals("3", kid.getVersionId());
        assertEquals("key-1", kid.getFragment());
    }

    @Test
    public void parsesWithoutFragmentYieldsEmptyFragment() {
        DidKid kid = DidKid.parse("did:omn:issuer");
        assertEquals("did:omn:issuer", kid.getDid());
        assertNull(kid.getVersionId());
        assertEquals("", kid.getFragment());
    }
}
