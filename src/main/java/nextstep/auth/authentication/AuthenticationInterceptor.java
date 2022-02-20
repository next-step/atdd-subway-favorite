package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;

public class AuthenticationInterceptor implements HandlerInterceptor {
	private UserDetailsService userDetailsService;

	public AuthenticationInterceptor(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public Authentication authenticate(AuthenticationToken authenticationToken) {
		String principal = authenticationToken.getPrincipal();
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, authenticationToken);

		return new Authentication(userDetails);
	}

	private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
		if (userDetails == null) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}
}
