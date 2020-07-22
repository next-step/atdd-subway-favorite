package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.application.SecurityContextPersistenceHandler;
import nextstep.subway.auth.infrastructure.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionSecurityContextPersistenceInterceptor extends AbstractSecurityContextPersistenceInterceptor {
    public SessionSecurityContextPersistenceInterceptor(SecurityContextPersistenceHandler persistenceHandler) {
        super(persistenceHandler);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (securityContext != null) {
            persistenceHandler.persist(securityContext);
        }
        return true;
    }
}
