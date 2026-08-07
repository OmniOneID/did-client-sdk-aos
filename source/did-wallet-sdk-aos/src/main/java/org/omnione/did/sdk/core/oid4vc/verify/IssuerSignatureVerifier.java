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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.omnione.did.sdk.core.exception.WalletCoreErrorCode;
import org.omnione.did.sdk.core.exception.WalletCoreException;
import org.omnione.did.sdk.core.oid4vc.errors.Oid4vciException;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.did.DidDocVo;
import org.omnione.did.sdk.datamodel.did.VerificationMethod;
import org.omnione.did.sdk.datamodel.util.MessageUtil;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.ec.EcUtils;
import org.omnione.did.sdk.utility.Encodings.Base64;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public final class IssuerSignatureVerifier {

    private static final String SC_PROVIDER = "SC";

    static {
        if (Security.getProvider(SC_PROVIDER) == null) {
            Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
        }
    }

    private IssuerSignatureVerifier() {}

    public static CompletableFuture<Void> verify(@NonNull String compact, @NonNull String apiGatewayUrl) {
        return CompletableFuture.runAsync(() -> {
            try {
                verifyInternal(compact, apiGatewayUrl);
            } catch (Exception e) {
                throw new CompletionException(new WalletCoreException(
                        WalletCoreErrorCode.ERR_CODE_OID4VC_FAILED_TO_VERIFY_SIGNATURE, e.getMessage()));
            }
        });
    }

    private static void verifyInternal(@NonNull String compact, @NonNull String apiGatewayUrl) throws Exception {

        String jwt = compact;
        int tilde = compact.indexOf('~');
        if (tilde >= 0) jwt = compact.substring(0, tilde);
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new Oid4vciException("SD-JWT VC is not a 3-part JWS");
        }

        String headerJson = new String(b64u(parts[0]), StandardCharsets.UTF_8);
        JsonObject header = JsonParser.parseString(headerJson).getAsJsonObject();
        if (header.get("kid") == null) {
            throw new Oid4vciException("SD-JWT VC header has no kid");
        }
        String kid = header.get("kid").getAsString();
        Kid parsed = Kid.parse(kid);
        WalletLogger.getInstance().d("IssuerSignatureVerifier: verify kid=" + kid + " did=" + parsed.did
                + " versionId=" + parsed.versionId + " fragment=" + parsed.fragment);

        DIDDocument doc = resolveDidDocument(apiGatewayUrl, parsed.did, parsed.versionId);
        VerificationMethod vm = findVerificationMethod(doc, parsed.fragment);
        if (vm == null) {
            throw new Oid4vciException("DID Document has no verificationMethod '"
                    + parsed.fragment + "' for " + parsed.did);
        }

        byte[] compressed = MultibaseUtils.decode(vm.getPublicKeyMultibase());
        PublicKey publicKey = publicKeyFromCompressed(compressed);

        String signingInputStr = parts[0] + "." + parts[1];
        byte[] signingInput = signingInputStr.getBytes(StandardCharsets.UTF_8);
        byte[] rawSig = b64u(parts[2]);
        byte[] der = rawToDer(rawSig);
        Signature sig = Signature.getInstance("SHA256withECDSA", SC_PROVIDER);
        sig.initVerify(publicKey);
        sig.update(signingInput);
        boolean ok = sig.verify(der);
        WalletLogger.getInstance().d("IssuerSignatureVerifier: issuer signature verify=" + ok
                + " (vm=" + vm.getId() + ")");
        if (!ok) {
            throw new Oid4vciException(
                    "Issuer signature verification failed: the SD-JWT VC is not signed "
                            + "by the DID Document key '" + parsed.fragment + "' of "
                            + parsed.did + " (kid=" + kid + ")");
        }
    }

    @NonNull
    private static DIDDocument resolveDidDocument(@NonNull String apiGatewayUrl,
                                                  @NonNull String did, String versionId) throws Exception {
        WalletLogger.getInstance().d("IssuerSignatureVerifier: getDidDoc did=" + did + " versionId=" + versionId);
        String body = WalletUtil.getDidDoc(apiGatewayUrl, did, versionId).get();
        DidDocVo vo = MessageUtil.deserialize(body, DidDocVo.class);
        if (vo == null || vo.getDidDoc() == null) {
            throw new Oid4vciException("did-doc response missing didDoc for " + did);
        }
        String didDocJson = new String(MultibaseUtils.decode(vo.getDidDoc()), StandardCharsets.UTF_8);
        DIDDocument doc = new DIDDocument();
        doc.fromJson(didDocJson);
        return doc;
    }

    private static VerificationMethod findVerificationMethod(@NonNull DIDDocument doc,
                                                             @NonNull String fragment) {
        List<VerificationMethod> methods = doc.getVerificationMethod();
        if (methods == null) return null;
        for (VerificationMethod vm : methods) {
            String id = vm.getId();
            if (id == null) continue;
            if (id.equals(fragment) || id.endsWith("#" + fragment)) {
                return vm;
            }
        }
        return null;
    }

    @NonNull
    private static PublicKey publicKeyFromCompressed(@NonNull byte[] compressed) throws Exception {
        return EcUtils.getPublicKey(compressed, EcType.EC_TYPE.SECP256_R1.getValue());
    }

    @NonNull
    private static byte[] rawToDer(@NonNull byte[] raw) {
        if (raw.length != 64) {
            throw new IllegalArgumentException("Invalid ES256 signature length: " + raw.length);
        }
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(raw, 0, r, 0, 32);
        System.arraycopy(raw, 32, s, 0, 32);
        byte[] rBytes = new BigInteger(1, r).toByteArray();
        byte[] sBytes = new BigInteger(1, s).toByteArray();
        int length = 2 + rBytes.length + 2 + sBytes.length;
        byte[] der = new byte[2 + length];
        der[0] = 0x30;
        der[1] = (byte) length;
        int o = 2;
        der[o++] = 0x02;
        der[o++] = (byte) rBytes.length;
        System.arraycopy(rBytes, 0, der, o, rBytes.length);
        o += rBytes.length;
        der[o++] = 0x02;
        der[o++] = (byte) sBytes.length;
        System.arraycopy(sBytes, 0, der, o, sBytes.length);
        return der;
    }

    private static byte[] b64u(@NonNull String s) {
        return Base64.decodeUrl(s);
    }

    private static final class Kid {
        @NonNull final String did;
        final String versionId;
        @NonNull final String fragment;

        private Kid(@NonNull String did, String versionId, @NonNull String fragment) {
            this.did = did;
            this.versionId = versionId;
            this.fragment = fragment;
        }

        @NonNull
        static Kid parse(@NonNull String kid) {
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
            return new Kid(did, versionId, fragment);
        }
    }
}
