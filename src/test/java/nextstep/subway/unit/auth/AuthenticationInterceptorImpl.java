package nextstep.subway.unit.auth;

import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.member.application.CustomUserDetailsService;

public class AuthenticationInterceptorImpl extends AuthenticationInterceptor {
    protected AuthenticationInterceptorImpl(CustomUserDetailsService userDetailsService) {
        super(userDetailsService);
    }
}
