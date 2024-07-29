package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class StationAssuredTemplate {

    public static Response createStation(String stationName) {
        Map<String, String> bodyData = Map.of("name", stationName);

        return RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bodyData)
                .when()
                .post("/stations");
    }

    public static Response deleteStation(long stationId) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", stationId)
                .delete("/stations/{id}");
    }

    public static Response showStations() {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/stations");
    }
}
