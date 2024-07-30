package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class PathAssuredTemplate {

    public static Response searchShortestPath(Long sourceStationId, Long targetStationId) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", sourceStationId)
                .queryParam("target", targetStationId)
                .get("/paths");
    }
}
