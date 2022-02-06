package nextstep.auth.ui.authorization;

import nextstep.auth.infrastructure.SecurityContext;

import javax.servlet.http.HttpServletRequest;

import static nextstep.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor{

    @Override
    protected SecurityContext extractSecurityContext(HttpServletRequest request) {
        SecurityContext context = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        return context;
    }
}
