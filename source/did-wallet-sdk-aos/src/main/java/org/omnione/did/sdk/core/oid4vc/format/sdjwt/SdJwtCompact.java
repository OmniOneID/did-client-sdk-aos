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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.Strictness;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SdJwtCompact {

    private static final TypeAdapter<JsonElement> JSON = new Gson().getAdapter(JsonElement.class);

    public static final class Disclosure {
        private final String encoded;
        private final String claimName;
        private final Object claimValue;

        private Disclosure(String encoded, String claimName, Object claimValue) {
            this.encoded = encoded;
            this.claimName = claimName;
            this.claimValue = claimValue;
        }

        public String getEncoded() { return encoded; }
        public String getClaimName() { return claimName; }
        public Object getClaimValue() { return claimValue; }
        public boolean isArrayElement() { return claimName == null; }
    }

    private final String credentialJwt;
    private final List<Disclosure> disclosures;

    private SdJwtCompact(String credentialJwt, List<Disclosure> disclosures) {
        this.credentialJwt = credentialJwt;
        this.disclosures = Collections.unmodifiableList(disclosures);
    }

    public String getCredentialJwt() { return credentialJwt; }
    public List<Disclosure> getDisclosures() { return disclosures; }

    public static SdJwtCompact parse(String compact) {
        if (compact == null || compact.trim().isEmpty()) {
            throw new IllegalArgumentException("SD-JWT string cannot be null or empty");
        }
        String[] parts = compact.split("~", -1);
        if (!isJwt(parts[0])) {
            throw new IllegalArgumentException("Invalid credential JWT format");
        }
        List<Disclosure> disclosures = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].isEmpty()) continue;
            if (isJwt(parts[i])) {
                for (int j = i + 1; j < parts.length; j++) {
                    if (!parts[j].isEmpty()) {
                        throw new IllegalArgumentException("Key binding JWT can only be the last component");
                    }
                }
                continue;
            }
            disclosures.add(parseDisclosure(parts[i]));
        }
        return new SdJwtCompact(parts[0].trim(), disclosures);
    }

    private static boolean isJwt(String s) {
        if (s.trim().isEmpty()) return false;
        String[] p = s.split("\\.");
        return p.length == 3 && !p[0].isEmpty() && !p[1].isEmpty();
    }

    private static Disclosure parseDisclosure(String encoded) {
        String json = new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
        if (json.startsWith("\uFEFF")) {
            throw new IllegalArgumentException("Disclosure must be a JSON array");
        }
        JsonElement tree;
        try (JsonReader reader = new JsonReader(new StringReader(json))) {
            reader.setStrictness(Strictness.STRICT);
            tree = JSON.read(reader);
        } catch (IOException | RuntimeException e) {
            throw new IllegalArgumentException("Invalid disclosure JSON", e);
        }
        if (!tree.isJsonArray()) {
            throw new IllegalArgumentException("Disclosure must be a JSON array");
        }
        JsonArray arr = tree.getAsJsonArray();
        if (arr.size() < 2 || arr.size() > 3) {
            throw new IllegalArgumentException("Disclosure array must have 2 or 3 elements");
        }
        if (asText(arr.get(0)).trim().isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty");
        }
        if (arr.size() == 2) {
            return new Disclosure(encoded, null, toJava(arr.get(1)));
        }
        String name = asText(arr.get(1));
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Claim name cannot be null or empty for object property");
        }
        return new Disclosure(encoded, name, toJava(arr.get(2)));
    }

    private static String asText(JsonElement e) {
        if (e.isJsonNull()) return "null";
        if (!e.isJsonPrimitive()) return "";
        return e.getAsJsonPrimitive().isNumber() ? String.valueOf(toJava(e)) : e.getAsString();
    }

    private static Object toJava(JsonElement e) {
        if (e.isJsonNull()) return null;
        if (e.isJsonObject()) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : e.getAsJsonObject().entrySet()) {
                map.put(entry.getKey(), toJava(entry.getValue()));
            }
            return map;
        }
        if (e.isJsonArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonElement item : e.getAsJsonArray()) list.add(toJava(item));
            return list;
        }
        JsonPrimitive p = e.getAsJsonPrimitive();
        if (p.isBoolean()) return p.getAsBoolean();
        if (p.isString()) return p.getAsString();
        String text = p.getAsString();
        if (text.indexOf('.') >= 0 || text.indexOf('e') >= 0 || text.indexOf('E') >= 0) {
            return Double.valueOf(text);
        }
        BigInteger n = new BigInteger(text);
        if (n.bitLength() < 32) return n.intValue();
        if (n.bitLength() < 64) return n.longValue();
        return n;
    }
}
