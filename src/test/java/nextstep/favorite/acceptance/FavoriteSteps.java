package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 지하철_좋아요_생성(Long sourceId, Long targetId, String token) {
        final Map<String, String> params = new HashMap<>();
        params.put("source", sourceId.toString());
        params.put("target", targetId.toString());

        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_좋아요_조회(String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().get("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_좋아요_삭제(Long favoriteId, String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().delete("/favorites/{id}", favoriteId)
            .then().log().all().extract();
    }
}
