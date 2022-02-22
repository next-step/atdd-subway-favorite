package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.User;
import nextstep.auth.token.TokenRequest;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface AuthenticationConverter {

	default AuthenticationToken convert(HttpServletRequest request) throws IOException {
		Map<String, String[]> paramMap = request.getParameterMap();

		if (paramMap.isEmpty()) {
			final TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
			return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
		}
		String principal = paramMap.get("username")[0];
		String credentials = paramMap.get("password")[0];

		return new AuthenticationToken(principal, credentials);

	}

	default void checkAuthentication(User userDetails, AuthenticationToken token) {
		if (ObjectUtils.isEmpty(userDetails)) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(token.getCredentials())) {
			throw new AuthenticationException();
		}
	}
}
