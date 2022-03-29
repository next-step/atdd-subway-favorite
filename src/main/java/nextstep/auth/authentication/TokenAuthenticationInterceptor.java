package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import org.springframework.http.MediaType;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

	private final JwtTokenProvider jwtTokenProvider;
	private final ObjectMapper objectMapper;

	public TokenAuthenticationInterceptor(AuthenticationConverter converter, UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
		super(converter, userDetailsService);
		this.jwtTokenProvider = jwtTokenProvider;
		this.objectMapper = objectMapper;
	}

	public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
		String token = jwtTokenProvider.createToken(payload);
		TokenResponse tokenResponse = new TokenResponse(token);

		String responseToClient = objectMapper.writeValueAsString(tokenResponse);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().print(responseToClient);
	}
}
