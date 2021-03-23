package nextstep.subway.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.exception.NotValidPasswordException;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.AuthenticationInterceptor;
import nextstep.subway.auth.ui.session.SessionAuthInterceptor;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.token.TokenAuthInterceptor;
import nextstep.subway.auth.ui.token.TokenAuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

public class AuthenticationInterceptorTest {

	private static final String USERNAME_FIELD = "username";
	private static final String PASSWORD_FIELD = "password";
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final String NEW_PASSWORD = "new_password";

	private CustomUserDetailsService userDetailsService;
	private JwtTokenProvider jwtTokenProvider;
	private AuthenticationInterceptor interceptor;

	@BeforeEach
	void setUp() {
		userDetailsService = mock(CustomUserDetailsService.class);
		jwtTokenProvider = mock(JwtTokenProvider.class);
	}

	@DisplayName("토큰 기반 인증 테스트")
	@Test
	void preHandleWithToken() throws IOException {
		// given
		final MockHttpServletRequest request = createMockRequestWithToken();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		interceptor = new TokenAuthInterceptor(new TokenAuthenticationConverter(objectMapper), userDetailsService, jwtTokenProvider, objectMapper);
		given(userDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
		given(jwtTokenProvider.createToken(any())).willReturn(JWT_TOKEN);


		// when
		interceptor.preHandle(request, response, new Object());

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
		assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
	}

	@DisplayName("토큰기반 인증 - 패스워드가 틀릴 경우")
	@Test
	void tokenAuthenticationInvalidPassword() throws IOException {
		// given
		final MockHttpServletRequest request = createMockRequestWithToken();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		interceptor = new TokenAuthInterceptor(new TokenAuthenticationConverter(objectMapper), userDetailsService, jwtTokenProvider, objectMapper);
		given(userDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(1L, EMAIL, NEW_PASSWORD, 20));

		// when, then
		assertThatThrownBy(
			() -> interceptor.preHandle(request, response, new Object()))
			.isInstanceOf(NotValidPasswordException.class);
	}

	@DisplayName("폼 기반 인증 테스트")
	@Test
	void preHandleWithForm() throws IOException {
		// given
		final MockHttpServletRequest request = createMockRequestWithForm();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		interceptor = new SessionAuthInterceptor(new SessionAuthenticationConverter(), userDetailsService);
		given(userDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

		// when
		interceptor.preHandle(request, response, new Object());

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@DisplayName("폼기반 인증 - 패스워드가 틀릴 경우")
	@Test
	void formAuthenticationInvalidPassword() {
		// given
		final MockHttpServletRequest request = createMockRequestWithForm();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		interceptor = new SessionAuthInterceptor(new SessionAuthenticationConverter(), userDetailsService);
		given(userDetailsService.loadUserByUsername(any())).willReturn(new LoginMember(1L, EMAIL, NEW_PASSWORD, 20));

		// when, then
		assertThatThrownBy(
			() -> interceptor.preHandle(request, response, new Object()))
			.isInstanceOf(NotValidPasswordException.class);
	}

	private MockHttpServletRequest createMockRequestWithToken() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
		return request;
	}

	private MockHttpServletRequest createMockRequestWithForm() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		request.addParameter(USERNAME_FIELD, EMAIL);
		request.addParameter(PASSWORD_FIELD, PASSWORD);
		return request;
	}
}
