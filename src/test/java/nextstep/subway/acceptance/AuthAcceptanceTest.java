package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.베이직_인증으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청_토큰_포함;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private static final String MEMBER_EMAIL = "member@email.com";
    private static final String MEMBER_PASSWORD = "memberPassword";
    private static final Integer MEMBER_AGE = 23;

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

    @DisplayName("권한 확인 성공")
    @Test
    void adminAuth() {
        // given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청_토큰_포함("신논현역", accessToken);

        // then
        assertThat(response.response().statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("권한이 존재하지 않는 경우 지하철역 생성 실패")
    @Test
    void invalidToken() {
        // given
        String accessToken = "Bearer failToken";

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청_토큰_포함("신논현역", accessToken);

        // then
        assertThat(response.response().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("관리자 권한이 아닌 토큰인 경우, 지하철역 생성 불가")
    @Test
    void notAdminAuth() {
        // given
        String accessToken = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청_토큰_포함("신논현역", accessToken);

        // then
        assertThat(response.response().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all()
                .auth().form(email, password, new FormAuthConfig("/login/form", "email", "password"))
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        return RestAssured.given().log().all()
                .headers(headers)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }
}
