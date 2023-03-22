package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_API(String token, Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured.given()
            .header("Authorization", String.format("Bearer %s", token))
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .body(params)
            .post("/favorites")
            .then()
            .log()
            .all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_API(String token, Long favoriteId) {
        return RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .log()
            .all()
            .when()
            .get("/favorites/{favoriteId}", favoriteId)
            .then()
            .log()
            .all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_API(String token, long favoriteId) {

        return RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .log()
            .all()
            .when()
            .delete("/favorites/{favoriteId}", favoriteId)
            .then()
            .log()
            .all()
            .extract();
    }

}
