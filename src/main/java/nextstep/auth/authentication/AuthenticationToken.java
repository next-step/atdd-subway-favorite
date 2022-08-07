package nextstep.auth.authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AuthenticationToken {
    private String principal;
    private String credentials;

    public AuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }
}
