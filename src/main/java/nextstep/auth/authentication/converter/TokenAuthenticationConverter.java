package nextstep.auth.authentication.converter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter {
	private final ObjectMapper objectMapper;
	public TokenAuthenticationConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override

	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
		String principal = tokenRequest.getEmail();
		String credentials = tokenRequest.getPassword();
		return new AuthenticationToken(principal, credentials);
	}
}
