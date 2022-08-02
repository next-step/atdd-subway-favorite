package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceSteps.given;

public class StationSteps {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String token, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
