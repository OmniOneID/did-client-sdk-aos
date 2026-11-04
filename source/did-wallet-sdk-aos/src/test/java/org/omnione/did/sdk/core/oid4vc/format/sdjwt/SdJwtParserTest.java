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

package org.omnione.did.sdk.core.oid4vc.format.sdjwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.gson.JsonObject;

import org.junit.Test;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.presentation.SDJWTPresenter;
import org.omnione.did.sdk.datamodel.oid4vc.SdJwt;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class SdJwtParserTest {

    static String b64(String s) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    static final String JWT = b64("{\"alg\":\"ES256\"}") + "." + b64("{\"iss\":\"did:example:issuer\"}") + ".c2ln";
    static final String KB = b64("{\"typ\":\"kb+jwt\"}") + "." + b64("{\"nonce\":\"n\"}") + ".a2I";

    static final String D_NAME = b64("[\"s1\",\"given_name\",\"Kim\"]");
    static final String D_INT = b64("[\"s2\",\"age\",20]");
    static final String D_DOUBLE = b64("[\"s3\",\"height\",172.5]");
    static final String D_BOOL = b64("[\"s4\",\"adult\",true]");
    static final String D_OBJECT = b64("[\"s5\",\"address\",{\"city\":\"Seoul\",\"zip\":12345,\"geo\":[1.0,2]}]");
    static final String D_ARRAY = b64("[\"s6\",\"nationalities\",[\"KR\",1,false,null]]");
    static final String D_LONG = b64("[\"s7\",\"serial\",9007199254740993]");
    static final String D_BIGINT = b64("[\"s8\",\"huge\",123456789012345678901234567890]");
    static final String D_NULL = b64("[\"s9\",\"nothing\",null]");
    static final String D_ELEMENT = b64("[\"s10\",\"KR\"]");
    static final String D_SPACED = b64("[\"s11\", \"spaced\", \"v\"]");
    static final String D_NEGATIVE = b64("[\"s12\",\"delta\",-7]");
    static final String D_EXP = b64("[\"s13\",\"ratio\",1e2]");

    static final List<String> ALL = Arrays.asList(D_NAME, D_INT, D_DOUBLE, D_BOOL, D_OBJECT, D_ARRAY,
            D_LONG, D_BIGINT, D_NULL, D_ELEMENT, D_SPACED, D_NEGATIVE, D_EXP);

    static final List<String> JACKSON_CANONICAL = Arrays.asList(D_NAME, D_INT, D_DOUBLE, D_BOOL, D_OBJECT,
            D_ARRAY, D_LONG, D_BIGINT, D_NULL, D_ELEMENT, D_NEGATIVE);

    static String compact(boolean trailingTilde, String kb) {
        return compact(ALL, trailingTilde, kb);
    }

    static String compact(List<String> disclosures, boolean trailingTilde, String kb) {
        StringBuilder sb = new StringBuilder(JWT);
        for (String d : disclosures) sb.append('~').append(d);
        if (trailingTilde || kb != null) sb.append('~');
        if (kb != null) sb.append(kb);
        return sb.toString();
    }

    static String describe(Object v) {
        if (v == null) return "null";
        if (v instanceof Map) {
            StringBuilder sb = new StringBuilder(v.getClass().getSimpleName()).append('{');
            for (Map.Entry<?, ?> e : ((Map<?, ?>) v).entrySet()) {
                sb.append(e.getKey()).append('=').append(describe(e.getValue())).append(',');
            }
            return sb.append('}').toString();
        }
        if (v instanceof List) {
            StringBuilder sb = new StringBuilder(v.getClass().getSimpleName()).append('[');
            for (Object o : (List<?>) v) sb.append(describe(o)).append(',');
            return sb.append(']').toString();
        }
        return v.getClass().getSimpleName() + ":" + v;
    }

    static String describeClaims(SdJwt sdjwt) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> e : sdjwt.getDisclosedClaims().entrySet()) {
            sb.append(e.getKey()).append('=').append(describe(e.getValue())).append('\n');
        }
        return sb.toString();
    }

    static String vpToken(SdJwt sdjwt, List<String> codes) throws Exception {
        JsonObject jwk = new JsonObject();
        jwk.addProperty("kty", "EC");
        return SDJWTPresenter.createVpToken(sdjwt, codes, "aud-1", "nonce-1", jwk, data -> new byte[64], 1700000000L);
    }

    static String parseError(String compact) {
        try {
            SdJwtParser.parse(compact);
            return "ok";
        } catch (WalletCoreException e) {
            return "error";
        }
    }

    static final String EXPECTED_CLAIMS =
            "given_name=String:Kim\n"
            + "age=Integer:20\n"
            + "height=Double:172.5\n"
            + "adult=Boolean:true\n"
            + "address=LinkedHashMap{city=String:Seoul,zip=Integer:12345,geo=ArrayList[Double:1.0,Integer:2,],}\n"
            + "nationalities=ArrayList[String:KR,Integer:1,Boolean:false,null,]\n"
            + "serial=Long:9007199254740993\n"
            + "huge=BigInteger:123456789012345678901234567890\n"
            + "nothing=null\n"
            + "spaced=String:v\n"
            + "delta=Integer:-7\n"
            + "ratio=Double:100.0\n";

    @Test
    public void claimValueTypesMatchJar() throws Exception {
        assertEquals(EXPECTED_CLAIMS, describeClaims(SdJwtParser.parse(compact(true, null))));
        assertEquals(EXPECTED_CLAIMS, describeClaims(SdJwtParser.parse(compact(false, null))));
        assertEquals(EXPECTED_CLAIMS, describeClaims(SdJwtParser.parse(compact(true, KB))));
    }

    @Test
    public void credentialJwtIsFirstSegment() throws Exception {
        assertEquals(JWT, SdJwtParser.parse(compact(true, KB)).getCredentialJwt());
        assertEquals(JWT, SdJwtParser.parse(JWT).getCredentialJwt());
        assertEquals(JWT, SdJwtParser.parse(JWT + "~").getCredentialJwt());
        assertEquals("", describeClaims(SdJwtParser.parse(JWT + "~~")));
    }

    @Test
    public void malformedInputIsRejected() {
        assertEquals("error", parseError(JWT + "~" + KB + "~" + D_NAME + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("{\"a\":1}") + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("[\"s\",\"a\",1,2]") + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("[\"s\"]") + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("[\"\",\"a\",1]") + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("[\"s\",\"\",1]") + "~"));
        assertEquals("error", parseError(JWT + "~not*base64~"));
        assertEquals("error", parseError("not-a-jwt~" + D_NAME + "~"));
        assertEquals("error", parseError("~" + D_NAME + "~"));
        assertEquals("error", parseError("  "));
        assertEquals("error", parseError(JWT + "~" + b64("\uFEFF[\"s\",\"n\",1]") + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("['s','n',1]") + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("[\"s\",\"n\",\"a\tb\"]") + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("[\"s\",\"n\",\"a\u0001b\"]") + "~"));
        assertEquals("error", parseError(JWT + "~" + b64("[\"s\",\"n\",\"it\\'s\"]") + "~"));
    }

    @Test
    public void numericClaimNameUsesNormalizedText() throws Exception {
        SdJwt sdjwt = SdJwtParser.parse(JWT + "~" + b64("[\"s\",1.50,\"v\"]") + "~" + b64("[\"t\",1e2,\"w\"]") + "~");
        assertEquals("1.5=String:v\n100.0=String:w\n", describeClaims(sdjwt));
    }

    static final String EXPECTED_VP_ALL =
            "eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJkaWQ6ZXhhbXBsZTppc3N1ZXIifQ.c2ln~WyJzMSIsImdpdmVuX25hbWUiLCJLaW0iXQ~WyJzMiIsImFnZSIsMjBd~WyJzMyIsImhlaWdodCIsMTcyLjVd~WyJzNCIsImFkdWx0Iix0cnVlXQ~WyJzNSIsImFkZHJlc3MiLHsiY2l0eSI6IlNlb3VsIiwiemlwIjoxMjM0NSwiZ2VvIjpbMS4wLDJdfV0~WyJzNiIsIm5hdGlvbmFsaXRpZXMiLFsiS1IiLDEsZmFsc2UsbnVsbF1d~WyJzNyIsInNlcmlhbCIsOTAwNzE5OTI1NDc0MDk5M10~WyJzOCIsImh1Z2UiLDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MF0~WyJzOSIsIm5vdGhpbmciLG51bGxd~WyJzMTAiLCJLUiJd~WyJzMTIiLCJkZWx0YSIsLTdd~eyJ0eXAiOiJrYitqd3QiLCJhbGciOiJFUzI1NiIsImp3ayI6eyJrdHkiOiJFQyJ9fQ.eyJpYXQiOjE3MDAwMDAwMDAsImF1ZCI6ImF1ZC0xIiwibm9uY2UiOiJub25jZS0xIiwic2RfaGFzaCI6IlZQVUR6NlVHd01jZXBlcWNiZFVMcnYwRUJSRnBXWGlFS2FzSGNfdi1qWE0ifQ.AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EXPECTED_VP_SELECTED =
            "eyJhbGciOiJFUzI1NiJ9.eyJpc3MiOiJkaWQ6ZXhhbXBsZTppc3N1ZXIifQ.c2ln~WyJzMiIsImFnZSIsMjBd~WyJzNSIsImFkZHJlc3MiLHsiY2l0eSI6IlNlb3VsIiwiemlwIjoxMjM0NSwiZ2VvIjpbMS4wLDJdfV0~eyJ0eXAiOiJrYitqd3QiLCJhbGciOiJFUzI1NiIsImp3ayI6eyJrdHkiOiJFQyJ9fQ.eyJpYXQiOjE3MDAwMDAwMDAsImF1ZCI6ImF1ZC0xIiwibm9uY2UiOiJub25jZS0xIiwic2RfaGFzaCI6Ik1zZlNDWkJJZE5xLVlqNERXbFBlZVV5Y2xfTmlhb0RsMjhSWko5Q0w1c28ifQ.AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @Test
    public void vpTokenMatchesJar() throws Exception {
        assertEquals(EXPECTED_VP_ALL, vpToken(SdJwtParser.parse(compact(JACKSON_CANONICAL, true, KB)), null));
        assertEquals(EXPECTED_VP_SELECTED, vpToken(SdJwtParser.parse(compact(JACKSON_CANONICAL, true, null)),
                Arrays.asList("age", "address", "KR")));
    }

    @Test
    public void vpTokenKeepsOriginalDisclosureEncoding() throws Exception {
        String vp = vpToken(SdJwtParser.parse(compact(true, null)), Arrays.asList("spaced", "ratio"));
        assertEquals(JWT + "~" + D_SPACED + "~" + D_EXP + "~", vp.substring(0, vp.lastIndexOf('~') + 1));
    }
}
