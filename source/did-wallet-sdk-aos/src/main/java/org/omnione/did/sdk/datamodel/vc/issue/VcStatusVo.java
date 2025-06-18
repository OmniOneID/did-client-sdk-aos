package org.omnione.did.sdk.datamodel.vc.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VcStatusVo {
    @SerializedName("vcId")
    @Expose
    private String vcId;

    @SerializedName("vcMeta")
    @Expose
    private String vcMeta;

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public String getVcMeta() {
        return vcMeta;
    }

    public void setVcMeta(String vcMeta) {
        this.vcMeta = vcMeta;
    }
}
