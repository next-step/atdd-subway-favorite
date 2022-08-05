package nextstep.auth.authentication;

import lombok.Getter;

@Getter
public class AuthenticationToken {
    private String principal;
    private String credentials;

    public AuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }
}
