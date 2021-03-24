package nextstep.subway.auth.ui.session;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.*;

import javax.servlet.http.HttpServletRequest;

import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.SecurityContextInterceptor;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {
	@Override
	protected SecurityContext extractSecurityContext(HttpServletRequest request) {
		return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
	}
}
