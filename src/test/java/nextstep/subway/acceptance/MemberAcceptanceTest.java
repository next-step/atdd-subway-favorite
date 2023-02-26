package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class MemberAcceptanceTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final int AGE = 20;

	@Nested
	@DisplayName("회원 생성을 검증하지 않는 테스트")
	class AssertNotMemberCreationTest extends AcceptanceTest {
		public ExtractableResponse<Response> createResponse;

		@BeforeEach
		public void setUp() {
			// given
			super.setUp();
			createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
		}

		@DisplayName("회원 정보를 조회한다.")
		@Test
		void getMember() {
			// when
			ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

			// then
			회원_정보_조회됨(response, EMAIL, AGE);

		}

		@DisplayName("회원 정보를 수정한다.")
		@Test
		void updateMember() {
			// when
			ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

			// then
			assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		}

		@DisplayName("회원 정보를 삭제한다.")
		@Test
		void deleteMember() {
			// when
			ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

			// then
			assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		}

		@DisplayName("내 정보를 조회한다.")
		@Test
		void getMyInfo() {
			// given
			String token = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

			// when
			ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(token);

			// then
			회원_정보_조회됨(response, EMAIL, AGE);
		}

		@DisplayName("내 정보를 조회한다. - 유효하지 않은 토큰일 경우, 예외를 던진다.")
		@Test
		void getMyInfo_fail_by_invalid_token() {
			// given
			String token = "Invalid Token";

			// when
			ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(token);

			// then
			assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
		}
	}

	@Nested
	@DisplayName("회원 생성을 검증하는 테스트")
	class AssertMemberCreationTest extends AcceptanceTest {
		@DisplayName("회원가입을 한다.")
		@Test
		void createMember() {
			// when
			ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

			// then
			assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		}
	}
}
