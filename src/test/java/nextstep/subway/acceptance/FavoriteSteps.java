package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    private static final String ROOT_PATH = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(
            String accessToken,
            Long source,
            Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return RestAssured.given()
                .log().all()
                .auth().preemptive().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(ROOT_PATH)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured.given()
                .log().all()
                .auth().preemptive().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ROOT_PATH)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String resourceLocation) {
        return RestAssured.given()
                .log().all()
                .auth().preemptive().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(resourceLocation)
                .then()
                .log().all()
                .extract();
    }
}
