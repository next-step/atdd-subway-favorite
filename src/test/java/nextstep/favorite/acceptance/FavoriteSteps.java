package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import org.springframework.http.MediaType;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long sourceId, Long targetId, String accessToken) {
            HashMap<String, Long> params = new HashMap<>();
            params.put("source", sourceId);
            params.put("target", targetId);

            return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .auth().oauth2(accessToken)
            .body(params)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }
}
