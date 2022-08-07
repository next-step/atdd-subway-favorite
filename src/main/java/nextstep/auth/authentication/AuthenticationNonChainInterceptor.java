package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;
import nextstep.auth.service.CustomUserDetails;

public abstract class AuthenticationNonChainInterceptor implements HandlerInterceptor {
	private CustomUserDetails customUserDetails;

	public AuthenticationNonChainInterceptor(CustomUserDetails customUserDetails) {
		this.customUserDetails = customUserDetails;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		AuthenticationToken authenticationToken = convert(request);
		Authentication authentication = authenticate(authenticationToken);
		afterAuthenticate(authentication, response);
		return false;
	}

	abstract AuthenticationToken convert(HttpServletRequest request);

	abstract boolean afterAuthenticate(Authentication authentication, HttpServletResponse response);

	abstract Authentication authenticate(AuthenticationToken authenticationToken);
}
