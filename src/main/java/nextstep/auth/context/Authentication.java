package nextstep.auth.context;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Authentication implements Serializable {
    private Object principal;
    private Object credentials;
    private List<String> authorities;

    public Authentication(Object principal, Object credentials, List<String> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
    }

    public Authentication(Object principal, List<String> authorities) {
        this(principal, null, authorities);
    }

    public Authentication(Object principal, Object credentials) {
        this(principal, credentials, Collections.emptyList());
    }

    public Authentication(Object principal) {
        this(principal, null, Collections.emptyList());
    }

    public Object getPrincipal() {
        return principal;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public Object getCredentials() {
        return credentials;
    }
}
