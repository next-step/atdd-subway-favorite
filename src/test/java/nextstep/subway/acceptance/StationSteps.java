package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationSteps {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String token, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return AuthCommon.given(token).log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
