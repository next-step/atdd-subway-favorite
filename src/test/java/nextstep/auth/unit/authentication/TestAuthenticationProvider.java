package nextstep.auth.unit.authentication;

import nextstep.auth.authentication.AuthenticationProvider;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;

public class TestAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(AuthenticationToken authenticationToken) {
        return new Authentication(authenticationToken.getPrincipal());
    }

    @Override
    public boolean supports(Class<?> authenticationToken) {
        return TestAuthenticationToken.class.isAssignableFrom(authenticationToken);
    }
}
