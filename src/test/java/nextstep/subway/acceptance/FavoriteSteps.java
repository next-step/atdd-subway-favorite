package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 지하철_즐겨찾기_등록(String accessToken, Long sourceStationId, Long targetStationId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceStationId);
        params.put("target", targetStationId);

        ExtractableResponse<Response> response = RestAssured
                .given().header("authorization", "Bearer "+ accessToken)
                .log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().statusCode(HttpStatus.CREATED.value())
                .log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 지하철_즐겨찾기_조회(String accessToken) {
        ExtractableResponse<Response> response = RestAssured
                .given().header("authorization", "Bearer "+ accessToken)
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().statusCode(HttpStatus.OK.value())
                .log().all().extract();
        return response;
    }

    public static  ExtractableResponse<Response> 지하철_즐겨찾기_삭제(String accessToken, Long favoriteId) {
        ExtractableResponse<Response> response = RestAssured
                .given().header("authorization", "Bearer "+ accessToken)
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().statusCode(HttpStatus.NO_CONTENT.value())
                .log().all().extract();
        return response;
    }
}
