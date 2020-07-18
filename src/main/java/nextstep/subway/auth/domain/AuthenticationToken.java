package nextstep.subway.auth.domain;

import nextstep.subway.auth.exception.AuthenticationException;
import org.springframework.util.StringUtils;

public class AuthenticationToken {
    private String principal;
    private String credentials;

    public AuthenticationToken(String principal, String credentials) {
        if (StringUtils.isEmpty(principal) || StringUtils.isEmpty(credentials)) {
            throw new AuthenticationException();
        }
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
