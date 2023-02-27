package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {

    public static ExtractableResponse<Response> 깃허브_권한증서_로그인_요청(final String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
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
}
