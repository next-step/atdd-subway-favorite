package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestSteps.given;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", "" + source);
        params.put("target", "" + target);
        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 토큰없이_즐겨찾기_생성_요청(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", "" + source);
        params.put("target", "" + target);
        return given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return given(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰없이_즐겨찾기_조회_요청() {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_제거_요청(String token, String location) {
        return given(token)
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰없이_즐겨찾기_제거_요청(String location) {
        return given()
                .when().delete(location)
                .then().log().all()
                .extract();
    }
}
