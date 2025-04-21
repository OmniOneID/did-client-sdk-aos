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

package org.omnione.did.sdk.utility.DataModels;

import com.google.gson.Gson;

public class CipherInfo {
    private ENCRYPTION_TYPE type; //"AES
    private ENCRYPTION_MODE mode; //"CBC", "ECB"
    private SYMMETRIC_KEY_SIZE size; //"128", "256"
    private SYMMETRIC_PADDING_TYPE padding; //"NOPAD", "PKCS5"

    public CipherInfo(ENCRYPTION_TYPE type, ENCRYPTION_MODE mode, SYMMETRIC_KEY_SIZE size, SYMMETRIC_PADDING_TYPE padding){
        this.type = type;
        this.mode = mode;
        this.size = size;
        this.padding = padding;
    }

    public  CipherInfo(){

    }

    public ENCRYPTION_TYPE getType() {
        return type;
    }

    public void setType(ENCRYPTION_TYPE type) {
        this.type = type;
    }

    public ENCRYPTION_MODE getMode() {
        return mode;
    }

    public void setMode(ENCRYPTION_MODE mode) {
        this.mode = mode;
    }

    public SYMMETRIC_KEY_SIZE getSize() {
        return size;
    }

    public void setSize(SYMMETRIC_KEY_SIZE size) {
        this.size = size;
    }

    public SYMMETRIC_PADDING_TYPE getPadding() {
        return padding;
    }

    public void setPadding(SYMMETRIC_PADDING_TYPE padding) {
        this.padding = padding;
    }

    public enum ENCRYPTION_TYPE {
        AES("AES");

        final private String value;
        ENCRYPTION_TYPE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static ENCRYPTION_TYPE fromValue(String value) {
            for (ENCRYPTION_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case AES:
                    return "AES";
                default:
                    return "Unknown";
            }
        }
    }
    public enum ENCRYPTION_MODE {
        CBC("CBC"),
        ECB("ECB");

        final private String value;
        ENCRYPTION_MODE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static ENCRYPTION_MODE fromValue(String value) {
            for (ENCRYPTION_MODE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case CBC:
                    return "CBC";
                case ECB:
                    return "ECB";
                default:
                    return "Unknown";
            }
        }
    }

    public enum SYMMETRIC_KEY_SIZE {
        AES_128("128"),
        AES_256("256");

        final private String value;
        SYMMETRIC_KEY_SIZE(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static SYMMETRIC_KEY_SIZE fromValue(String value) {
            for (SYMMETRIC_KEY_SIZE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case AES_128:
                    return "128";
                case AES_256:
                    return "256";
                default:
                    return "Unknown";
            }
        }
    }

    public enum SYMMETRIC_PADDING_TYPE {
        NOPAD("NOPAD","NOPAD"),
        PKCS5("PKCS5","PKCS5Padding");

        final private String value;
        final private String key;
        SYMMETRIC_PADDING_TYPE(String key, String value) {
            this.key = key;
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        public String getKey() {
            return key;
        }

        public static SYMMETRIC_PADDING_TYPE fromKey(String key) {
            for (SYMMETRIC_PADDING_TYPE type : values()) {
                if (type.getKey().equals(key)) {
                    return type;
                }
            }
            return null;
        }

        public static SYMMETRIC_PADDING_TYPE fromValue(String value) {
            for (SYMMETRIC_PADDING_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case NOPAD:
                    return "NOPAD";
                case PKCS5:
                    return "PKCS5Padding";
                default:
                    return "Unknown";
            }
        }
    }
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
