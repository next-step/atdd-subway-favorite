package nextstep.auth.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProperties;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Token 기반 Authorization Interceptor 테스트")
class TokenSecurityContextPersistenceInterceptorTest {

	private static final String EMAIL = "email@email.com";
	private static final String PASSWORD = "password";

	JwtTokenProperties fakeJwtTokenProperties = new JwtTokenProperties("secret", 10_000L);

	SecurityContextInterceptor tokenSecurityContextPersistenceInterceptor;
	JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		jwtTokenProvider = new JwtTokenProvider(fakeJwtTokenProperties);
		tokenSecurityContextPersistenceInterceptor = new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
	}

	@Test
	void preHandle() throws Exception {
		// when
		tokenSecurityContextPersistenceInterceptor.preHandle(getMockHttpServletRequest(), new MockHttpServletResponse(), new Object());

		// then
		LoginMember principal = (LoginMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		assertThat(principal.getEmail()).isEqualTo(EMAIL);
		assertThat(principal.getPassword()).isEqualTo(PASSWORD);
	}

	private HttpServletRequest getMockHttpServletRequest() throws JsonProcessingException {
		String TOKEN = createJwtToken();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(AuthorizationExtractor.AUTHORIZATION, "bearer " + TOKEN);
		return request;
	}

	private LoginMember getMockLoginMember() {
		return new LoginMember(0L, EMAIL, PASSWORD, 0);
	}

	private String createJwtToken() throws JsonProcessingException {
		String payload = new ObjectMapper().writeValueAsString(getMockLoginMember());
		return jwtTokenProvider.createToken(payload);
	}

}