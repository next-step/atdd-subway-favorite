package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SessionAuthenticationConverter;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class SessionAuthenticationInterceptorTest {

	private static final String EMAIL = "heesunjung@gmail.com";
	private static final String PASSWORD = "password";

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private SessionAuthenticationConverter converter;

	@Mock
	UserDetails userDetails;

	private SessionAuthenticationInterceptor interceptor;

	@BeforeEach
	void setUp() {
		interceptor = new SessionAuthenticationInterceptor(converter, userDetailsService);
	}

	@Test
	void preHandle() throws IOException {
		//given
		given(userDetails.checkPassword(eq(PASSWORD))).willReturn(true);
		given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(userDetails);
		MockHttpServletRequest mockRequest = createMockRequest();
		given(converter.convert(eq(mockRequest))).willReturn(new AuthenticationToken(EMAIL, PASSWORD));
		MockHttpServletResponse response = new MockHttpServletResponse();

		//when

		interceptor.preHandle(mockRequest, response, new Object());

		//then
		assertAll(
			() -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value())
		);
	}

	private MockHttpServletRequest createMockRequest() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
		return request;
	}
}
