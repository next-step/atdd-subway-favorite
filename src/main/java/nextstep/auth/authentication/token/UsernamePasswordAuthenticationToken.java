package nextstep.auth.authentication.token;

import nextstep.auth.context.Authentication;

import java.util.List;

public class UsernamePasswordAuthenticationToken implements Authentication {
    // 주로 사용자의 ID에 해당함
    private Object principal;

    // 주로 사용자의 PW에 해당함
    private Object credentials;

    // 권한
    private List<String> authorities;

    // 인증 완료 여부
    private boolean isAuthenticated;

    // 인증 완료 전의 객체 생성
    public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    // 인증 완료 후의 객체 생성
    public UsernamePasswordAuthenticationToken(Object principal, Object credentials, List<String> authorities) {
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
