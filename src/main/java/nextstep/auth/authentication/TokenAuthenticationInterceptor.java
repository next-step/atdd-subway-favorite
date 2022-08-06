package nextstep.auth.authentication;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.context.Authentication;
import nextstep.auth.service.CustomUserDetails;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor {

	private JwtTokenProvider jwtTokenProvider;

	public TokenAuthenticationInterceptor(CustomUserDetails customUserDetails, JwtTokenProvider jwtTokenProvider) {
		super(customUserDetails);
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public AuthenticationToken convert(HttpServletRequest request) throws IOException {
		String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);
		return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
	}

	@Override
	public boolean afterAuthenticate(Authentication authentication, HttpServletResponse response) throws
		Exception {

		String token = jwtTokenProvider.createToken((String)authentication.getPrincipal(),
			authentication.getAuthorities());

		String responseToClient = new ObjectMapper().writeValueAsString(new TokenResponse(token));
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().print(responseToClient);
		return false;
	}
}
