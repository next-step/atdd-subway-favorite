package nextstep.auth.interceptor;

import nextstep.auth.authorization.AuthorizationStrategy;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChainedAuthorizationInterceptor implements HandlerInterceptor {

    private AuthorizationStrategy strategy;

    public ChainedAuthorizationInterceptor(AuthorizationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            strategy.authorize(request);
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
