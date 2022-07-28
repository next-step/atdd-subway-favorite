package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.PASSWORD_FIELD;
import static nextstep.subway.acceptance.MemberSteps.USERNAME_FIELD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.베이직_인증으로_내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;
import static nextstep.subway.utils.MockMember.ADMIN;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.MockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(ADMIN);

        회원_정보_조회됨(response, ADMIN);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(ADMIN);

        회원_정보_조회됨(response, ADMIN);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(ADMIN);

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, ADMIN);
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(MockMember member) {
        return RestAssured.given().log().all()
            .auth().form(member.getEmail(), member.getPassword(),
                new FormAuthConfig("/login/form", USERNAME_FIELD, PASSWORD_FIELD))
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }
}
