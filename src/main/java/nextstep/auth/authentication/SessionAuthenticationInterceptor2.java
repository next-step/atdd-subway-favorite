package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;

public class SessionAuthenticationInterceptor2 extends AuthenticationInterceptor {

    public SessionAuthenticationInterceptor2(CustomUserDetailsService userDetailsService,
                                             AuthenticationConverter converter) {
        super(userDetailsService, converter);
    }

    @Override
    public void afterAuthentication(Authentication authentication) {

    }

}
