package nextstep.subway.auth;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.auth.ui.SecurityContextInterceptor;
import nextstep.subway.auth.ui.session.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.token.TokenSecurityContextPersistenceInterceptor;

class SecurityContextInterceptorTest {

	private static final String USERNAME_FIELD = "username";
	private static final String PASSWORD_FIELD = "password";
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

	@Test
	void preHandleWithToken() {
		// given
		String payload = "{\"id\":1,\"email\":\"email@email.com\",\"password\":\"password\",\"age\":20}";
		JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
		SecurityContextInterceptor interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, new ObjectMapper());
		given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
		given(jwtTokenProvider.getPayload(anyString())).willReturn(payload);

		// when
		final boolean isHandle = interceptor.preHandle(
			createMockRequestWithToken(), new MockHttpServletResponse(), new Object());

		// then
		assertThat(isHandle).isTrue();
		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
	}

	@Test
	void preHandleWithSession() {
		// given
		SecurityContextInterceptor interceptor = new SessionSecurityContextPersistenceInterceptor();
		MockHttpServletRequest request = createMockRequestWithForm();

		// when
		final boolean isHandle =
			interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

		// then
		assertThat(isHandle).isTrue();
		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
	}

	private MockHttpServletRequest createMockRequestWithToken() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer " + JWT_TOKEN);
		return request;
	}

	private MockHttpServletRequest createMockRequestWithForm() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter(USERNAME_FIELD, EMAIL);
		request.addParameter(PASSWORD_FIELD, PASSWORD);
		HttpSession httpSession = request.getSession();
		assert httpSession != null;
		httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(new Authentication()));
		return request;
	}
}