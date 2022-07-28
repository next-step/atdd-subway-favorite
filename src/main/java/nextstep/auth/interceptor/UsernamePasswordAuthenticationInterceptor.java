package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthMemberLoader;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.converter.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationInterceptor extends AuthenticationInterceptor {
    public UsernamePasswordAuthenticationInterceptor(AuthenticationConverter authenticationConverter, AuthMemberLoader authMemberLoader) {
        super(authenticationConverter, authMemberLoader);
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
