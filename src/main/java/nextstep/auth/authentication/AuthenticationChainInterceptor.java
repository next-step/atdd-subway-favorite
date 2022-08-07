package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.domain.AuthUser;
import nextstep.auth.service.CustomUserDetails;

public abstract class AuthenticationChainInterceptor implements HandlerInterceptor {
	private CustomUserDetails customUserDetails;

	public AuthenticationChainInterceptor(CustomUserDetails customUserDetails) {
		this.customUserDetails = customUserDetails;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		AuthenticationToken authenticationToken = convert(request);
		Authentication authentication = authenticate(authenticationToken);
		return afterAuthenticate(authentication, response);
	}

	AuthenticationToken convert(HttpServletRequest request) throws Exception {
		return new AuthenticationToken();
	}

	boolean afterAuthenticate(Authentication authentication, HttpServletResponse response) throws
		Exception {
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return true;
	}

	Authentication authenticate(AuthenticationToken authenticationToken) {
		AuthUser authUser = customUserDetails.loadUserByUsername(authenticationToken.getPrincipal());
		if (authUser == null) {
			throw new AuthenticationException();
		}
		if (!authUser.isValidPassword(authenticationToken.getCredentials())) {
			throw new AuthenticationException();
		}
		return new Authentication(authenticationToken.getPrincipal(), authUser.getAuthorities());
	}
}
