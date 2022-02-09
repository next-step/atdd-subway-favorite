package nextstep.subway.acceptance.line;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.line.application.dto.LineRequest;
import nextstep.line.application.dto.SectionRequest;
import nextstep.line.domain.Distance;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        LineRequest body = createLineRequest(name, color);
        return RestAssured
                .given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    private static LineRequest createLineRequest(String name, String color) {
        return LineRequest.builder()
            .name(name)
            .color(color)
            .build();
    }

    public static Long 지하철_노선_생성_요청_하고_ID_반환(String name, String color) {
        return 지하철_노선_생성_요청(name, color).jsonPath()
                                        .getLong("id");
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long id, String name, String color) {
        LineRequest body = createLineRequest(name, color);

        return RestAssured
            .given().log().all()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/" + id)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long id) {
        return RestAssured
            .given().log().all()
            .when().delete("/lines/" + id)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse) {
        return RestAssured
                .given().log().all()
                .when().get(createResponse.header("location"))
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest body) {
        return RestAssured
                .given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, SectionRequest params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().extract();
    }

    public static LineRequest createLineCreateParams(Long upStationId, Long downStationId) {
        return LineRequest.builder()
            .name("노선")
            .color("bg-red-600")
            .upStationId(upStationId)
            .downStationId(downStationId)
            .distance(new Distance(10))
            .build();
    }

    public static SectionRequest createSectionCreateParams(Long upStationId, Long downStationId, int distance) {
        return SectionRequest.builder()
            .upStationId(upStationId)
            .downStationId(downStationId)
            .distance(new Distance(distance))
            .build();
    }

    public static SectionRequest createSectionCreateParams(Long upStationId, Long downStationId) {
        return createSectionCreateParams(upStationId, downStationId, 6);
    }
}
