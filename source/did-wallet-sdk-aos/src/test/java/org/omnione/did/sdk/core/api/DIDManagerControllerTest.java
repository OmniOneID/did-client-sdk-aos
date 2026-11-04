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

import org.junit.Test;
import org.omnione.did.sdk.core.didmanager.datamodel.DIDKeyInfo;
import org.omnione.did.sdk.core.didmanager.datamodel.DIDMethodType;
import org.omnione.did.sdk.core.keymanager.datamodel.KeyInfo;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.did.VerificationMethod;

import java.util.ArrayList;
import java.util.List;

public class DIDManagerControllerTest {
    private static final String DID = "did:omn:holder";
    private static final String TAS = "did:omn:tas";

    private static final class UnsavedDIDManager extends DIDManager<DIDDocument> {
        @Override
        public boolean isSaved() {
            return false;
        }
    }

    private static List<DIDKeyInfo> keyInfos(String controller, String... ids) {
        List<DIDKeyInfo> infos = new ArrayList<>();
        for (String id : ids) {
            KeyInfo keyInfo = new KeyInfo();
            keyInfo.setId(id);
            keyInfo.setPublicKey("z" + id);
            infos.add(new DIDKeyInfo(keyInfo, List.of(DIDMethodType.DID_METHOD_TYPE.authentication), controller));
        }
        return infos;
    }

    private static void assertAllControllers(String expected, DIDDocument document) {
        for (VerificationMethod method : document.getVerificationMethod()) {
            assertEquals(method.getId(), expected, method.getController());
        }
    }

    @Test
    public void holderCreateUsesDocumentIdAsVerificationMethodController() throws Exception {
        DIDManager<DIDDocument> manager = new UnsavedDIDManager();
        manager.createDocument(DID, keyInfos(null, "pin", "bio", "keyagree"), TAS, null);
        DIDDocument document = manager.getDocument();
        assertEquals(TAS, document.getController());
        assertEquals(3, document.getVerificationMethod().size());
        assertAllControllers(DID, document);
    }

    @Test
    public void holderUpdateKeepsDocumentIdAsVerificationMethodController() throws Exception {
        DIDManager<DIDDocument> manager = new UnsavedDIDManager();
        manager.createDocument(DID, keyInfos(null, "pin", "keyagree"), TAS, null);
        manager.updateDocument(keyInfos(null, "pin", "bio", "keyagree"), null);
        DIDDocument document = manager.getDocument();
        assertEquals(TAS, document.getController());
        assertEquals(3, document.getVerificationMethod().size());
        assertAllControllers(DID, document);
    }

    @Test
    public void deviceCreateUsesDocumentIdAsVerificationMethodController() throws Exception {
        DIDManager<DIDDocument> manager = new UnsavedDIDManager();
        manager.createDocument("did:omn:device", keyInfos(null, "assert", "auth", "keyagree"), TAS, null);
        DIDDocument document = manager.getDocument();
        assertEquals(TAS, document.getController());
        assertEquals(3, document.getVerificationMethod().size());
        assertAllControllers("did:omn:device", document);
    }
}
