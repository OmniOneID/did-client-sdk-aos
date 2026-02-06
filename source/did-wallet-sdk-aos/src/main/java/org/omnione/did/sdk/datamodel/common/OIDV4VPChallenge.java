package org.omnione.did.sdk.datamodel.common;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OIDV4VPChallenge extends BaseObject {
    @SerializedName("domain")
    @Expose
    String domain;
    @SerializedName("challenge")
    @Expose
    String challenge;

    public OIDV4VPChallenge(String domain, String challenge) {
        this.domain = domain;
        this.challenge = challenge;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    @Override
    public void fromJson(String val) {
        Gson gson = new Gson();
        OIDV4VPChallenge obj = gson.fromJson(val, OIDV4VPChallenge.class);
        domain = obj.getDomain();
        challenge = obj.getChallenge();
    }
}
