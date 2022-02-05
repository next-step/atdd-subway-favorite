package nextstep.auth.authentication.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Token 기반 인증 토큰 변환 테스트")
class TokenAuthenticationConverterTest {

	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";

	AuthenticationConverter tokenAuthenticationConverter;

	@BeforeEach
	void setUp() {
		tokenAuthenticationConverter = new TokenAuthenticationConverter();
	}

	@Test
	void convert() throws IOException {
		// when
		AuthenticationToken authenticationToken = tokenAuthenticationConverter.convert(createMockRequest());

		// then
		assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
		assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
	}

	private MockHttpServletRequest createMockRequest() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
		return request;
	}

}