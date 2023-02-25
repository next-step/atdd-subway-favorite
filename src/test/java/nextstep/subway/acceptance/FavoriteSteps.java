package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, String source, String target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured.given().log().all()
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return RestAssured.given().log().all()
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, String favoriteId) {
        return RestAssured.given().log().all()
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/favorites/{favoriteId}", favoriteId)
            .then().log().all()
            .extract();
    }
}
