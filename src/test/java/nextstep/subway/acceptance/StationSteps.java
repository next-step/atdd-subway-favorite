package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.MediaType;

public class StationSteps {
    public static StationResponse 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured
            .given()
            .body(Map.of("name", name))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().extract().as(StationResponse.class);
    }
}
