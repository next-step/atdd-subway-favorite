package nextstep.auth.authentication.archive;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

@Deprecated
public class TokenAuthenticationInterceptorOld implements HandlerInterceptor {
	private LoginMemberService loginMemberService;
	private JwtTokenProvider jwtTokenProvider;

	public TokenAuthenticationInterceptorOld(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
		this.loginMemberService = loginMemberService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

		String principal = tokenRequest.getEmail();
		String credentials = tokenRequest.getPassword();

		LoginMember loginMember = loginMemberService.loadUserByUsername(principal);

		if (loginMember == null) {
			throw new AuthenticationException();
		}

		if (!loginMember.checkPassword(credentials)) {
			throw new AuthenticationException();
		}

		String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
		TokenResponse tokenResponse = new TokenResponse(token);

		String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().print(responseToClient);

		return false;
	}
}
