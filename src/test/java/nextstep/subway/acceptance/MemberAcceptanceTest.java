package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
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
     * given 회원 생성 후 token 을 받아온다.
     * when  token 으로 내 정보를 요청하면
     * then  내 email 을 받아볼 수 있다.
     */
    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> 베어러_인증_로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        String token = 베어러_인증_로그인_요청.jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 일반_인증으로_내_회원_정보_조회_요청(token);

        // then
        assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * given 회원 생성 후 token 을 받아온다.
     * when  token 으로 내 정보를 요청하면
     * then  내 email 을 받아볼 수 있다.
     */
    @DisplayName("내 정보 조회 : 실패")
    @Test
    void getMyInfo2() {
        // given
        // when
        ExtractableResponse<Response> response = 일반_인증으로_내_회원_정보_조회_요청("invalid");

        // then
        assertThat(response.body().asString()).contains("유효하지 않은 토큰입니다");
    }

}