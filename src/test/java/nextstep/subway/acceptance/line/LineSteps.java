package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.utils.RestAssuredUtils.given;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, String accessToken) {
        Map<String, String> params = Map.of(
                "name", name,
                "color", color
        );
        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createResponse) {
        return RestAssured
                .given().log().all()
                .when().get(createResponse.header("location"))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/{id}", id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params, String accessToken) {
        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response, Map<String, String> params, String accessToken) {
        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(response.header("location"))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(ExtractableResponse<Response> response, String accessToken) {
        return given(accessToken)
                .when().delete(response.header("location"))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Map<String, String> params, String accessToken) {
        return given(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId, String accessToken) {
        return given(accessToken)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

}
