package nextstep.auth.authentication;

import nextstep.auth.User;
import org.springframework.util.ObjectUtils;

public class Authorizor {

	public void checkAuthentication(User userDetails, AuthenticationToken token) {
		if (ObjectUtils.isEmpty(userDetails)) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}
}
