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

package org.omnione.did.sdk.wallet.walletservice.logger;

import android.util.Log;

public class WalletLogger {

    private static WalletLogger instance = null;
    private boolean isEnabled;

    private WalletLogger() {
        isEnabled = false;
    }

    public static WalletLogger getInstance() {
        if (instance == null) {
            instance = new WalletLogger();
        }
        return instance;
    }

    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }

    private static String lineOut() {
        int level = 4;
        StackTraceElement[] traces;
        traces = Thread.currentThread().getStackTrace();
        return ("at " + traces[level] + " ");
    }

    private String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuffer sb = new StringBuffer();
        if(ste.getFileName() != null) {
            sb.append(" [ ");
            sb.append(ste.getFileName().replace(".java", ""));
            sb.append(" :: ");
            sb.append(ste.getMethodName());
            sb.append(" ] ");
            sb.append(message);
        }
        return sb.toString();
    }

    public void d(String logMsg) {
        if(isEnabled) {
            Log.d("WALLET_LOG", logMsg);
        }
    }
    public void e(String logMsg) {
        if(isEnabled)
            Log.e("WALLET_LOG", buildLogMsg(logMsg + " :: " + lineOut()));
    }
    public void v(String logMsg) {
        if(isEnabled)
            Log.v("WALLET_LOG", buildLogMsg(logMsg + " :: " + lineOut()));
    }
    public void i(String logMsg) {
        if(isEnabled)
            Log.i("WALLET_LOG", buildLogMsg(logMsg + " :: " + lineOut()));
    }
}
