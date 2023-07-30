package nextstep.member.acceptance;

import static nextstep.auth.acceptance.AuthSteps.일반_로그인_요청;
import static nextstep.member.acceptance.MemberSteps.내_정보_조회_요청;
import static nextstep.member.acceptance.MemberSteps.회원_삭제_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_수정_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_조회_요청;
import static nextstep.member.acceptance.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;

class MemberAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 20;

	@DisplayName("회원가입을 한다.")
	@Test
	void createMember() {
		// when
		var response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// then
		응답_상태코드_검증(response, HttpStatus.CREATED);
	}

	@DisplayName("회원 정보를 조회한다.")
	@Test
	void getMember() {
		// given
		var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// when
		var response = 회원_정보_조회_요청(createResponse);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		회원_정보_조회됨(response, EMAIL, AGE);
	}

	@DisplayName("회원 정보를 수정한다.")
	@Test
	void updateMember() {
		// given
		var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// when
		var response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
	}

	@DisplayName("회원 정보를 삭제한다.")
	@Test
	void deleteMember() {
		// given
		var createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// when
		var response = 회원_삭제_요청(createResponse);

		// then
		응답_상태코드_검증(response, HttpStatus.NO_CONTENT);
	}

	/**
	 * Given 회원 가입을 생성하고
	 * And 로그인을 하고
	 * When 토큰을 통해 내 정보를 조회하면
	 * Then 내 정보를 조회할 수 있다
	 */
	@DisplayName("내 정보를 조회한다.")
	@Test
	void getMyInfo() {
		// given
		회원_생성_요청(EMAIL, PASSWORD, AGE);
		var tokenResponse = 일반_로그인_요청(EMAIL, PASSWORD);

		// when
		var response = 내_정보_조회_요청(tokenResponse.jsonPath().getString("accessToken"));

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		회원_정보_조회됨(response, EMAIL, AGE);
	}

	private void 응답_상태코드_검증(ExtractableResponse<Response> response, HttpStatus httpStatus) {
		assertThat(response.statusCode()).isEqualTo(httpStatus.value());
	}
}
