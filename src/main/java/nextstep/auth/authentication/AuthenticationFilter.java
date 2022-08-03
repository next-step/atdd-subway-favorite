package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            return authenticate(request);
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract boolean authenticate(HttpServletRequest request);
}
