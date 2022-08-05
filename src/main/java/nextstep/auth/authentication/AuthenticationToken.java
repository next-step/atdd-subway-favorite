package nextstep.auth.authentication;

import org.springframework.util.StringUtils;

public class AuthenticationToken {
    private String principal;
    private String credentials;

    public AuthenticationToken(String principal, String credentials) {
        if (hasNotTest(principal) || hasNotTest(credentials)) {
            throw new IllegalArgumentException();
        }
        this.principal = principal;
        this.credentials = credentials;
    }

    private boolean hasNotTest(String value) {
        return !StringUtils.hasText(value);
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }
}
