package nextstep.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
public class LoginSteps {

    @DisplayName("Fake Github Auth")
    @Test
    public static ExtractableResponse<Response> 가짜_깃허브_권한증서_로그인(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/github/login")
                .then().log().all()
                .extract();

        return response;
    }

    @DisplayName("Fake Token 인증")
    public static ExtractableResponse<Response> 가짜_베어러_인증_내_정보_조회(String token) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .given().header(HttpHeaders.AUTHORIZATION, token)
                .when().get("/github/members/me")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 베어러_인증_로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    public static ExtractableResponse<Response> 베어러_인증_내_정보_조회(String token) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .given().header(HttpHeaders.AUTHORIZATION, token)
                .when().get("/members/me")
                .then().log().all().extract();
    }


}
