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

package org.omnione.did.sdk.datamodel.common.enums;

import org.omnione.did.sdk.datamodel.util.StringEnum;

public class ClaimFormat {
    public enum CLAIM_FORMAT implements StringEnum {
        plain("plain"),
        html("html"),
        xml("xml"),
        csv("csv"),
        png("png"),
        jpg("jpg"),
        gif("gif"),
        txt("txt"),
        pdf("pdf"),
        word("word");

        final private String value;
        CLAIM_FORMAT(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static CLAIM_FORMAT fromValue(String value) {
            for (CLAIM_FORMAT type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case plain:
                    return "plain";
                case html:
                    return "html";
                case xml:
                    return "xml";
                case csv:
                    return "csv";
                case png:
                    return "png";
                case jpg:
                    return "jpg";
                case gif:
                    return "gif";
                case txt:
                    return "txt";
                case pdf:
                    return "pdf";
                case word:
                    return "word";
                default:
                    return "Unknown";
            }
        }
    }
}
