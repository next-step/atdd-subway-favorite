package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import static nextstep.login.github.GithubResponses.*;
import static nextstep.subway.steps.AuthSteps.*;
import static nextstep.subway.steps.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

@DisplayName("토근 생성 API")
class AuthAcceptanceTest extends AcceptanceTest {

	private static final String EMAIL = "admin@email.com";
	private static final String PASSWORD = "password";

	private final String GITHUB_USER_CODE = "832ovnq039hfjn";
	private final String GITHUB_USER_ACCESS_TOKEN = "access_token_1";
	private final String GITHUB_USER_EMAIL = "email1@email.com";

	/**
	 * When 올바른 값으로 로그인 요청을 했을 때
	 * Then 토근값 응답을 받는다
	 * */
	@DisplayName("올바른 email, pw 입력 시 토근 생성")
	@Test
	void successBearerAuth() {
		// when
		ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

		// then
		String accessToken = response.jsonPath().getString("accessToken");
		assertThat(accessToken).isNotBlank();
	}

	/**
	 * When 틀린 값으로 로그인 요청을 했을 때
	 * Then 400에러 발생
	 * */
	@DisplayName("올바르지 않는 email, pw 입력 시 에러 발생")
	@Test
	void failBearerAuth() {
		ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, "testPassword");

		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	/**
	 * When 올바른 코드전송 시
	 * Then accessToken 받는다
	 * */
	@DisplayName("올바른 코드 전송 시 토큰 생성")
	@Test
	void githubAuthLogin() {
		// when
		ExtractableResponse<Response> githubResponse = githubAuth(GITHUB_USER_CODE);

		// then
		github_정상_응답(githubResponse, GITHUB_USER_ACCESS_TOKEN);
	}

	/**
	 * When 비정상 코드전송 시
	 * Then 400에러 발생
	 * */
	@DisplayName("비정상 코드 전송 시 에러 발생")
	@ParameterizedTest
	@ValueSource(strings = {"", "TEST_TEST"})
	void failGithubAuthLogin(String code) {
		// when
		ExtractableResponse<Response> githubResponse = githubAuth(code);

		// then
		github_실패_응답(githubResponse);
	}

	/**
	 * When 올바른 토큰전송 시
	 * Then accessToken 받는다
	 * */
	@DisplayName("올바른 토큰 전송 시 유저 정보 응답")
	@Test
	void successTokenAuth() {
		// when
		ExtractableResponse<Response> githubResponse = tokenAuth(GITHUB_USER_ACCESS_TOKEN);

		// then
		github_정상_유저정보_응답(githubResponse, GITHUB_USER_EMAIL);
	}

	/**
	 * When 비정상 토큰전송 시
	 * Then 400에러 발생
	 * */
	@DisplayName("비정상 토큰 전송 시 에러 발생")
	@ParameterizedTest
	@ValueSource(strings = {"", "TEST_TOKEN"})
	void failTokenAuth(String code) {
		// when
		ExtractableResponse<Response> githubResponse = tokenAuth(code);

		// then
		github_실패_응답(githubResponse);
	}

	@DisplayName("Oauth2.0 프로세스 적용")
	@MethodSource(value = {"githubResponses"})
	@ParameterizedTest
	void getGithubProfileFromGithubTest(String code, String accessToken, String email) {
		ExtractableResponse<Response> codeResponse = githubAuth(code);
		github_정상_응답(codeResponse, accessToken);

		String responseToken = codeResponse.jsonPath().getString("accessToken");

		ExtractableResponse<Response> toKenResponse = tokenAuth(responseToken);
		github_정상_유저정보_응답(toKenResponse, email);
	}

	private static Stream<Arguments> githubResponses() {
		return Stream.of(
			Arguments.of(사용자1.getCode(), 사용자1.getAccessToken(), 사용자1.getEmail()),
			Arguments.of(사용자2.getCode(), 사용자2.getAccessToken(), 사용자2.getEmail()),
			Arguments.of(사용자3.getCode(), 사용자3.getAccessToken(), 사용자3.getEmail()),
			Arguments.of(사용자4.getCode(), 사용자4.getAccessToken(), 사용자4.getEmail())
		);
	}
}
