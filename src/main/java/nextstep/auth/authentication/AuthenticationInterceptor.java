package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;

public class AuthenticationInterceptor implements HandlerInterceptor {
	private CustomUserDetailsService userDetailsService;

	public AuthenticationInterceptor(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public Authentication authenticate(AuthenticationToken authenticationToken) {
		String principal = authenticationToken.getPrincipal();
		LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, authenticationToken);

		return new Authentication(userDetails);
	}

	private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
		if (userDetails == null) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}
}
