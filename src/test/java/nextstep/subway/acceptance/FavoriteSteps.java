package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_추가(String accessToken, Long source, Long target) {
        Map<String, Long> params = new HashMap<>();

        params.put("source", source);
        params.put("target", target);

        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 비로그인_즐겨찾기_추가(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();

        params.put("source", source);
        params.put("target", target);

        return RestAssured
                .given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회(String accessToken) {
        return given(accessToken)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private static RequestSpecification given(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken);
    }
}
