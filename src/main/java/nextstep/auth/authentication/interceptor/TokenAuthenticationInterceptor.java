package nextstep.auth.authentication.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(AuthenticationConverter authenticationConverter,
		UserDetailsService userDetailsService
		, JwtTokenProvider jwtTokenProvider) {
		super(authenticationConverter, userDetailsService);
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String payload = objectMapper.writeValueAsString(authentication.getPrincipal());
		TokenResponse tokenResponse = new TokenResponse(jwtTokenProvider.createToken(payload));
		String responseToClient = objectMapper.writeValueAsString(tokenResponse);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().print(responseToClient);
	}
}
