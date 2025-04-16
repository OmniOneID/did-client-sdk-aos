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

package org.omnione.did.sdk.datamodel.protocol;


import org.omnione.did.sdk.datamodel.security.AccEcdh;

public class P132ResponseVo extends BaseResponseVo {
    private String iv;
    private String encStd;
    private AccEcdh accEcdh;

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEncStd() {
        return encStd;
    }

    public void setEncStd(String encStd) {
        this.encStd = encStd;
    }

    public AccEcdh getAccEcdh() {
        return accEcdh;
    }

    public void setAccEcdh(AccEcdh accEcdh) {
        this.accEcdh = accEcdh;
    }


}
