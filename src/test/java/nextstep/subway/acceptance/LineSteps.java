package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.ADMIN_토큰권한으로_호출;
import static nextstep.subway.acceptance.AuthSteps.MEMBER_토큰권한으로_호출;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return ADMIN_토큰권한으로_호출()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return MEMBER_토큰권한으로_호출()
                .when()
                .get("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse) {
        return MEMBER_토큰권한으로_호출()
                .when()
                .get(createResponse.header("location"))
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return MEMBER_토큰권한으로_호출()
                .when()
                .get("/lines/{id}", id)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return ADMIN_토큰권한으로_호출()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Map<String, String> params) {
        return ADMIN_토큰권한으로_호출()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/lines/{lineId}/sections", lineId)
                .then()
                .log()
                .all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId) {
        return ADMIN_토큰권한으로_호출()
                .when()
                .delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then()
                .log()
                .all()
                .extract();
    }
}
