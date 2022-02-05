package nextstep.auth.authentication;

import nextstep.auth.authentication.convert.AuthenticationConverter;
import nextstep.auth.authentication.convert.SessionAuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.service.UserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("토큰 인증 기능 단위 테스트")
class SessionAuthenticationInterceptorTest {

	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";

	SessionAuthenticationInterceptor sessionAuthenticationInterceptor;

	AuthenticationConverter sessionAuthenticationConverter;

	@Mock
	UserDetailsService userDetailsService;

	@BeforeEach
	void setUp() {
		sessionAuthenticationConverter = new SessionAuthenticationConverter();
		sessionAuthenticationInterceptor = new SessionAuthenticationInterceptor(userDetailsService, sessionAuthenticationConverter);
	}

	@Test
	void preHandle() throws IOException {
		// given
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		when(userDetailsService.loadUserByUsername(anyString()))
				.thenReturn(getMockLoginMember());
		// when
		sessionAuthenticationInterceptor.preHandle(createMockRequest(), mockResponse, new Object());

		// then
		assertThat(mockResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	void authenticate() {
		// given
		LoginMember mockLoginMember = getMockLoginMember();
		when(userDetailsService.loadUserByUsername(anyString()))
				.thenReturn(mockLoginMember);

		// when
		AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
		Authentication authenticate = sessionAuthenticationInterceptor.authenticate(authenticationToken);

		// then
		LoginMember principal = (LoginMember) authenticate.getPrincipal();
		assertThat(principal.getEmail()).isEqualTo(mockLoginMember.getEmail());
		assertThat(principal.getPassword()).isEqualTo(mockLoginMember.getPassword());
	}

	private LoginMember getMockLoginMember() {
		return new LoginMember(0L, EMAIL, PASSWORD, 0);
	}

	private HttpServletRequest createMockRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter(SessionAuthenticationInterceptor.USERNAME_FIELD, EMAIL);
		request.addParameter(SessionAuthenticationInterceptor.PASSWORD_FIELD, PASSWORD);
		return request;
	}

}