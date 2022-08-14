package nextstep.subway.acceptance.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RestAssuredUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.member.MemberSteps.어드민_로그인_되어_있음;
import static nextstep.subway.utils.RestAssuredUtils.securedGiven;

public class LineSteps {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return securedGiven(어드민_로그인_되어_있음())
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
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

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return securedGiven(어드민_로그인_되어_있음())
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> response, Map<String, String> params) {
        return securedGiven(어드민_로그인_되어_있음())
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(response.header("location"))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(ExtractableResponse<Response> response) {
        return securedGiven(어드민_로그인_되어_있음())
                .when().delete(response.header("location"))
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Map<String, String> params) {
        return securedGiven(어드민_로그인_되어_있음())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철_구간_제거_요청(Long lineId, Long stationId) {
        return securedGiven(어드민_로그인_되어_있음())
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all().extract();
    }
}
