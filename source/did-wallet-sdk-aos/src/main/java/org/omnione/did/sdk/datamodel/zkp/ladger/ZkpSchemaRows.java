package org.omnione.did.sdk.datamodel.zkp.ladger;

import java.io.Serializable;

public class ZkpSchemaRows extends ZkpTableRows implements Serializable {

    private String schema_id;
    private String schema_id_hash;
    private String schema_desc;
    private String schema_value;

    public String getSchema_id() {
        return schema_id;
    }

    public void setSchema_id(String schema_id) {
        this.schema_id = schema_id;
    }

    public String getSchema_id_hash() {
        return schema_id_hash;
    }

    public void setSchema_id_hash(String schema_id_hash) {
        this.schema_id_hash = schema_id_hash;
    }

    public String getSchema_desc() {
        return schema_desc;
    }

    public void setSchema_desc(String schema_desc) {
        this.schema_desc = schema_desc;
    }

    public String getSchema_value() {
        return schema_value;
    }

    public void setSchema_value(String schema_value) {
        this.schema_value = schema_value;
    }
}
