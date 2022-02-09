package nextstep.auth.unit;

import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmptyAuthenticationInterceptor extends AuthenticationInterceptor {
    public EmptyAuthenticationInterceptor(CustomUserDetailsService customUserDetailsService) {
        super(customUserDetailsService, null);
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    }
}
