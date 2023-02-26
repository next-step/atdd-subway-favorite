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

	private final String GITHUB_USER_CODE = "832ovnq039hfjn";
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
	 * When 정상 권한증서(code) 전달 시
	 * Then AccessToken 발급
	 * */
	@DisplayName("정상 권한증서(code) 전송 시 토큰 생성")
	@Test
	void githubAuthLogin() {
		// when
		ExtractableResponse<Response> githubResponse = githubAuth(GITHUB_USER_CODE);

		// then
		github_정상_응답(githubResponse);
	}

	/**
	 * Given 정상 권한증서(code) 전달 후 AccessToken 발급
	 * When AccessToken으로 Member를 조회하면
	 * Then Github profile 이메일이 조회된다.
	 * */
	@DisplayName("정상 권한증서(code) 전송 시 유저 정보 응답")
	@Test
	void successTokenAuth() {
		// given
		String accessToken = githubAuth(GITHUB_USER_CODE).jsonPath().getString("accessToken");

		// when
		ExtractableResponse<Response> tokenResponse = 베이직_인증으로_내_회원_정보_조회_요청(accessToken);

		// then
		github_정상_유저정보_응답(tokenResponse, GITHUB_USER_EMAIL);
	}
}
