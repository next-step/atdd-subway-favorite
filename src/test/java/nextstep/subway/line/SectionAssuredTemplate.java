package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import org.springframework.http.MediaType;

public class SectionAssuredTemplate {

    public static Response addSection(Long lineId, SectionRequest sectionRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .pathParam("lineId", lineId)
                .when()
                .post("/lines/{lineId}/sections");
    }

    public static Response deleteSection(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("stationId", stationId)
                .pathParam("lineId", lineId)
                .when()
                .delete("/lines/{lineId}/sections");
    }
}
