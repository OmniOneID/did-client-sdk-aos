/*
 * Copyright 2024-2025 OmniOne.
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

package org.omnione.did.sdk.utility;

import org.omnione.did.sdk.utility.DataModels.DigestEnum;
import org.omnione.did.sdk.utility.Errors.UtilityErrorCode;
import org.omnione.did.sdk.utility.Errors.UtilityException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {

    /**
     * Provides hash functionality.
     *
     * This method generates a hash of the given source byte array using the specified digest algorithm.
     * It supports SHA-256, SHA-384, and SHA-512 algorithms.
     *
     * @param source The input byte array to be hashed. Throws an exception if null.
     * @param digestEnum The enum representing the desired digest algorithm. Throws an exception if null or unsupported.
     * @return The hash of the input byte array as a byte array.
     * @throws Exception If the source or digestEnum is null, or if the digest algorithm is unsupported.
     */
    public static byte[] getDigest(byte[] source, DigestEnum.DIGEST_ENUM digestEnum) throws UtilityException {
        if (source == null) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_DIGEST_UTILS_INVALID_PARAMETER, "source");
        }
        switch (digestEnum) {
            case SHA_256:
                return getShaDigest(source, "SHA-256");
            case SHA_384:
                return getShaDigest(source, "SHA-384");
            case SHA_512:
                return getShaDigest(source, "SHA-512");
            default:
                throw new UtilityException(UtilityErrorCode.ERR_CODE_DIGEST_UTILS_UNSUPPORTED_ALGORITHM_TYPE, digestEnum.getValue());
        }
    }

    /**
     * Computes the SHA digest for the given input byte array using the specified algorithm.
     *
     * @param source The input byte array to be hashed.
     * @param algorithm The name of the SHA algorithm to be used (e.g., "SHA-256").
     * @return The hash of the input byte array as a byte array.
     * @throws Exception If the algorithm is not supported.
     */
    private static byte[] getShaDigest(byte[] source, String algorithm) throws UtilityException{
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_DIGEST_UTILS_UNSUPPORTED_ALGORITHM_TYPE, algorithm);
        }
        return digest.digest(source);
    }
}
