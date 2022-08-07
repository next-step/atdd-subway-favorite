package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

public abstract class AuthenticationChainInterceptor implements HandlerInterceptor {

	public AuthenticationChainInterceptor() {

	}

	boolean afterAuthenticate(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return true;
	}

}
