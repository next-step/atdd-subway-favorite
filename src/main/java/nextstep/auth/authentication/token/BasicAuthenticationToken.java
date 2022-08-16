package nextstep.auth.authentication.token;

import nextstep.auth.context.Authentication;

import java.util.List;

public class BasicAuthenticationToken implements Authentication {
    private Object principal;

    private Object credentials;

    private List<String> authorities;

    private boolean isAuthenticated;

    public BasicAuthenticationToken(Object principal, Object credentials) {
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public BasicAuthenticationToken(Object principal, Object credentials, List<String> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
        setAuthenticated(true);
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}
