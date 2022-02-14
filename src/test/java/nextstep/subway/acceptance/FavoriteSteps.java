package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 비로그인_즐겨찾기_생성_요청(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_제거_요청(String accessToken, String url) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(url)
                .then().log().all().extract();
    }
}
