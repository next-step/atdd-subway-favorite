package nextstep.subway.auth.ui.session;

import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.auth.ui.SecurityContextInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

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
