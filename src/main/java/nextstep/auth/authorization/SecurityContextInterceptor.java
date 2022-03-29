package nextstep.auth.authorization;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class SecurityContextInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
			return true;
		}

		SecurityContext securityContext = getSecurityContext(request);

		if (Objects.nonNull(securityContext)) {
			SecurityContextHolder.setContext(securityContext);
		}

		return true;
	}

	protected abstract SecurityContext getSecurityContext(HttpServletRequest request);

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		SecurityContextHolder.clearContext();
	}
}