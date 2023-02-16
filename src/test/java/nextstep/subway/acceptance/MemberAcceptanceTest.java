package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.config.message.AuthError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    private static final String 토큰_비어_있음 = "";
    private static final String 유효한_토큰이_아님 = "1q2w3e4r5t";

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

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        final ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        final String 로그인_토큰 = 로그인_되어_있음(response);

        final ExtractableResponse<Response> 내_회원_정보_조회_응답 = 베어러_인증으로_내_회원_정보_조회_요청(로그인_토큰);

        내_회원_정보_조회_성공(내_회원_정보_조회_응답, EMAIL, AGE);
    }

    @DisplayName("토큰이 없어서 내 정보 조회의 실패한다.")
    @Test
    void error_getMyInfo() {

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        베어러_인증_로그인_요청(EMAIL, PASSWORD);

        final ExtractableResponse<Response> 내_회원_정보_조회_응답 = 베어러_인증으로_내_회원_정보_조회_요청(토큰_비어_있음);

        내_회원_정보_조회_실패(내_회원_정보_조회_응답, HttpStatus.UNAUTHORIZED, AuthError.NOT_MISSING_TOKEN);
    }

    @DisplayName("유효한 토큰이 아니어서 내 정보 조회의 실패한다.")
    @Test
    void error_getMyInfo2() {

        회원_생성_요청(EMAIL, PASSWORD, AGE);

        베어러_인증_로그인_요청(EMAIL, PASSWORD);

        final ExtractableResponse<Response> 내_회원_정보_조회_응답 = 베어러_인증으로_내_회원_정보_조회_요청(유효한_토큰이_아님);

        내_회원_정보_조회_실패(내_회원_정보_조회_응답, HttpStatus.UNAUTHORIZED, AuthError.NOT_VALID_TOKEN);
    }
}