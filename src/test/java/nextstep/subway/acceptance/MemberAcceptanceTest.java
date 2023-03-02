package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberAcceptanceTest extends AcceptanceTest {
    private static final int AGE = 20;

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
        ExtractableResponse<Response> response = 회원_정보_조회_요청(관리자_토큰, createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(관리자_토큰, createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(관리자_토큰, createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 회원 가입 후 로그인을 하고
     * When  로그인 때 받은 토큰으로 내 정보 조회 요청하면
     * Then  내 정보를 조회할 수 있다.
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // when
        var token = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        var response = 토큰_인증으로_내_회원_정보_조회_요청(token);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
            assertThat(response.jsonPath().getInt("age")).isEqualTo(AGE);
        });
    }

    /**
     * When 올바르지 않은 토큰 값을 주면
     * Then 오류가 발생한다.
     */
    @DisplayName("올바르지 않은 토큰으로 조회한다.")
    @Test
    void getMyInfo_WithUnAuthorizedToken() {
        // when
        var response = 토큰_인증으로_내_회원_정보_조회_요청("invalid token");

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        });
    }
}
