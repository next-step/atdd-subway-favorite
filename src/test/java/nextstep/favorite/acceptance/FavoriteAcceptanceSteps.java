package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceSteps {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String accessToken, Map<String, Long> params) {
        return RestAssured.given().log().all()
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .auth().oauth2(accessToken)
                   .body(params)
                   .when().post("/favorites")
                   .then().log().all()
                   .extract();
    }


    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .auth().oauth2(accessToken)
                          .when().get("/favorites")
                          .then().log().all()
                          .statusCode(HttpStatus.OK.value())
                          .extract();
    }


    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(long id, String accessToken) {
        return RestAssured.given().log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .auth().oauth2(accessToken)
                          .when().delete("/favorites/" + id)
                          .then().log().all()
                          .extract();
    }

    public static Map<String, Long> 즐겨찾기_등록_파라미터생성(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return params;
    }
}
