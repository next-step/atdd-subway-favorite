package nextstep.subway.acceptance.favorites;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoritesSteps {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, Long sourceId, Long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(sourceId));
        params.put("target", String.valueOf(targetId));

        return RestAssured.given().log().all()
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorite")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorite")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long id) {
        return RestAssured.given().log().all()
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorite/{id}", id)
                .then().log().all().extract();
    }
}
