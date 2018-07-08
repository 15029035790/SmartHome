package android.smart.home.smarthome.entity;

/**
 * Created by administrator on 2017/12/14.
 */

public class AccessToken {

    /**
     * access_token : 219e53e4fea824e7cc86
     * expires_in : 3600
     * token_type : Bearer
     * scope : null
     * refresh_token : 8107bce9803527c448
     */

    private String access_token;
    private Long expires_in;
    private String token_type;
    private Object scope;
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Object getScope() {
        return scope;
    }

    public void setScope(Object scope) {
        this.scope = scope;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
