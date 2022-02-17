package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class MemberAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 20;

	@DisplayName("회원가입을 한다.")
	@Test
	void createMember() {
		ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	@DisplayName("회원 정보를 조회한다.")
	@Test
	void getMember() {
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

		회원_정보_조회됨(response, EMAIL, AGE);

	}

	@DisplayName("회원 정보를 수정한다.")
	@Test
	void updateMember() {
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@DisplayName("회원 정보를 삭제한다.")
	@Test
	void deleteMember() {
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("회원 정보를 관리한다.")
	@Test
	void manageMember() {
		// given
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		// when
		ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

		ExtractableResponse<Response> 회원_정보_반환 = 회원_정보_조회_요청(createResponse);

		회원_정보_조회됨(회원_정보_반환, "newemail@email.com", AGE);
	}

	@DisplayName("나의 정보를 관리한다.")
	@Test
	void manageMyInfo() {
		ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

		String 로그인_회원_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

		내_회원_정보_수정_요청(로그인_회원_토큰, "new" + EMAIL, "new" + PASSWORD, AGE);

		ExtractableResponse<Response> 내_회원_정보_반환 = 내_회원_정보_조회_요청(로그인_회원_토큰);

		회원_정보_조회됨(내_회원_정보_반환, "newemail@email.com", AGE);
	}
}