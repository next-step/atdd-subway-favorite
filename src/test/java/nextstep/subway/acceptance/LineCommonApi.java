package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.line.dto.LineCreateRequest;
import nextstep.subway.domain.line.dto.LineUpdateRequest;
import nextstep.subway.domain.section.dto.SectionCreateRequest;
import org.springframework.http.MediaType;

public class LineCommonApi {
    public static ExtractableResponse<Response> createLine(LineCreateRequest request) {
        return RestAssured.given().log().all()
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findLineById(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateLine(Long id, LineUpdateRequest request) {
        return RestAssured.given().log().all()
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> addSection(Long lineId, SectionCreateRequest request) {
        return RestAssured.given().log().all()
                .body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .queryParam("stationId", stationId)
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
