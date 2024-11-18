/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.wallet.walletservice.db;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static SharedPreferences getPreference(Context context){
        return context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
    }

    public static void saveWalletId(Context context, String walletId) {
        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("walletId", walletId);
        editor.apply();
    }
    public static String loadWalletId(Context context) {
        SharedPreferences prefs = getPreference(context);
        return prefs.getString("walletId", "");
    }

    public static void savePii(Context context, String pii) {
        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pii", pii);
        editor.apply();
    }
    public static String loadPii(Context context) {
        SharedPreferences prefs = getPreference(context);
        return prefs.getString("pii", "");
    }
}
