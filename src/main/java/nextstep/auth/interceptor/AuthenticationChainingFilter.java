package nextstep.auth.interceptor;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationChainingFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Authentication authentication = createAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract Authentication createAuthentication(HttpServletRequest request);
}
