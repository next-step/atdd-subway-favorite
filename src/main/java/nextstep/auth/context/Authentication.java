package nextstep.auth.context;

import java.io.Serializable;
import java.util.List;

public class Authentication implements Serializable {
    private Object principal;
    private Object credential;
    private List<String> authorities;

    public Authentication(Object principal, List<String> authorities) {
        this.principal = principal;
        this.authorities = authorities;
    }

    public Authentication(Object principal, Object credential) {
        this.principal = principal;
        this.credential = credential;
    }

    public Object getPrincipal() {
        return principal;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public Object getCredential() {
        return credential;
    }
}
