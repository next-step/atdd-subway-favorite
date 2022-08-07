package nextstep.auth.authentication;

import org.springframework.util.StringUtils;

public class AuthenticationToken {
    private String principal;
    private String credentials;

    public AuthenticationToken(String principal, String credentials) {
        if (hasNotText(principal) || hasNotText(credentials)) {
            throw new IllegalArgumentException();
        }
        this.principal = principal;
        this.credentials = credentials;
    }

    private boolean hasNotText(String value) {
        return !StringUtils.hasText(value);
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }
}
