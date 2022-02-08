package nextstep.subway.station.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.commons.RestAssuredUtils;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.commons.RestAssuredUtils.delete;

public class StationUtils {

    private StationUtils() {}

    public static long getStationId(ExtractableResponse<Response> response) {
        return response.as(StationResponse.class).getId();
    }

    public static ExtractableResponse<Response> 지하철역_생성요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(long id) {
        return delete("/stations/" + id);
    }
}
