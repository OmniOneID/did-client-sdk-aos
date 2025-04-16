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

import java.math.BigInteger;

public class ZkpConstants {

    public static final String MASTER_SECRET_KEY = "masterSecret";

    public static final String HASH_ALG = "SHA-256";

    public static final String DELTA = "DELTA";

    public static final int ITERATION = 4;

    public static final int LARGE_PRIME = 1024;
    public static final int LARGE_NONCE = 80;
    public static final int LARGE_MASTER_SECRET = 256;

    public static final int LARGE_VPRIME = (LARGE_PRIME * 2) + LARGE_NONCE;
    public static final int LARGE_VPRIME_VPRIME = 2724;

    //TODO: 표준과 같이 다름 (재검토 필요)
    public static final int LARGE_VPRIME_TILDE = 673; //2465가 표준

    public final static int LARGE_E_START = 596;
    public final static int LARGE_E_END_RANGE = 119;
    public final static int LARGE_E_MAX_BITS = LARGE_E_START + LARGE_E_END_RANGE;

    public final static BigInteger LARGE_E_START_VALUE = BigInteger.ONE.shiftLeft(LARGE_E_START);

    //TODO: 이 아래부분은 표준과 같이 다름을 검증하지 못한 코드
    //TODO: 표준과 값이 다름 재고려 필요
    public final static int LARGE_MTILDE = 593;
    public final static int LARGE_ETILDE = 456;
    public final static int LARGE_VTILDE = 3060;
    public final static int LARGE_MVECT = 592;
    public final static int LARGE_UTILDE = 592;
    public final static int LARGE_RTILDE = 672;
    public final static int LARGE_ALPHATILDE = 2787;
}
