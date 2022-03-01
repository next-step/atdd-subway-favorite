package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class TokenAuthenticationConverter implements AuthenticationConverter {

	@Override
	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		TokenRequest tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);

		return AuthenticationToken.of(tokenRequest.getEmail(), tokenRequest.getPassword());
	}
}
