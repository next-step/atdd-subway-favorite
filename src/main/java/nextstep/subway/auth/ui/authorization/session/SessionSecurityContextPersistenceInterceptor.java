package nextstep.subway.auth.ui.authorization.session;

import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.authorization.SecurityContextInterceptor;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    @Override
    protected SecurityContext extractSecurityContext(HttpServletRequest request) {
        SecurityContext context = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        return context;
    }
}
