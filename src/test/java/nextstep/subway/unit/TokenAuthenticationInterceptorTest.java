package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

	private static final String EMAIL = "heesunjung@gmail.com";
	private static final String PASSWORD = "password";
	public static final String JWT_TOKEN = "JWT_TOKEN";

	@Mock
	private UserDetailsService userDetailsService;
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	@Mock
	private AuthenticationConverter converter;
	private ObjectMapper objectMapper = new ObjectMapper();

	private TokenAuthenticationInterceptor interceptor;

	@BeforeEach
	void setUp() {
		interceptor = new TokenAuthenticationInterceptor(converter, userDetailsService, jwtTokenProvider, objectMapper);
	}

	@Test
	void preHandle() throws IOException {
		//given
		LoginMember member = new LoginMember(1L, EMAIL, PASSWORD, 20);
		given(userDetailsService.loadUserByUsername(eq(EMAIL))).willReturn(member);
		given(jwtTokenProvider.createToken(any())).willReturn(JWT_TOKEN);
		given(converter.convert(any())).willReturn(new AuthenticationToken(EMAIL, PASSWORD));

		MockHttpServletResponse response = new MockHttpServletResponse();

		//when
		interceptor.preHandle(createMockRequest(), response, new Object());

		//then
		assertAll(
			() -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(new TokenResponse(JWT_TOKEN)))
		);
	}

	private MockHttpServletRequest createMockRequest() throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
		request.setContent(objectMapper.writeValueAsString(tokenRequest).getBytes());
		return request;
	}
}