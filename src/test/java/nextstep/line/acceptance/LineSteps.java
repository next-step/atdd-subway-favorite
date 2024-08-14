package nextstep.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineSteps {

    public static Map<String, Object> 신분당선_생성(Long upStationId, Long downStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 7);
        return params;
    }

    public static Map<String, Object> 분당선_생성(Long upStationId, Long downStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "분당선");
        params.put("color", "bg-red-500");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 25);
        return params;
    }

    public static Map<String, String> createStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    public static ExtractableResponse<Response> 지하철_역_생성(String stationName) {
        return RestAssured.given().log().all()
                .body(createStationParams(stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
