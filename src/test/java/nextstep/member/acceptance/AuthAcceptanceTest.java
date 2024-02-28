package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.GithubResponses;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
@Transactional
class AuthAcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }


    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);

        Map<String, Object> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        String accessToken = MemberSteps.토큰_생성(params);
        assertThat(accessToken).isNotBlank();

        ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }


    @DisplayName("깃허브로 로그인이 가능하다")
    @Test
    public void githubAuth() {

        MemberSteps.회원_생성_요청(GithubResponses.USER_A.getEmail(), "pw", 12);
        String actualToken = MemberSteps.토큰_생성(GithubResponses.USER_A.getEmail(), "pw");

        ExtractableResponse<Response> response = 깃허브_로그인_요청(GithubResponses.USER_A.getCode(), HttpStatus.OK);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Test
    @DisplayName("깃허브로 로그인 시 회원정보가 존재하지 않으면 회원가입 후 토큰이 발행된다")
    public void githubAuth_AfterLogin() {

        ExtractableResponse<Response> response = 깃허브_로그인_요청(GithubResponses.USER_A.getCode(), HttpStatus.OK);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Test
    @DisplayName("깃허브의 인증을 받지 못하면 토큰 발행이 불가능하다")
    public void githubAuth_ShouldFailUnauthorizedUser() {

        ExtractableResponse<Response> response = 깃허브_로그인_요청(GithubResponses.UNAUTHORIZED_USER.getCode(), HttpStatus.UNAUTHORIZED);

        assertThat(response.jsonPath().getString("accessToken")).isNull();
    }


    private static ExtractableResponse<Response> 깃허브_로그인_요청(String code, HttpStatus status) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(status.value()).extract();
        return response;
    }



}