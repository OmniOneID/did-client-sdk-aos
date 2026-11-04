/*
 * Copyright 2024-2026 OmniOne.
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

package org.omnione.did.sdk.communication.urlconnection;

import android.content.res.Resources;

import org.omnione.did.sdk.communication.exception.CommunicationErrorCode;
import org.omnione.did.sdk.communication.exception.CommunicationException;
import org.omnione.did.sdk.wallet.walletservice.logger.WalletLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class HttpUrlConnectionTask {
    public HttpUrlConnectionTask(){

    }
    public String makeHttpRequest(String urlString, String method, String payload, String accessToken) throws CommunicationException {
        return makeHttpRequest(urlString, method, payload, accessToken, "application/json");
    }

    public String makeHttpRequest(String urlString, String method, String payload, String accessToken, String contentType) throws CommunicationException {
        WalletLogger.getInstance().d("request : " + payload + " / " + urlString + " / [" + method + "]");
        if(urlString.isEmpty()){
            throw new CommunicationException(CommunicationErrorCode.ERR_CODE_COMMUNICATION_INVALID_PARAMETER, "urlString");
        }
        if(method.isEmpty()){
            throw new CommunicationException(CommunicationErrorCode.ERR_CODE_COMMUNICATION_INVALID_PARAMETER, "method");
        }
//        if(payload.isEmpty() && method.equals("POST")){
//            throw new CommunicationException(CommunicationErrorCode.ERR_CODE_COMMUNICATION_INVALID_PARAMETER, "payload");
//        }
        HttpURLConnection urlConnection = null;
        int responseCode = 0;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            if (accessToken != null) {
                urlConnection.setRequestProperty("Authorization", "Bearer " +accessToken);
            }
            urlConnection.setRequestProperty("Content-Type", contentType);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Accept-Language", getSystemLanguageTag());

            if ("POST".equalsIgnoreCase(method)) {
                urlConnection.setDoOutput(true);
                try (OutputStream os = urlConnection.getOutputStream()) {
                    if (payload != null) {
                        byte[] input = payload.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                }
            }
            urlConnection.connect();
            responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response;
                try (InputStream stream = urlConnection.getInputStream()) {
                    response = readAll(stream);
                }
                WalletLogger.getInstance().d("response : " + response + " / " + responseCode);
                return response;
            } else if( responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                WalletLogger.getInstance().d("response : unauthorized " + " / " + responseCode);
                throw new CommunicationException(CommunicationErrorCode.ERR_CODE_COMMUNICATION_UNAUTHORIZED , accessToken);

            } else if( responseCode == HttpURLConnection.HTTP_BAD_REQUEST
                    || responseCode == HttpURLConnection.HTTP_SERVER_ERROR) {
                String error;
                try (InputStream stream = urlConnection.getErrorStream()) {
                    error = stream == null ? "" : readAll(stream);
                }
                WalletLogger.getInstance().d("response : " + error + " / " + responseCode);
                throw new CommunicationException(CommunicationErrorCode.ERR_CODE_COMMUNICATION_SERVER_FAIL , error);
            } else {
                String error;
                try (InputStream stream = urlConnection.getInputStream()) {
                    error = readAll(stream);
                }
                WalletLogger.getInstance().d("response : " + error + " / " + responseCode);
                throw new CommunicationException(CommunicationErrorCode.ERR_CODE_COMMUNICATION_INCORRECT_URL_CONNECTION , urlString);
            }
        } catch (IOException e) {
            throw new CommunicationException(CommunicationErrorCode.ERR_CODE_COMMUNICATION_INCORRECT_URL_CONNECTION , e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private static String readAll(InputStream stream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(stream);
             BufferedReader in = new BufferedReader(reader)) {
            StringBuilder body = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                body.append(inputLine);
            }
            return body.toString();
        }
    }

    private static String getSystemLanguageTag() {
        Locale locale = Resources.getSystem()
                .getConfiguration()
                .getLocales()
                .get(0);

        String langTag = locale.toLanguageTag();
        WalletLogger.getInstance().d("langTag : " + langTag);
        return langTag;
    }
}

