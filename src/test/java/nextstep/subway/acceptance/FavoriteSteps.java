package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target){
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured
                .given()
                    .log()
                    .all()
                    .header("authrization", "Bearer " + token)
                .when()
                    .body(params)
                    .get("/favorites")
                .then()
                    .log()
                    .all()
                    .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token){
        return RestAssured
                .given()
                .log()
                .all()
                .header("authrization", "Bearer " + token)
                .when()
                .get("/favorites")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long favoriteId){
        return RestAssured
                .given()
                    .log()
                    .all()
                    .header("authrization", "Bearer " + token)
                .when()
                    .delete("/favorites/{id}", favoriteId)
                .then()
                    .log()
                    .all()
                    .extract();
    }
}
