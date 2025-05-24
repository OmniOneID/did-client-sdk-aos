package org.omnione.did.sdk.datamodel.zkp.ladger;


import java.io.Serializable;

public class ZkpLadgerRequestData implements Serializable {

    private String table;
    private String scope;
    private String code;
    private int index_position;
    private String key_type;
    private String lower_bound;
    private boolean json;
//    private int limit;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isJson() {
        return json;
    }

    public void setJson(boolean json) {
        this.json = json;
    }

//    public int getLimit() {
//        return limit;
//    }
//
//    public void setLimit(int limit) {
//        this.limit = limit;
//    }

    public int getIndex_position() {
        return index_position;
    }

    public void setIndex_position(int index_position) {
        this.index_position = index_position;
    }

    public String getKey_type() {
        return key_type;
    }

    public void setKey_type(String key_type) {
        this.key_type = key_type;
    }

    public String getLower_bound() {
        return lower_bound;
    }

    public void setLower_bound(String lower_bound) {
        this.lower_bound = lower_bound;
    }
}
