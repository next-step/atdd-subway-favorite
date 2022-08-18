package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.MemberData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청_토큰따로;
import static org.assertj.core.api.Assertions.assertThat;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = MemberData.admin.getEmail();
    private static final String PASSWORD = MemberData.admin.getPassword();
    private static final Integer AGE = MemberData.admin.getAge();

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

    @DisplayName("권한 통과")
    @Test
    void onlyAdminAuth() {
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 지하철역_생성_요청_토큰따로("서울역", accessToken);

        assertThat(response.response().statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("권한 존재하지 않는 토큰 실패")
    @Test
    void fail_onlyAdminAuth() {
        String accessToken = "Bearer invalid";

        ExtractableResponse<Response> response = 지하철역_생성_요청_토큰따로("서울역", accessToken);

        assertThat(response.response().statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("권한 부족한 토큰 실패")
    @Test
    void fail_notAdminAuth() {
        String accessToken = 로그인_되어_있음(MemberData.member.getEmail(), MemberData.member.getPassword());

        ExtractableResponse<Response> response = 지하철역_생성_요청_토큰따로("서울역", accessToken);

        assertThat(response.response().statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
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
