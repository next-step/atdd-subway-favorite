package nextstep.auth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthSteps {

    private AuthSteps() {}

    public static void access_token_응답을_받음(ExtractableResponse<Response> response) {
        이메일_응답_받음(response, "accessToken");
    }

    public static ExtractableResponse<Response> 토큰_로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }

    public static ExtractableResponse<Response> 깃허브_로그인_요청(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }

    public static void oauth2_깃허브_토큰_응답_받음(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("access_token")).isNotBlank();
        assertThat(response.jsonPath().getString("scope")).isNotBlank();
        assertThat(response.jsonPath().getString("token_type")).isNotBlank();
    }

    // Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
    public static ExtractableResponse<Response> oauth2_깃허브에_토큰_요청(String tokenUrl, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", "test_id");
        params.put("client_secret", "test_secret");

        // given
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(params)
                .when().post(tokenUrl)
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }

    public static void 이메일_응답_받음(ExtractableResponse<Response> response, String email) {
        assertThat(response.jsonPath().getString(email)).isNotBlank();
    }


    // Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
    public static ExtractableResponse<Response> oauth2_깃허브_리소스_조회_요청(String profileUrl, String accessToken) {
        Map<String, String> headers = Map.of(
                "Accept", "application/vnd.github+json",
                "Authorization", "Bearer " + accessToken,
                "X-GitHub-Api-Version", "2022-11-28"
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .headers(headers)
                .accept(ContentType.JSON)
                .when().get(profileUrl)
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
        return response;
    }
}
