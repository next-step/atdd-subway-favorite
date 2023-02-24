package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.steps.AuthSteps.*;
import static nextstep.subway.steps.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("토근 생성 API")
class AuthAcceptanceTest extends AcceptanceTest {

	private static final String EMAIL = "admin@email.com";
	private static final String PASSWORD = "password";

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
		ExtractableResponse<Response> githubResponse = githubAuth("832ovnq039hfjn");

		// then
		github_정상_응답(githubResponse);
	}

	/**
	 * When 비정상 코드전송 시
	 * Then 400에러 발생
	 * */
	@DisplayName("비정상 코드 전송 시 토큰 생성")
	@Test
	void failGithubAuthLogin() {
		// when
		ExtractableResponse<Response> githubResponse = githubAuth("TEST_TEST");

		// then
		github_실패_응답(githubResponse);
	}
}
