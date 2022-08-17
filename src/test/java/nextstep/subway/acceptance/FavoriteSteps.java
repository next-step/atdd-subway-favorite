package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 지하철_노선_즐겨찾기_생성(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");
        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_즐겨찾기_조회_요청(String accessToken) {
        return given(accessToken)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_즐겨찾기_제거_요청(String accessToken, String favoriteId) {
        return given(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_즐겨찾기_제거_요청(String favoriteId) {
        return RestAssured.given().log().all()
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().extract();
    }

    private static RequestSpecification given(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token);
    }
}
