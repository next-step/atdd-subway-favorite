package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(Long 출발역, Long 도착역, String token) {
        return AcceptanceTest.post("/favorites", token, createFavoriteParams(출발역, 도착역));
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return AcceptanceTest.get("/favorites", token);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String url, String token) {
        return AcceptanceTest.delete(url, token);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long 즐겨찾기식별값, String token) {
        return AcceptanceTest.delete("/favorites/" + 즐겨찾기식별값, token);
    }

    public static Map<String, Object> createFavoriteParams(Long 출발역, Long 도착역) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", 출발역);
        params.put("target", 도착역);
        return params;
    }
}
