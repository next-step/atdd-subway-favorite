package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Token 기반 Authorization Interceptor 테스트")
class SessionSecurityContextPersistenceInterceptorTest {

	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";

	SecurityContextInterceptor sessionSecurityContextInterceptor;

	@BeforeEach
	void setUp() {
		sessionSecurityContextInterceptor = new SessionSecurityContextPersistenceInterceptor();
	}

	@Test
	void preHandle() throws Exception {
		// given
		SecurityContext securityContext = new SecurityContext(new Authentication(createMockLoginMember()));

		// when
		sessionSecurityContextInterceptor.preHandle(createMockHttpServletRequest(securityContext), new MockHttpServletResponse(), new Object());

		// then
		LoginMember principal = (LoginMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		assertThat(principal.getEmail()).isEqualTo(EMAIL);
		assertThat(principal.getPassword()).isEqualTo(PASSWORD);
	}

	private LoginMember createMockLoginMember() {
		return new LoginMember(0L, EMAIL, PASSWORD, 0);
	}

	private HttpServletRequest createMockHttpServletRequest(SecurityContext securityContext) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
		request.setSession(session);
		return request;

	}
}