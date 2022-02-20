package nextstep.auth.model.authorization.interceptor;

import nextstep.auth.model.context.SecurityContext;

import javax.servlet.http.HttpServletRequest;

import static nextstep.auth.model.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionSecurityContextPersistenceInterceptor extends AbstractSecurityContextPersistenceInterceptor {
    @Override
    protected SecurityContext extractSecurityContext(HttpServletRequest request) {
        return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }
}
