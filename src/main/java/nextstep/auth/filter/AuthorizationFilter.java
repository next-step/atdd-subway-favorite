package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = strategy.getToken(request);

        if (token == null || token.isBlank()) {
            return true;
        }

        if (!strategy.validToken(token)) {
            throw new AuthenticationException();
        }

        Authentication user = strategy.getAuthentication(token);

        if (!strategy.validUser(user)) {
            throw new AuthenticationException();
        }

        Authentication authentication = strategy.getAuthentication(user);

        setSecurityContext(authentication);
        return true;
    }

    private void setSecurityContext(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
