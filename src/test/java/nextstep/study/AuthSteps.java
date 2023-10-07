package nextstep.study;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {

    public static ExtractableResponse<Response> bearer_로그인_요청(String EMAIL, String PASSWORD) {
        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();
    }

    public static String bearer_로그인_AccessToken_추출(String EMAIL, String PASSWORD) {
        return bearer_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }
}
