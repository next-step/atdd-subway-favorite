package nextstep.utils.api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationApi {
    public static JsonPath 지하철역_생성요청(Map<String, String> stationRequestBody) {
        return RestAssured.given().log().all()
                .body(stationRequestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath();
    }
}
