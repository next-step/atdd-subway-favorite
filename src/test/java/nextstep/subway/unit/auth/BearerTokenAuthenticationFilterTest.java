package nextstep.subway.unit.auth;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.token.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
public class BearerTokenAuthenticationFilterTest {
	private static final String EMAIL = "email@email.com";
	public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

	String accessToken;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		MockHttpServletRequest request = createMockRequest();
		accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
	}

	/*
		@Test
		void convert() {
			when(jwtTokenProvider.validateToken(JWT_TOKEN)).thenReturn(true);

			assertThat(accessToken).isEqualTo(JWT_TOKEN);
			assertThat(jwtTokenProvider.validateToken(accessToken)).isTrue();
		}

		@Test
		void authenticate() {
			when(jwtTokenProvider.getPrincipal(JWT_TOKEN)).thenReturn(EMAIL);

			assertThat(jwtTokenProvider.getPrincipal(accessToken)).isEqualTo(EMAIL);
		}
	*/
	private MockHttpServletRequest createMockRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + JWT_TOKEN);

		return request;
	}
}
