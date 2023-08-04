package nextstep.auth.token.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TokenSteps {

    public static String 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/token")
            .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.as(TokenResponse.class).getAccessToken();
    }

    public static String 가짜_깃헙_토큰_요청() {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(
            "code",
            "test_id",
            "test_secret"
        );

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(githubAccessTokenRequest)
            .when().post("/github/login/oauth/access_token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(GithubAccessTokenResponse.class)
            .getAccessToken();
    }

    public static GithubProfileResponse 가짜_깃헙_프로필_요청(String token) {

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/github/user")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        return new GithubProfileResponse(
            response.jsonPath().getString("email"),
            response.jsonPath().getInt("age")
        );
    }

    public static void 토큰_검증_통과(JwtTokenProvider jwtTokenProvider, String token) {
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    public static void 가짜_깃헙_토큰_검증(String accessToken, String githubToken) {
        assertThat(accessToken).isEqualTo(githubToken);
    }

    public static void 가짜_깃헙_프로필_검증(
        String email,
        int age,
        GithubProfileResponse githubProfileResponse
    ) {
        assertThat(email).isEqualTo(githubProfileResponse.getEmail());
        assertThat(age).isEqualTo(githubProfileResponse.getAge());
    }
}
