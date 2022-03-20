package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.내_회원_삭제_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.로그인_회원_정보_수정_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_삭제_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_수정_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MemberAcceptanceTest extends AcceptanceTest {

	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 20;

	@DisplayName("회원가입을 한다.")
	@Test
	void createMember() {
		// when
		ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	@DisplayName("회원 정보를 조회한다.")
	@Test
	void getMember() {
		// given
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// when
		ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

		// then
		회원_정보_조회됨(response, EMAIL, AGE);

	}

	@DisplayName("회원 정보를 수정한다.")
	@Test
	void updateMember() {
		// given
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// when
		ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@DisplayName("회원 정보를 삭제한다.")
	@Test
	void deleteMember() {
		// given
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// when
		ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	/*
	Scenario: 회원 정보를 관리
		When 회원 생성을 요청
		Then 회원 생성됨
		When 회원 정보 조회 요청
		Then 회원 정보 조회됨
		When 회원 정보 수정 요청
		Then 회원 정보 수정됨
		When 회원 삭제 요청
		Then 회원 삭제됨
	*/
	@DisplayName("회원 정보를 관리한다.")
	@Test
	void manageMember() {
		ExtractableResponse<Response> memberCreateResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
		assertThat(memberCreateResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		ExtractableResponse<Response> memberSearchResponse = 회원_정보_조회_요청(memberCreateResponse);
		회원_정보_조회됨(memberSearchResponse, EMAIL, AGE);

		ExtractableResponse<Response> memberUpdateResponse = 회원_정보_수정_요청(memberCreateResponse, "new" + EMAIL, "new" + PASSWORD, AGE);
		assertThat(memberUpdateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

		ExtractableResponse<Response> memberDeleteResponse = 회원_삭제_요청(memberCreateResponse);
		assertThat(memberDeleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("나의 정보를 관리한다.")
	@Test
	void manageMyInfo() {
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		String token = 로그인_되어_있음(EMAIL, PASSWORD);
		ExtractableResponse<Response> response = 로그인_회원_정보_수정_요청(token, EMAIL, PASSWORD, AGE);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

		ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(token);
		assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}