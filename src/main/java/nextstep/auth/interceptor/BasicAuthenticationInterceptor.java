package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthMemberLoader;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.converter.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasicAuthenticationInterceptor extends  AuthenticationInterceptor{
    public BasicAuthenticationInterceptor(AuthenticationConverter authenticationConverter, AuthMemberLoader authMemberLoader) {
        super(authenticationConverter, authMemberLoader);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        super.preHandle(request, response, handler);
        return false;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
