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

import com.google.gson.JsonObject;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;

import org.omnione.did.sdk.core.oid4vc.crypto.JwsParts;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.security.Provider;
import java.security.Security;

public final class Es256Verifier {

    private static final String SC_PROVIDER = "SC";

    static {

        try {
            if (Security.getProvider(SC_PROVIDER) == null) {
                Class<?> cls = Class.forName("org.spongycastle.jce.provider.BouncyCastleProvider");
                java.security.Provider p = (java.security.Provider) cls.getDeclaredConstructor().newInstance();
                Security.addProvider(p);
                WalletLogger.getInstance().d("Es256Verifier: registered provider " + p.getName());
            }
        } catch (Throwable t) {
            WalletLogger.getInstance().d("Es256Verifier: SpongyCastle provider not available: " + t);
        }
    }

    private Es256Verifier() {}

    public static boolean verify(JwsParts parts, JsonObject jwk) throws Exception {

        ECKey ecKey = ECKey.parse(normalizeJwk(jwk).toString());

        String compact = parts.header() + "." + parts.payload() + "." + parts.signature();
        JWSObject jws = JWSObject.parse(compact);

        ECDSAVerifier verifier = new ECDSAVerifier(ecKey);
        Provider sc = Security.getProvider(SC_PROVIDER);
        if (sc != null) {
            verifier.getJCAContext().setProvider(sc);
        }

        boolean ok = jws.verify(verifier);
        WalletLogger.getInstance().d("Es256Verifier: verify alg=" + jws.getHeader().getAlgorithm()
                + " crv=" + ecKey.getCurve()
                + " provider=" + (sc != null ? sc.getName() : "default")
                + " signedDataLen=" + (parts.header().length() + 1 + parts.payload().length())
                + " result=" + ok);
        return ok;
    }

    private static JsonObject normalizeJwk(JsonObject jwk) {
        if (jwk.get("x") == null || jwk.get("y") == null) {
            throw new IllegalArgumentException("JWK missing x or y");
        }
        JsonObject out = jwk.deepCopy();
        if (out.get("kty") == null) {
            out.addProperty("kty", "EC");
        }
        if (out.get("crv") != null) {
            String crv = out.get("crv").getAsString();
            if ("secp256r1".equalsIgnoreCase(crv)) {
                out.addProperty("crv", "P-256");
            } else if ("P-256K".equalsIgnoreCase(crv)) {
                out.addProperty("crv", "secp256k1");
            }
        }
        return out;
    }
}
