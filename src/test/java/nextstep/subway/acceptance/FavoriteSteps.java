package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps extends AcceptanceTestSteps {
    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();

    }

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(String token, Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();

    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token, Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorite")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_모두_조회_요청() {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_모두_조회_요청(String token) {
        return given(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long id) {
        return given(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{id}", id)
                .then().log().all().extract();
    }
}
