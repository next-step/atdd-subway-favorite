package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long sourceId, Long targetId, String accessToken) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(ExtractableResponse<Response> response, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get(response.header("Location"))
                .then().log().all().extract();
    }

    public static Long 즐겨찾기_조회_출발역ID_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("source.id");
    }
    public static Long 즐겨찾기_조회_도착역ID_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("target.id");
    }
}
