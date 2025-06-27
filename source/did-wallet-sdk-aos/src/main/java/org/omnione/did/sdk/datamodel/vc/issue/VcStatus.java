package org.omnione.did.sdk.datamodel.vc.issue;

import org.omnione.did.sdk.core.storagemanager.datamodel.FileExtension;

public enum VcStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    REVOKED("REVOKED");

    private String value;

    VcStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}