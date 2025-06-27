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

public class RoleType {

    public enum ROLE_TYPE implements StringEnum {
        TAS("Tas"),
        WALLET("Wallet"),
        ISSUER("Issuer"),
        VERIFIER("Verifier"),
        WALLET_PROVIDER("WalletProvider"),
        APP_PROVIDER("AppProvider"),
        LIST_PROVIDER("ListProvider"),
        OP_PROVIDER("OpProvider"),
        KYC_PROVIDER("KycProvider"),
        NOTIFICATION_PROVIDER("NotificationProvider"),
        LOG_PROVIDER("LogProvider"),
        PORTAL_PROVIDER("PortalProvider"),
        DELEGATION_PROVIDER("DelegationProvider"),
        STORAGE_PROVIDER("StorageProvider"),
        BACKUP_PROVIDER("BackupProvider"),
        ETC("Etc");


        final private String value;
        ROLE_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ROLE_TYPE fromValue(int value) {
            for (ROLE_TYPE type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case TAS:
                    return "Tas";
                case WALLET:
                    return "Wallet";
                case ISSUER:
                    return "Issuer";
                case VERIFIER:
                    return "Verifier";
                case WALLET_PROVIDER:
                    return "WalletProvider";
                case APP_PROVIDER:
                    return "AppProvider";
                case LIST_PROVIDER:
                    return "ListProvider";
                case OP_PROVIDER:
                    return "OpProvider";
                case KYC_PROVIDER:
                    return "KycProvider";
                case NOTIFICATION_PROVIDER:
                    return "NotificationProvider";
                case LOG_PROVIDER:
                    return "LogProvider";
                case PORTAL_PROVIDER:
                    return "PortalProvider";
                case DELEGATION_PROVIDER:
                    return "DelegationProvider";
                case STORAGE_PROVIDER:
                    return "StorageProvider";
                case BACKUP_PROVIDER:
                    return "BackupProvider";
                case ETC:
                    return "Etc";
                default:
                    return "Unknown";
            }
        }
    }
}
