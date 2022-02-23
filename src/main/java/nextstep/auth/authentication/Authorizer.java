package nextstep.auth.authentication;

import nextstep.auth.User;
import nextstep.auth.UserDetailsService;
import nextstep.auth.context.Authentication;
import org.springframework.util.ObjectUtils;

public class Authorizer {

	private UserDetailsService userDetailsService;

	public Authorizer(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public void checkAuthentication(User userDetails, AuthenticationToken token) {
		if (ObjectUtils.isEmpty(userDetails)) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}

	public Authentication authenticate(AuthenticationToken authenticationToken) {
		final String principal = authenticationToken.getPrincipal();
		final User userDetails = userDetailsService.loadUserByUsername(principal);
		checkAuthentication(userDetails, authenticationToken);

		return new Authentication(userDetails);
	}
}
