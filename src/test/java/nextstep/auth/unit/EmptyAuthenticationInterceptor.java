package nextstep.auth.unit;

import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmptyAuthenticationInterceptor extends AuthenticationInterceptor {
    public EmptyAuthenticationInterceptor(UserDetailsService userDetailsService) {
        super(userDetailsService, null);
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    }
}
