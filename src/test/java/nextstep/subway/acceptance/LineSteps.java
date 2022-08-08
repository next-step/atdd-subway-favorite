package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return AcceptanceTest.post("/lines", token, params);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String token) {
        return AcceptanceTest.get("/lines", token);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse, String token) {
        return AcceptanceTest.get(createResponse.header("location"), token);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id, String token) {
        return AcceptanceTest.get("/lines/" + id, token);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, Object> params, String token) {
        return AcceptanceTest.post("/lines", token, params);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Map<String, Object> params, String token) {
        return AcceptanceTest.post("/lines/" + lineId + "/sections", token, params);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId, String token) {
        return AcceptanceTest.delete("/lines/" + lineId + "/sections?stationId=" + stationId, token);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String url, String token, Map<String, Object> params) {
        return AcceptanceTest.put(url, token, params);
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String url, String token) {
        return AcceptanceTest.delete(url, token);
    }
}
