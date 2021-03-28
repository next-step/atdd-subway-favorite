package nextstep.subway.auth.ui.persistence;

import nextstep.subway.auth.infrastructure.SecurityContext;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionSecurityContextInterceptor extends SecurityContextInterceptor {

    @Override
    public SecurityContext getSecurityContext(HttpServletRequest request) {
        return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }

}
