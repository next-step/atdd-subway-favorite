package nextstep.auth.filter;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements HandlerInterceptor {

    private final AuthorizationStrategy strategy;

    public AuthorizationFilter(AuthorizationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = strategy.getToken(request);

        if (token == null || token.isBlank()) {
            return true;
        }

        Authentication authentication = strategy.extractAuthentication(token);

        setSecurityContext(authentication);
        return true;
    }

    private void setSecurityContext(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
