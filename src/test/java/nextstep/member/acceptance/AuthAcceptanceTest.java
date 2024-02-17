package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.AcceptanceTest;
import nextstep.core.AcceptanceTestAuthBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
class AuthAcceptanceTest extends AcceptanceTestAuthBase {

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        assertThat(accessToken).isNotBlank();

        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
    }

    /**
     * When Github 로그인 요청시
     * Then 토큰을 발급받을 수 있다.
     */
    @DisplayName("Github 로그인 요청시 토큰을 발급 받을 수 있다.")
    @Test
    void githubAuthSuccess() {
        final ExtractableResponse<Response> response = 깃허브_로그인_요청("code");

        토큰_발급_성공(response);
    }

    /**
     * When Github 로그인 요청시
     * When Github 로그인 요청에 실패하면
     * Then 에러가 난다.
     */
    @DisplayName("Github 로그인 요청 실패시 에러가 난다.")
    @Test
    void githubAuthFail() {
        final ExtractableResponse<Response> response = 깃허브_로그인_요청("wrong_code");

        토큰_발급_실패(response);
    }

    private static ExtractableResponse<Response> 깃허브_로그인_요청(final String code) {
        final Map<String, String> githubAuthRequest = Map.of("code", code);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(githubAuthRequest)
                .when().post("/login/github")
                .then().log().all()
                .extract();
    }

    private static void 토큰_발급_성공(final ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    private void 토큰_발급_실패(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
