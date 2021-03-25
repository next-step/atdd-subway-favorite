package nextstep.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;

public abstract class SecurityContextInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
		HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			return true;
		}

		SecurityContext securityContext = extractSecurityContext(request);
		if (securityContext != null) {
			SecurityContextHolder.setContext(securityContext);
		}
		return true;
	}

	@Override
	public void afterCompletion(
		HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		SecurityContextHolder.clearContext();
	}

	protected abstract SecurityContext extractSecurityContext(HttpServletRequest request);
}
