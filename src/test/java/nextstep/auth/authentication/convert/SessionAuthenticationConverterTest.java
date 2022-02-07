package nextstep.auth.authentication.convert;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Session 기반 인증 토큰 변환 테스트")
class SessionAuthenticationConverterTest {

	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";

	AuthenticationConverter sessionAuthenticationConverter;

	@BeforeEach
	void setUp() {
		sessionAuthenticationConverter = new SessionAuthenticationConverter();
	}

	@Test
	void convert() throws IOException {
		// given
		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletRequest.addParameter(SessionAuthenticationInterceptor.USERNAME_FIELD, EMAIL);
		mockHttpServletRequest.addParameter(SessionAuthenticationInterceptor.PASSWORD_FIELD, PASSWORD);

		// when
		AuthenticationToken authenticationToken = sessionAuthenticationConverter.convert(mockHttpServletRequest);

		// then
		assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
		assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
	}
}