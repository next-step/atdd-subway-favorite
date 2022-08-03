package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.DataLoader.*;
import static nextstep.subway.acceptance.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.acceptance.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.*;


class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DataLoader dataLoader;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        dataLoader.loadData();
    }

    @DisplayName("Basic Auth")
    @Test
    void myInfoWithBasicAuth() {
        ExtractableResponse<Response> response = 베이직_인증으로_내_회원_정보_조회_요청(MEMBER_EMAIL, MEMBER_PASSWORD);

        회원_정보_조회됨(response, MEMBER_EMAIL, MEMBER_AGE);
    }

    @DisplayName("폼 로그인 후 내 정보 조회")
    @Test
    void myInfoWithForm() {
        ExtractableResponse<Response> response = 폼_로그인_후_내_회원_정보_조회_요청(MEMBER_EMAIL, MEMBER_PASSWORD);

        회원_정보_조회됨(response, MEMBER_EMAIL, MEMBER_AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD);

        ExtractableResponse<Response> response = 베어러_인증으로_내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, MEMBER_EMAIL, MEMBER_AGE);
    }

    private ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all().
                auth().form(email, password,
                        new FormAuthConfig("/login/form", USERNAME_FIELD, PASSWORD_FIELD)).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract();
    }

    private ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all().
                auth().oauth2(accessToken).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract();
    }
}
