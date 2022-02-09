package nextstep.auth.unit.authentication;

import nextstep.auth.authentication.AuthenticationToken;

public class TestAuthenticationToken extends AuthenticationToken {
    public TestAuthenticationToken(String principal, String credentials) {
        super(principal, credentials);
    }
}
