package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class AuthAcceptanceTest extends AcceptanceTest {
	private static final String EMAIL = "admin@email.com";
	private static final String PASSWORD = "password";
	private static final Integer AGE = 20;

	@DisplayName("Basic Auth")
	@Test
	void myInfoWithBasicAuth() {
		ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);
		회원_정보_조회됨(response, EMAIL, AGE);
	}

	@DisplayName("Session 로그인 후 내 정보 조회")
	@Test
	void myInfoWithSession() {
		ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(EMAIL, PASSWORD);

		회원_정보_조회됨(response, EMAIL, AGE);
	}

	@DisplayName("Bearer Auth")
	@Test
	void myInfoWithBearerAuth() {
		String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

		ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

		회원_정보_조회됨(response, EMAIL, AGE);
	}

	private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
		return null;
	}
}
