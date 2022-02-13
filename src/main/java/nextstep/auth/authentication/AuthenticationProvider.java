package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;

public interface AuthenticationProvider {
    Authentication authenticate(AuthenticationToken authenticationToken);

    boolean supports(Class<?> authenticationToken);
}
