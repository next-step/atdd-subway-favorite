package nextstep.favorties.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성(String accessToken, Long 교대역, Long 양재역) {
        Map<String, String> params = new HashMap<>();
        params.put("source", 교대역.toString());
        params.put("target", 양재역.toString());

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }
}
