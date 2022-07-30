package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationStrategy;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChainedInterceptor implements HandlerInterceptor {

    private AuthenticationStrategy strategy;

    public ChainedInterceptor(AuthenticationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            strategy.authenticate(request, response, handler);
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
