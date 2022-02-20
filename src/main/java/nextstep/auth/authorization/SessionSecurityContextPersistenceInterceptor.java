package nextstep.auth.authorization;

import nextstep.auth.context.SecurityContext;

import javax.servlet.http.HttpServletRequest;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionSecurityContextPersistenceInterceptor extends AbstractSecurityContextInterceptor {

    @Override
    protected SecurityContext extractSecurityContext(HttpServletRequest request) {
        return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }
}
