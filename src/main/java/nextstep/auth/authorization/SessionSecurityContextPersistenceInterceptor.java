package nextstep.auth.authorization;

import static nextstep.auth.context.SecurityContextHolder.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
    }
}
