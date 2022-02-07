package nextstep.auth.authentication.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter {

	@Override
	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		TokenRequest tokenRequest = readTokenRequest(request);
		String principal = tokenRequest.getEmail();
		String credentials = tokenRequest.getPassword();

		return new AuthenticationToken(principal, credentials);
	}

	private TokenRequest readTokenRequest(HttpServletRequest request) throws IOException {
		return new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
	}
}
