package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class SessionAuthenticationConverterTest {

	private static final String EMAIL = "heesunjung@gmail.com";
	private static final String PASSWORD = "password";

	private final SessionAuthenticationConverter converter = new SessionAuthenticationConverter();

	@Test
	void convert() {
		//given
		MockHttpServletRequest mockRequest = createMockRequest();

		//when
		AuthenticationToken convert = converter.convert(mockRequest);

		//then
		String credentials = convert.getCredentials();
		System.out.println("credentials = " + credentials);
	}

	private MockHttpServletRequest createMockRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("username", EMAIL);
		request.addParameter("password", PASSWORD);
		return request;
	}
}