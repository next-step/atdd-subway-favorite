package nextstep.auth.authentication;

import nextstep.auth.token.TokenRequest;

public class AuthenticationToken {
    private String principal;
    private String credentials;

    public AuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public static AuthenticationToken of(TokenRequest tokenRequest) {
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }
}
