package nextstep.subway.testhelper.apicaller;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class TokenApiCaller {

    public static ExtractableResponse<Response> 깃허브_로그인(Map<String, String> params) {
        return given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/github")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
