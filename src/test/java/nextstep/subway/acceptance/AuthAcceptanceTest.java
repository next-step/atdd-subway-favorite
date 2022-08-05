package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.AcceptanceSteps.given;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인증 관리 기능")
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final String WRONG_EMAIL = "wrong@email.com";
    private static final String WRONG_TOKEN = "wrongToken";

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Wrong Basic Auth")
    @Test
    void myInfoWithBasicAuth_wrongEmail() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(WRONG_EMAIL, PASSWORD);

        인증정보가_잘못됨(response);
    }

    @DisplayName("Session 로그인 성공 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        ExtractableResponse<Response> response = 폼_로그인_요청_후_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Session 로그인 실패 후 내 정보 조회")
    @Test
    void myInfoWithSession_wrongEmail() {
        ExtractableResponse<Response> response = 폼_로그인_요청_후_내_회원_정보_조회_요청(WRONG_EMAIL, PASSWORD);

        권한이_없음(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Wrong Bearer Auth")
    @Test
    void myInfoWithBearerAuth_wrongToken() {
        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(WRONG_TOKEN);

        인증정보가_잘못됨(response);
    }

    private ExtractableResponse<Response> 폼_로그인_요청_후_내_회원_정보_조회_요청(String email, String password) {
        return given(email, password, "/login/form")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return given(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private void 인증정보가_잘못됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 권한이_없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
