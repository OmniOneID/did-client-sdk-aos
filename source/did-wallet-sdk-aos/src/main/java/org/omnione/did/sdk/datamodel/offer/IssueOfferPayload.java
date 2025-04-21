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

package org.omnione.did.sdk.datamodel.offer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.SortData;

public class IssueOfferPayload extends SortData {
    @SerializedName("offerId")
    @Expose
    private String offerId;

    @SerializedName("type")
    @Expose
    private OFFER_TYPE type;
    @SerializedName("vcPlanId")
    @Expose
    private String vcPlanId;
    @SerializedName("issuer")
    @Expose
    private String issuer;
    @SerializedName("validUntil")
    @Expose
    private String validUntil;

    public enum OFFER_TYPE {
        IssueOffer("IssueOffer"),
        VerifyOffer("VerifyOffer");

        final private String value;
        OFFER_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static OFFER_TYPE fromValue(String value) {
            for (OFFER_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public OFFER_TYPE getType() {
        return type;
    }

    public void setType(OFFER_TYPE type) {
        this.type = type;
    }

    public String getVcPlanId() {
        return vcPlanId;
    }

    public void setVcPlanId(String vcPlanId) {
        this.vcPlanId = vcPlanId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }
}
