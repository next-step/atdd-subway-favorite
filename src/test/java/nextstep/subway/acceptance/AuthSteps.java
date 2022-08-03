package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthSteps {

    public static ExtractableResponse<Response> 폼_로그인_후_내_회원_정보_조회_요청(String email, String password) {
        return RestAssured.given().log().all().
            auth().form(email, password,
                new FormAuthConfig("/login/form", "username", "password")).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/members/me").
            then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract();
    }

    public static ExtractableResponse<Response> 베어러_인증으로_내_회원_정보_조회_요청(String accessToken) {
        return given(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 베어러_토큰_인증으로_내_회원_정보_수정(String accessToken, String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return given(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 베어러_토큰_인증으로_내_회원_정보_삭제_요청(String accessToken) {
        return given(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/members/me")
            .then().log().all()
            .extract();
    }

    public static RequestSpecification given(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token);
    }

}
