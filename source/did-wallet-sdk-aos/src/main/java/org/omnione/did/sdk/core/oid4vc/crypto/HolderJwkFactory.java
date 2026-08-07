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

package org.omnione.did.sdk.core.oid4vc.crypto;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;

import org.omnione.did.sdk.datamodel.did.DIDDocument;
import org.omnione.did.sdk.datamodel.did.VerificationMethod;
import org.omnione.did.sdk.utility.DataModels.EcType;
import org.omnione.did.sdk.utility.DataModels.ec.EcUtils;
import org.omnione.did.sdk.utility.MultibaseUtils;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletErrorCode;
import org.omnione.did.sdk.wallet.walletservice.exception.WalletException;

import java.security.Security;
import java.security.interfaces.ECPublicKey;
import java.util.List;

public final class HolderJwkFactory {

    private static final String SC_PROVIDER = "SC";

    static {
        if (Security.getProvider(SC_PROVIDER) == null) {
            Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
        }
    }

    private HolderJwkFactory() {}

    @NonNull
    public static JsonObject fromHolderDocument(@NonNull DIDDocument holderDoc, @NonNull String keyId) throws WalletException {
        try {
            VerificationMethod vm = findVerificationMethod(holderDoc, keyId);
            if (vm == null) {
                throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_CREATE_PROOF_FAIL,
                        "Holder DID Document has no verificationMethod '" + keyId + "'");
            }
            byte[] compressed = MultibaseUtils.decode(vm.getPublicKeyMultibase());
            ECPublicKey pub = publicKeyFromCompressed(compressed);
            String json = new ECKey.Builder(Curve.P_256, pub).build().toJSONString();
            return JsonParser.parseString(json).getAsJsonObject();
        } catch (WalletException e) {
            throw e;
        } catch (Exception e) {
            throw new WalletException(WalletErrorCode.ERR_CODE_WALLET_CREATE_PROOF_FAIL,
                    "Failed to build holder JWK: " + e.getMessage());
        }
    }

    private static VerificationMethod findVerificationMethod(@NonNull DIDDocument doc,
                                                             @NonNull String keyId) {
        List<VerificationMethod> methods = doc.getVerificationMethod();
        if (methods == null) return null;
        for (VerificationMethod vm : methods) {
            String id = vm.getId();
            if (id == null) continue;
            if (id.equals(keyId) || id.endsWith("#" + keyId)) {
                return vm;
            }
        }
        return null;
    }

    @NonNull
    private static ECPublicKey publicKeyFromCompressed(@NonNull byte[] compressed) throws Exception {
        return EcUtils.getPublicKey(compressed, EcType.EC_TYPE.SECP256_R1.getValue());
    }
}
