package nextstep.auth.authorization;

import static nextstep.auth.context.SecurityContextHolder.*;

import javax.servlet.http.HttpServletRequest;

import nextstep.auth.context.SecurityContext;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    @Override
    public SecurityContext getSecurityContext(HttpServletRequest request) {
        return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }
}
