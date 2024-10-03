package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.presentation.request.LineCreateRequest;
import nextstep.subway.line.presentation.request.LineUpdateRequest;
import org.springframework.http.MediaType;

public class LineSteps {

    public static ExtractableResponse<Response> 노선_생성_요청(LineCreateRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static Long 노선_생성_요청_후_id_반환(LineCreateRequest request) {
        var extractableResponse = 노선_생성_요청(request);
        return extractableResponse.response().jsonPath().getLong("id");
    }


    public static ExtractableResponse<Response> 모든_노선_조회_요청() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
        return response;
    }

    public static ExtractableResponse<Response> 노선_조회_요청(Long lineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_수정_요청(Long lineId, LineUpdateRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(Long lineId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .extract();
    }
}
