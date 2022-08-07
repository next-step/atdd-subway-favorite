package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return AcceptanceTest.post("/stations", token, params);
    }

    public static ExtractableResponse<Response> 지하철역_조회_요청(String token) {
        return AcceptanceTest.get("/stations", token);
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(String url, String token) {
        return AcceptanceTest.delete(url, token);
    }
}
