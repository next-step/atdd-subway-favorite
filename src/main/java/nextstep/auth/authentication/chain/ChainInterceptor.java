package nextstep.auth.authentication.chain;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ChainInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			Authentication authentication = createAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			return true;
		}
		return true;
	}

	protected abstract Authentication createAuthentication(HttpServletRequest request);
}
