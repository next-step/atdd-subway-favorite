package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class UrlBasedAuthorizationInterceptorTest {

	UrlBasedAuthorizationInterceptor urlBasedAuthorizationInterceptor;

	@BeforeEach
	void setUp() {
		urlBasedAuthorizationInterceptor = new UrlBasedAuthorizationInterceptor(
				asList(URI.create("/authorized"), URI.create("/authorized2/**"))
		);
	}

	@ParameterizedTest
	@CsvSource({"/favorites, false", "/authorized, true", "/authorized2/abc, true", "/authorized2, true"})
	void isMatch(String uri, boolean expectedIsMatched) {
		// when
		boolean isMatched = urlBasedAuthorizationInterceptor.isMatch(uri);

		// then
		assertThat(isMatched).isEqualTo(expectedIsMatched);
	}

	@ParameterizedTest
	@CsvSource({"/favorites", "/authorized", "/authorized2/abc", "/authorized2"})
	void preHandleSuccess(String requestPath) {
		// given
		setPrincipalToContextHolder();
		HttpServletRequest request = getMockHttpServletRequest(requestPath);

		// when
		HttpServletResponse response = new MockHttpServletResponse();
		boolean isSuccess = urlBasedAuthorizationInterceptor.preHandle(request, response, new Object());

		// then
		assertThat(isSuccess).isTrue();
	}

	@ParameterizedTest
	@CsvSource({"/favorites, true", "/authorized, false", "/authorized2/abc, false", "/authorized2, false"})
	void preHandleFail(String requestPath, boolean expectedResult) {
		// given
		clearContextHolder();
		HttpServletRequest request = getMockHttpServletRequest(requestPath);

		// when
		HttpServletResponse response = new MockHttpServletResponse();
		boolean isSuccess = urlBasedAuthorizationInterceptor.preHandle(request, response, new Object());

		// then
		assertThat(isSuccess).isEqualTo(expectedResult);
		if (!isSuccess) {
			assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		}
	}

	private void clearContextHolder() {
		SecurityContextHolder.clearContext();
	}


	private void setPrincipalToContextHolder() {
		SecurityContextHolder.setContext(
				new SecurityContext(
						new Authentication(
								new LoginMember(0L, "email@email.com", "password", 0))));
	}

	private HttpServletRequest getMockHttpServletRequest(String requestPath) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServletPath(requestPath);
		return request;
	}

}