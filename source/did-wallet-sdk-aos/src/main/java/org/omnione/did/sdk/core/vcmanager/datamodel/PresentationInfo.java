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

package org.omnione.did.sdk.core.vcmanager.datamodel;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.BaseObject;

public class PresentationInfo extends BaseObject {
    @SerializedName("holder")
    @Expose
    private String holder;

    @SerializedName("validFrom")
    @Expose
    private String validFrom;

    @SerializedName("validUntil")
    @Expose
    private String validUntil;

    @SerializedName("verifierNonce")
    @Expose
    private String verifierNonce;
    public PresentationInfo(){}

    public PresentationInfo(String holder, String validFrom, String validUntil, String verifierNonce) {
        this.holder = holder;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.verifierNonce = verifierNonce;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getVerifierNonce() {
        return verifierNonce;
    }

    public void setVerifierNonce(String verifierNonce) {
        this.verifierNonce = verifierNonce;
    }

    @Override
    public void fromJson(String val) {
        Gson gson = new Gson();
        PresentationInfo obj = gson.fromJson(val, PresentationInfo.class);

        holder = obj.getHolder();
        validFrom = obj.getValidFrom();
        validUntil = obj.getValidUntil();
        verifierNonce = obj.getVerifierNonce();

    }
}
