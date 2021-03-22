package nextstep.subway.auth.domain;

public class AuthenticationToken {

    private final String principal;
    private final String credentials;

    public AuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }
}
