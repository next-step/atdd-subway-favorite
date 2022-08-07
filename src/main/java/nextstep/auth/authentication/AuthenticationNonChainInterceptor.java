package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;
import nextstep.auth.domain.AuthUser;
import nextstep.auth.service.CustomUserDetails;

public abstract class AuthenticationNonChainInterceptor implements HandlerInterceptor {
	private CustomUserDetails customUserDetails;

	public AuthenticationNonChainInterceptor(CustomUserDetails customUserDetails) {
		this.customUserDetails = customUserDetails;
	}

	public Authentication authenticate(AuthenticationToken authenticationToken) {
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
