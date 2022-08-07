package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;
import nextstep.auth.domain.CustomUser;
import nextstep.auth.service.CustomUserDetails;

public abstract class AuthenticationNonChainInterceptor implements HandlerInterceptor {
	private CustomUserDetails customUserDetails;

	public AuthenticationNonChainInterceptor(CustomUserDetails customUserDetails) {
		this.customUserDetails = customUserDetails;
	}

	public Authentication authenticate(AuthenticationToken authenticationToken) {
		CustomUser customUser = customUserDetails.loadUserByEmail(authenticationToken.getPrincipal());
		if (customUser == null) {
			throw new AuthenticationException();
		}
		if (!customUser.isValidPassword(authenticationToken.getCredentials())) {
			throw new AuthenticationException();
		}
		return new Authentication(authenticationToken.getPrincipal(), customUser.getAuthorities());
	}

}
