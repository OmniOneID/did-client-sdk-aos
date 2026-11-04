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

package org.omnione.did.sdk.core.oid4vc.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;
import org.omnione.did.sdk.communication.exception.CommunicationErrorCode;
import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.communication.urlconnection.HttpUrlConnectionTask;

import java.util.concurrent.ExecutionException;

public class RevocationRequesterTest {
    private static final String ENDPOINT = "https://issuer.example.com/opendid/credential-lifecycle/v1/revoke";
    private static final String RESPONSE = "{\"credential_status\":{\"status\":\"INVALID\"}}";

    private static final class FakeHttp extends HttpUrlConnectionTask {
        final CommunicationException failure;
        String url;
        String method;
        String payload;

        FakeHttp(CommunicationException failure) {
            this.failure = failure;
        }

        @Override
        public String makeHttpRequest(String urlString, String method, String payload, String accessToken)
                throws CommunicationException {
            this.url = urlString;
            this.method = method;
            this.payload = payload;
            if (failure != null) throw failure;
            return RESPONSE;
        }
    }

    @Test
    public void postsRevocationTokenAsJson() throws Exception {
        FakeHttp http = new FakeHttp(null);
        assertEquals(RESPONSE, new RevocationRequester(http).request(ENDPOINT, "h.p.s").get());
        assertEquals(ENDPOINT, http.url);
        assertEquals("POST", http.method);
        JsonObject expected = new JsonObject();
        expected.addProperty("revocation_token", "h.p.s");
        assertEquals(expected, JsonParser.parseString(http.payload));
    }

    @Test
    public void communicationFailureIsTheExecutionCause() throws Exception {
        CommunicationException failure = new CommunicationException(
                CommunicationErrorCode.ERR_CODE_COMMUNICATION_SERVER_FAIL,
                "{\"code\":\"SSRVISS00210\",\"description\":\"The issuer nonce is not valid.\"}");
        try {
            new RevocationRequester(new FakeHttp(failure)).request(ENDPOINT, "h.p.s").get();
            fail("expected revocation failure");
        } catch (ExecutionException e) {
            assertSame(failure, e.getCause());
        }
    }
}
