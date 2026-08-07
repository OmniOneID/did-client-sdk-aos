/*
 * Copyright 2026 OmniOne.
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

package org.omnione.did.sdk.datamodel.oid4vc.dcql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CredentialSetQuery {

    @SerializedName("options")
    @Expose
    private List<List<String>> options;

    @SerializedName("required")
    @Expose
    private Boolean required;

    @SerializedName("purpose")
    @Expose
    private Object purpose;

    public List<List<String>> getOptions() {
        return options;
    }

    public void setOptions(List<List<String>> options) {
        this.options = options;
    }

    public boolean isRequired() {
        return required == null || required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Object getPurpose() {
        return purpose;
    }

    public void setPurpose(Object purpose) {
        this.purpose = purpose;
    }
}
