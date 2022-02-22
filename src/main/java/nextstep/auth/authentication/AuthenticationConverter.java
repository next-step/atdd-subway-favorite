package nextstep.auth.authentication;

import nextstep.auth.User;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AuthenticationConverter {

	AuthenticationToken convert(HttpServletRequest request) throws IOException;

	default void checkAuthentication(User userDetails, AuthenticationToken token) {
		if (ObjectUtils.isEmpty(userDetails)) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}
}
