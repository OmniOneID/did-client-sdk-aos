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

import org.omnione.did.sdk.core.oid4vc.errors.Oid4vciException;
import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.did.DidDocVo;
import org.omnione.did.sdk.datamodel.did.VerificationMethod;
import org.omnione.did.sdk.datamodel.util.MessageUtil;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.ec.EcUtils;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;
import org.omnione.did.sdk.wallet.walletservice.util.WalletUtil;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.List;

public final class DidVerificationKeyResolver {
    private DidVerificationKeyResolver() {}

    @NonNull
    public static PublicKey resolve(@NonNull String apiGatewayUrl, @NonNull String kid) throws Exception {
        return resolve(apiGatewayUrl, DidKid.parse(kid));
    }

    @NonNull
    public static PublicKey resolve(@NonNull String apiGatewayUrl, @NonNull DidKid kid) throws Exception {
        DIDDocument doc = resolveDidDocument(apiGatewayUrl, kid.getDid(), kid.getVersionId());
        VerificationMethod vm = findVerificationMethod(doc, kid.getFragment());
        if (vm == null) {
            throw new Oid4vciException("DID Document has no verificationMethod '"
                    + kid.getFragment() + "' for " + kid.getDid());
        }
        byte[] compressed = MultibaseUtils.decode(vm.getPublicKeyMultibase());
        return publicKeyFromCompressed(compressed);
    }

    @NonNull
    public static DIDDocument resolveDidDocument(@NonNull String apiGatewayUrl,
                                                 @NonNull String did, String versionId) throws Exception {
        WalletLogger.getInstance().d("DidVerificationKeyResolver: getDidDoc did=" + did + " versionId=" + versionId);
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

    public static VerificationMethod findVerificationMethod(@NonNull DIDDocument doc,
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
    public static PublicKey publicKeyFromCompressed(@NonNull byte[] compressed) throws Exception {
        return EcUtils.getPublicKey(compressed, EcType.EC_TYPE.SECP256_R1.getValue());
    }
}
