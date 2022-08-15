package nextstep.auth.filter;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            SecurityContextHolder.getContext()
                    .setAuthentication(getAuth(request));
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract Authentication getAuth(HttpServletRequest request);

}
