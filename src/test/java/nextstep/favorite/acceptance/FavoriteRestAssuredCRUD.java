package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteRestAssuredCRUD {

    public static ExtractableResponse<Response> createFavorite(Long sourceId, Long targetId, String accessToken) {
        Map<String, Object> param = new HashMap<>();
        param.put("source", sourceId);
        param.put("target", targetId);

        return RestAssured
                .given().log().all()
                    .auth().oauth2(accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(param)
                .when()
                    .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findFavorite(String accessToken) {
        return RestAssured
                .given().log().all()
                    .auth().oauth2(accessToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/favorites")
                .then().log().all()
                .extract();
    }
}
