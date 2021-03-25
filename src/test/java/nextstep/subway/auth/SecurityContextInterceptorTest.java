package nextstep.subway.auth;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.SecurityContextInterceptor;
import nextstep.subway.auth.ui.session.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.token.TokenSecurityContextPersistenceInterceptor;

class SecurityContextInterceptorTest {

	private static final String USERNAME_FIELD = "username";
	private static final String PASSWORD_FIELD = "password";
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";

	@Test
	void preHandleWithToken() throws IOException {
		// given
		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
		SecurityContextInterceptor interceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, new ObjectMapper());

		// when
		final boolean isHandle = interceptor.preHandle(
			createMockRequestWithToken(), new MockHttpServletResponse(), new Object());

		// then
		assertThat(isHandle).isTrue();
	}

	@Test
	void preHandleWithSession() {
		// given
		SecurityContextInterceptor interceptor = new SessionSecurityContextPersistenceInterceptor();

		// when
		final boolean isHandle =
			interceptor.preHandle(createMockRequestWithForm(), new MockHttpServletResponse(), new Object());

		// then
		assertThat(isHandle).isTrue();
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