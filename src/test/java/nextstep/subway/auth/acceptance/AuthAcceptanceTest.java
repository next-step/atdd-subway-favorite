package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.SubwayApplication;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.*;

@ContextConfiguration(classes = SubwayApplication.class)
public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Basic Auth를 통한 로그인 시도")
    @Test
    void loginWithBasicAuth() {

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_됨(response);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(tokenResponse);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("회원 정보 수정")
    @Test
    void updateMyInfo() {
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        int newAge = 111;
        Map<String, String> newMyInfo = 회원정보_파라미터_생성(EMAIL, PASSWORD, newAge);

        내_회원_정보_수정_요청(tokenResponse, newMyInfo);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(tokenResponse);

        회원_정보_조회됨(response, EMAIL, newAge);
    }

    private ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse, Map<String, String> newMyInfo) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(ContentType.JSON)
                .body(newMyInfo)
                .put("/members/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
