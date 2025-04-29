/*
 * Copyright 2025 OmniOne.
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

package org.omnione.did.sdk.core.zkp.other.util;

import java.security.SecureRandom;

public class RandomUtil {
    private static final Object cacheLock = new Object();
    private static SecureRandom defaultSecureRandom;

    private RandomUtil() { }

    public static SecureRandom getSecureRandom() {
        synchronized (cacheLock) {
            if (null != defaultSecureRandom) {
                return defaultSecureRandom;
            }
        }


        SecureRandom tmp = new SecureRandom();

//        SecureRandom tmp = null;
//        try {
//            tmp = SecureRandom.getInstance("SHA1PRNG", "SUN");
//            int entropy_bytes = 128;
//            byte[] seed = new byte[entropy_bytes];
//            tmp.setSeed(seed);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        }

        synchronized (cacheLock) {
            if (null == defaultSecureRandom) {
                defaultSecureRandom = tmp;
            }
            return defaultSecureRandom;
        }
    }

    public static void setSecureRandom(SecureRandom secureRandom) {
        synchronized (cacheLock) {
            defaultSecureRandom = secureRandom;
        }
    }
}