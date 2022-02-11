package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationConverter converter;

    protected AuthenticationInterceptor(CustomUserDetailsService userDetailsService, AuthenticationConverter converter) {
        this.userDetailsService = userDetailsService;
        this.converter = converter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return false;
    }

    public abstract void afterAuthentication(Authentication authentication);

    public Authentication authenticate(AuthenticationToken token) {
        return null;
    }
}
