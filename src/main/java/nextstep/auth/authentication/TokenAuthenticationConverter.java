package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.token.TokenRequest;

public class TokenAuthenticationConverter implements AuthenticationConverter {

	private final ObjectMapper objectMapper;

	public TokenAuthenticationConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public AuthenticationToken convert(HttpServletRequest request) {
		TokenRequest tokenRequest = getTokenRequest(request);
		String principal = tokenRequest.getEmail();
		String credentials = tokenRequest.getPassword();

		return new AuthenticationToken(principal, credentials);
	}

	private TokenRequest getTokenRequest(HttpServletRequest request) {
		try {
			return objectMapper.readValue(request.getInputStream(), TokenRequest.class);
		} catch (IOException e) {
			throw new AuthenticationException();
		}
	}
}
