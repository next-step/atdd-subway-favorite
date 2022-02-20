package nextstep.auth.authorization;

import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractSecurityContextInterceptor implements HandlerInterceptor {

    protected abstract SecurityContext extractSecurityContext(HttpServletRequest request);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        SecurityContext securityContext = extractSecurityContext(request);
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.clearContext();
    }
}
