package org.omnione.did.sdk.datamodel.protocol;

import org.omnione.did.sdk.datamodel.security.AccEcdh;

public class P141ResponseVo extends BaseResponseVo {
    private String authNonce;
    private AccEcdh accEcdh;
    private String iv;
    private String encStd;

    public String getAuthNonce() {
        return authNonce;
    }

    public void setAuthNonce(String authNonce) {
        this.authNonce = authNonce;
    }

    public AccEcdh getAccEcdh() {
        return accEcdh;
    }

    public void setAccEcdh(AccEcdh accEcdh) {
        this.accEcdh = accEcdh;
    }

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
}
