package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationStrategy;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UnchainedAuthenticationInterceptor implements HandlerInterceptor {

    private AuthenticationStrategy strategy;

    public UnchainedAuthenticationInterceptor(AuthenticationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        strategy.authenticate(request, response, handler);
        return false;
    }
}
