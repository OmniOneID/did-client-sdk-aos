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

import org.bitcoinj.core.Base58;
import org.omnione.did.sdk.utility.DataModels.MultibaseType;
import org.omnione.did.sdk.utility.Encodings.Base16;
import org.omnione.did.sdk.utility.Encodings.Base16Upper;
import org.omnione.did.sdk.utility.Encodings.Base64;
import org.omnione.did.sdk.utility.Errors.UtilityErrorCode;
import org.omnione.did.sdk.utility.Errors.UtilityException;

public class MultibaseUtils {
    /**
     * Multibase encoding.
     *
     * This method encodes the given byte array into a Multibase string using the specified Multibase type.
     *
     * @param type The Multibase type to use for encoding.
     * @param data The byte array to encode.
     * @return The Multibase encoded string.
     */
    public static String encode(MultibaseType.MULTIBASE_TYPE type, byte[] data){
        if(data == null){
            return "";
        }
        StringBuilder result = new StringBuilder(type.getValue());

        switch (type) {
            case BASE_16:
                result.append(bytesToHex(data));
                break;
            case BASE_16_UPPER:
                result.append(bytesToHexUpper(data));
                break;
            case BASE_58_BTC:
                result.append(encodeBase58(data));
                break;
            case BASE_64:
                result.append(encodeBase64(data));
                break;
            case BASE_64_URL:
                result.append(encodeBase64URL(data));
                break;
        }

        return result.toString();

    }

    /**
     * Multibase decoding.
     *
     * This method decodes the given Multibase encoded string back into a byte array.
     *
     * @param encoded The Multibase encoded string to decode.
     * @return The decoded byte array.
     * @throws Exception If the input string is invalid or the decoding fails.
     */
    public static byte[] decode(String encoded) throws UtilityException{
        if (encoded.length() < 2) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_MULTIBASE_UTILS_INVALID_PARAMETER, "encoded");
        }

        String firstString = encoded.substring(0, 1);
        String remainString = encoded.substring(1);

        MultibaseType.MULTIBASE_TYPE type = getMultibaseEnum(firstString);

        if (type == null) {
            throw new UtilityException(UtilityErrorCode.ERR_CODE_MULTIBASE_UTILS_FAIL_TO_DECODE);
        }

        switch (type) {
            case BASE_16:
                return hexToBytes(remainString);
            case BASE_16_UPPER:
                return hexUpperToBytes(remainString);
            case BASE_58_BTC:
                return decodeBase58(remainString);
            case BASE_64:
                return decodeBase64(remainString);
            case BASE_64_URL:
                return decodeBase64URL(remainString);
        }

        return null;
    }

    /**
     * Converts a byte array to a hexadecimal string (lowercase).
     *
     * @param bytes The byte array to convert.
     * @return The hexadecimal string representation.
     */
    private static String bytesToHex(byte[] bytes) {
        return Base16.toHex(bytes);
    }

    /**
     * Converts a hexadecimal string (lowercase) to a byte array.
     *
     * @param hex The hexadecimal string to convert.
     * @return The byte array representation.
     */
    private static byte[] hexToBytes(String hex) {
        return Base16.toBytes(hex);
    }

    /**
     * Converts a byte array to a hexadecimal string (uppercase).
     *
     * @param bytes The byte array to convert.
     * @return The uppercase hexadecimal string representation.
     */
    private static String bytesToHexUpper(byte[] bytes) {
        return Base16Upper.toHex(bytes).toUpperCase();
    }

    /**
     * Converts an uppercase hexadecimal string to a byte array.
     *
     * @param hex The uppercase hexadecimal string to convert.
     * @return The byte array representation.
     */
    private static byte[] hexUpperToBytes(String hex) {
        return Base16Upper.toBytes(hex);
    }

    /**
     * Encodes a byte array to a Base58 string.
     *
     * @param source The byte array to encode.
     * @return The Base58 encoded string.
     */
    private static String encodeBase58(byte[] source) {
        return Base58.encode(source);
    }

    /**
     * Decodes a Base58 string to a byte array.
     *
     * @param base58 The Base58 string to decode.
     * @return The decoded byte array.
     */
    private static byte[] decodeBase58(String base58) {
        return Base58.decode(base58);
    }

    /**
     * Encodes a byte array to a Base64 string.
     *
     * @param source The byte array to encode.
     * @return The Base64 encoded string.
     */
    private static String encodeBase64(byte[] source) {
        return Base64.encodeToString(source, Base64.NO_WRAP);
    }

    /**
     * Decodes a Base64 string to a byte array.
     *
     * @param base64 The Base64 string to decode.
     * @return The decoded byte array.
     */
    private static byte[] decodeBase64(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP);
    }

    /**
     * Encodes a byte array to a URL-safe Base64 string.
     *
     * @param source The byte array to encode.
     * @return The URL-safe Base64 encoded string.
     */
    private static String encodeBase64URL(byte[] source) {
        return Base64.encodeUrlString(source);
    }

    /**
     * Decodes a URL-safe Base64 string to a byte array.
     *
     * @param base64url The URL-safe Base64 string to decode.
     * @return The decoded byte array.
     */
    private static byte[] decodeBase64URL(String base64url) {
        return Base64.decodeUrl(base64url);
    }

    /**
     * Gets the Multibase enum type corresponding to the given prefix string.
     *
     * @param value The prefix string representing the Multibase type.
     * @return The corresponding Multibase enum type, or null if not found.
     */
    private static MultibaseType.MULTIBASE_TYPE getMultibaseEnum(String value) {
        for (MultibaseType.MULTIBASE_TYPE type : MultibaseType.MULTIBASE_TYPE.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
