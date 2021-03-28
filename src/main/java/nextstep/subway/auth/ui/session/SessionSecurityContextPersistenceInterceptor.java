package nextstep.subway.auth.ui.session;

import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.base.SecurityContextInterceptor;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    @Override
    protected boolean proceed(HttpServletRequest request) {
        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        setSecurityContext(securityContext);
        return true;
    }
}
