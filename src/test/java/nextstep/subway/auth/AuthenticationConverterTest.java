package nextstep.subway.auth;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.session.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.token.TokenAuthenticationConverter;

public class AuthenticationConverterTest {

	private static final String USERNAME_FIELD = "username";
	private static final String PASSWORD_FIELD = "password";
	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private AuthenticationConverter converter;

	@DisplayName("토큰 방식으로 변환한다.")
	@Test
	void convertWithToken() throws IOException {
		// given
		final MockHttpServletRequest mockRequest = createMockRequestWithToken();
		converter = new TokenAuthenticationConverter(objectMapper);

		// when
		final AuthenticationToken authenticationToken = converter.convert(mockRequest);

		// then
		assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
		assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
	}

	@DisplayName("세션 방식으로 변환한다.")
	@Test
	void convertWithSession() throws IOException {
		// given
		final MockHttpServletRequest mockRequest = createMockRequestWithForm();
		converter = new SessionAuthenticationConverter();

		// when
		final AuthenticationToken authenticationToken = converter.convert(mockRequest);

		// then
		assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
		assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
	}

	private MockHttpServletRequest createMockRequestWithToken() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		request.setContent(objectMapper.writeValueAsString(tokenRequest).getBytes());
		return request;
	}

	private MockHttpServletRequest createMockRequestWithForm() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		request.addParameter(USERNAME_FIELD, EMAIL);
		request.addParameter(PASSWORD_FIELD, PASSWORD);
		return request;
	}
}
