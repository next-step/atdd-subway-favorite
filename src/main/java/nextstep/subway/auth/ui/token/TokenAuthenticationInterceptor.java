package nextstep.subway.auth.ui.token;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationInterceptor;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

	private final JwtTokenProvider jwtTokenProvider;
	private final ObjectMapper objectMapper;

	public TokenAuthenticationInterceptor(TokenAuthenticationConverter converter,
		UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
		super(converter, userDetailsService);
		this.jwtTokenProvider = jwtTokenProvider;
		this.objectMapper = objectMapper;
	}

	@Override
	protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		final String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
		final String token = jwtTokenProvider.createToken(payload);

		TokenResponse tokenResponse = new TokenResponse(token);

		String responseToClient = objectMapper.writeValueAsString(tokenResponse);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().print(responseToClient);
	}
}
