package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성요청(Long source, Long target, String accessToken) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제요청(Long id, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/"+id)
                .then().log().all().extract();
    }
}
