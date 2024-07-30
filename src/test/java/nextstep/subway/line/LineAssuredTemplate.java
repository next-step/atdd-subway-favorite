package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.UpdateLineRequest;
import org.springframework.http.MediaType;

public class LineAssuredTemplate {

    public static Response createLine(LineRequest lineRequest) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when()
                .post("/lines");
    }

    public static Response updateLine(UpdateLineRequest updateLineRequest, Long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateLineRequest)
                .pathParam("lineId", lineId)
                .when()
                .put("/lines/{lineId}");

    }

    public static Response searchOneLine(Long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lineId", lineId)
                .when()
                .get("/lines/{lineId}");
    }

    public static Response searchAllLine() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines");
    }

    public static Response deleteOneLine(Long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("lineId", lineId)
                .when()
                .delete("/lines/{lineId}");
    }
}
