package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import static nextstep.subway.steps.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

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

	/**
	 * Given 회원 생성 후 Bearer 토큰을 요청
	 * When 생성된 토큰으로 정보를 조회하면
	 * Then 정보가 일치한다.
	 * */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
		// given
		회원_생성_요청(EMAIL, PASSWORD, AGE);
		String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

		// when
		ExtractableResponse<Response> tokenResponse = 베이직_인증으로_내_회원_정보_조회_요청(accessToken);

		// then
		회원_정보_조회됨(tokenResponse, EMAIL, AGE);
    }

	/**
	 * Given 회원 생성 후 Bearer 토큰을 요청
	 * When 비어있거나 잘못된 토큰으로 조회하면
	 * Then 400에러 발생
	 * */
	@DisplayName("내 정보 조회가 실패한다.")
	@ParameterizedTest
	@ValueSource(strings = {"", "q1w2e3r4.q5w6e7r8"})
	void getFailMyInfo(String accessToken) {
		// given
		회원_생성_요청(EMAIL, PASSWORD, AGE);
		베어러_인증_로그인_요청(EMAIL, PASSWORD);

		// when
		ExtractableResponse<Response> tokenResponse = 베이직_인증으로_내_회원_정보_조회_요청(accessToken);

		// then
		회원_정보_실패(tokenResponse);
	}
}
