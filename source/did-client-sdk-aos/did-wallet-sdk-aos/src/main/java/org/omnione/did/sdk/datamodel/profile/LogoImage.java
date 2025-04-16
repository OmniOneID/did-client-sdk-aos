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

package org.omnione.did.sdk.datamodel.profile;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.omnione.did.sdk.datamodel.util.StringEnum;

public class LogoImage {
    @SerializedName("format")
    @Expose
    private LOGO_IMAGE_TYPE format;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("value")
    @Expose
    private String value;

    public LOGO_IMAGE_TYPE getFormat() {
        return format;
    }

    public void setFormat(LOGO_IMAGE_TYPE format) {
        this.format = format;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void fromJson(String val) {
        Gson gson = new Gson();
        LogoImage obj = gson.fromJson(val, LogoImage.class);

        this.format = obj.getFormat();
        this.link = obj.getLink();
        this.value = obj.getValue();
    }

    public enum LOGO_IMAGE_TYPE implements StringEnum {
        jpg("jpg"),
        png("png");

        final private String value;
        LOGO_IMAGE_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static LOGO_IMAGE_TYPE fromValue(String value) {
            for (LOGO_IMAGE_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case jpg:
                    return "jpg";
                case png:
                    return "png";
                default:
                    return "Unknown";
            }
        }
    }
}
