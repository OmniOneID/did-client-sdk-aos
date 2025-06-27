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

package org.omnione.did.sdk.datamodel.vc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.common.enums.ClaimFormat;
import org.omnione.did.sdk.datamodel.common.enums.ClaimType;
import org.omnione.did.sdk.datamodel.common.enums.Location;

import java.util.Map;

public class Claim {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("type")
    @Expose
    private ClaimType.CLAIM_TYPE type;

    @SerializedName("format")
    @Expose
    private ClaimFormat.CLAIM_FORMAT format;

    @SerializedName("hideValue")
    @Expose
    private boolean hideValue;

    @SerializedName("location")
    @Expose
    private Location.LOCATION location;

    @SerializedName("digestSRI")
    @Expose
    private String digestSRI;

    @SerializedName("i18n")
    @Expose
    private Map<String, Internationalization> i18n;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ClaimType.CLAIM_TYPE getType() {
        return type;
    }

    public void setType(ClaimType.CLAIM_TYPE type) {
        this.type = type;
    }

    public ClaimFormat.CLAIM_FORMAT getFormat() {
        return format;
    }

    public void setFormat(ClaimFormat.CLAIM_FORMAT format) {
        this.format = format;
    }

    public boolean isHideValue() {
        return hideValue;
    }

    public void setHideValue(boolean hideValue) {
        this.hideValue = hideValue;
    }

    public Location.LOCATION getLocation() {
        return location;
    }

    public void setLocation(Location.LOCATION location) {
        this.location = location;
    }

    public String getDigestSRI() {
        return digestSRI;
    }

    public void setDigestSRI(String digestSRI) {
        this.digestSRI = digestSRI;
    }

    public Map<String, Internationalization> getI18n() {
        return i18n;
    }

    public void setI18n(Map<String, Internationalization> i18n) {
        this.i18n = i18n;
    }

    public static class Internationalization {
        private String caption;
        private String value;
        private String digestSRI;

    }
}
