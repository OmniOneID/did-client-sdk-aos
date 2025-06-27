package org.omnione.did.sdk.datamodel.vc.issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.intellij.lang.annotations.Pattern;
import org.omnione.did.sdk.datamodel.common.BaseObject;
import org.omnione.did.sdk.datamodel.token.Provider;
import org.omnione.did.sdk.datamodel.util.GsonWrapper;
import org.omnione.did.sdk.datamodel.vc.CredentialSchema;

public class VcMeta extends BaseObject {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("issuer")
    @Expose
    private Provider issuer;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("credentialSchema")
    @Expose
    private CredentialSchema credentialSchema;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("issuanceDate")
    @Expose
    private String issuanceDate;
    @SerializedName("validFrom")
    @Expose
    private String validFrom;
    @SerializedName("validUntil")
    @Expose
    private String validUntil;
    @SerializedName("formatVersion")
    @Expose
    private String formatVersion;
    @SerializedName("language")
    @Expose
    private String language;

    public void fromJson(String val) {
        GsonWrapper gson = new GsonWrapper();
        VcMeta data = (VcMeta)gson.fromJson(val, VcMeta.class);
        this.id = data.id;
        this.issuer = data.issuer;
        this.subject = data.subject;
        this.credentialSchema = data.credentialSchema;
        this.status = data.status;
        this.issuanceDate = data.issuanceDate;
        this.validFrom = data.validFrom;
        this.validUntil = data.validUntil;
        this.formatVersion = data.formatVersion;
        this.language = data.language;
    }

    public String getId() {
        return this.id;
    }

    public Provider getIssuer() {
        return this.issuer;
    }

    public String getSubject() {
        return this.subject;
    }

    public CredentialSchema getCredentialSchema() {
        return this.credentialSchema;
    }

    public String getStatus() {
        return this.status;
    }

    public String getIssuanceDate() {
        return this.issuanceDate;
    }

    public String getValidFrom() {
        return this.validFrom;
    }

    public String getValidUntil() {
        return this.validUntil;
    }

    public String getFormatVersion() {
        return this.formatVersion;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIssuer(Provider issuer) {
        this.issuer = issuer;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCredentialSchema(CredentialSchema credentialSchema) {
        this.credentialSchema = credentialSchema;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
