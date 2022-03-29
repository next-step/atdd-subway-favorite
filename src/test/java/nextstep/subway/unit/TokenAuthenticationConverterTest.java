package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationConverter;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationConverterTest {

	private static final String EMAIL = "heesunjung@gmail.com";
	private static final String PASSWORD = "password";

	private final ObjectMapper objectMapper = new ObjectMapper();
	private TokenAuthenticationConverter converter;

	@BeforeEach
	void setUp() {
		converter = new TokenAuthenticationConverter(objectMapper);
	}

	@Test
	void convert() throws IOException {
		// when
		AuthenticationToken authenticationToken = converter.convert(createMockRequest());

		// then
		assertAll(
			() -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
			() -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
		);
	}

	private MockHttpServletRequest createMockRequest() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		request.setContent(objectMapper.writeValueAsString(tokenRequest).getBytes());
		return request;
	}
}
