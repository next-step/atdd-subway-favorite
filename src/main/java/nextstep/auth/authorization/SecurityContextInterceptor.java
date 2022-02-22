package nextstep.auth.authorization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;

public abstract class SecurityContextInterceptor implements HandlerInterceptor {
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		SecurityContextHolder.clearContext();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			return true;
		}

		SecurityContext securityContext = getSecurityContext(request);
		if (securityContext != null) {
			SecurityContextHolder.setContext(securityContext);
		}
		return true;
	}

	public abstract SecurityContext getSecurityContext(HttpServletRequest request);
}
