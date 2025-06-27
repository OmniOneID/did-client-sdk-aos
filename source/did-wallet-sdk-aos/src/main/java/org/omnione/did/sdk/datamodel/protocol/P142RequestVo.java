package org.omnione.did.sdk.datamodel.protocol;

import org.omnione.did.sdk.datamodel.security.DIDAuth;
import org.omnione.did.sdk.datamodel.security.ReqEcdh;
import org.omnione.did.sdk.datamodel.token.ServerTokenSeed;

public class P142RequestVo extends BaseRequestVo {
    private String offerId;
    private String did;
    private ReqEcdh reqEcdh;
    private ServerTokenSeed seed;
    private String serverToken;
    private DIDAuth didAuth;
    public P142RequestVo(String id) {
        super(id);
    }
    public P142RequestVo(String id, String txId) {
        super(id, txId);
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public ReqEcdh getReqEcdh() {
        return reqEcdh;
    }

    public void setReqEcdh(ReqEcdh reqEcdh) {
        this.reqEcdh = reqEcdh;
    }

    public ServerTokenSeed getSeed() {
        return seed;
    }

    public void setSeed(ServerTokenSeed seed) {
        this.seed = seed;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public DIDAuth getDidAuth() {
        return didAuth;
    }

    public void setDidAuth(DIDAuth didAuth) {
        this.didAuth = didAuth;
    }
}
