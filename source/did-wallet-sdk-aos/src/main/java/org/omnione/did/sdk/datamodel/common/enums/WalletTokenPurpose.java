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

import org.omnione.did.sdk.datamodel.util.IntEnum;

public class WalletTokenPurpose {

    public enum WALLET_TOKEN_PURPOSE implements IntEnum {
        PERSONALIZE(1),
        DEPERSONALIZE(2),
        PERSONALIZE_AND_CONFIGLOCK(3),
        CONFIGLOCK(4),
        CREATE_DID(5),
        UPDATE_DID(6),
        RESTORE_DID(7),
        ISSUE_VC(8),
        REMOVE_VC(9),
        PRESENT_VP(10),
        LIST_VC(11),
        DETAIL_VC(12),
        CREATE_DID_AND_ISSUE_VC(13),
        LIST_VC_AND_PRESENT_VP(14);

        final private int value;
        WALLET_TOKEN_PURPOSE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static WALLET_TOKEN_PURPOSE fromValue(int value) {
            for (WALLET_TOKEN_PURPOSE type : values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case PERSONALIZE:
                    return "Personalize";
                case DEPERSONALIZE:
                    return "Depersonalize";
                case PERSONALIZE_AND_CONFIGLOCK:
                    return "PersonalizeAndConfigLock";
                case CONFIGLOCK:
                    return "ConfigLock";
                case CREATE_DID:
                    return "CreateDID";
                case UPDATE_DID:
                    return "UpdateDID";
                case RESTORE_DID:
                    return "RestoreDID";
                case ISSUE_VC:
                    return "IssueVc";
                case REMOVE_VC:
                    return "RemoveVc";
                case PRESENT_VP:
                    return "PresentVp";
                case LIST_VC:
                    return "ListVc";
                case DETAIL_VC:
                    return "DetailVc";
                case CREATE_DID_AND_ISSUE_VC:
                    return "CreateDIDAndIssueVc";
                case LIST_VC_AND_PRESENT_VP:
                    return "ListVCAndPresentVP";
                default:
                    return "Unknown";
            }
        }
    }
}
