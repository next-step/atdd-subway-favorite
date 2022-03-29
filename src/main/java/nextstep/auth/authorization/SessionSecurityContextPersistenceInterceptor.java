package nextstep.auth.authorization;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

import javax.servlet.http.HttpServletRequest;
import nextstep.auth.context.SecurityContext;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

	@Override
	protected SecurityContext getSecurityContext(HttpServletRequest request) {
		return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
	}
}
