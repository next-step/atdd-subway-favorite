package nextstep.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.line.LineResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class SectionSteps {
    public static ExtractableResponse<Response> 구간_생성_요청(Long lineId, Long upstationId, Long downstationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upstationId", upstationId.toString());
        params.put("downstationId", downstationId.toString());
        params.put("distance", Integer.toString(distance));

        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제_요청(Long lineId, Long stationId) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .queryParam("stationId", stationId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }
}
