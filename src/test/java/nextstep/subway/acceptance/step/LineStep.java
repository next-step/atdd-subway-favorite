package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import nextstep.subway.controller.dto.AddSectionRequest;
import nextstep.subway.controller.dto.CreateLineRequest;
import nextstep.subway.controller.dto.UpdateLineRequest;

public class LineStep {
    public static ExtractableResponse<Response> 노선을_생성한다(CreateLineRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_목록을_조회한다() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선을_조회한다(Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선을_수정한다(Long id, String name, String color) {
        UpdateLineRequest params = new UpdateLineRequest(name, color);
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선을_삭제한다(Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간을_추가한다(
            Long id,
            Long upStationId,
            Long downStationId,
            Long distance
    ) {
        return RestAssured
                .given().log().all()
                .body(new AddSectionRequest(upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + id + "/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간을_삭제한다(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();
    }
}
