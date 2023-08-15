package nextstep.handler.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.StationRequest;
import org.springframework.http.MediaType;

public class StationHandler {

    public static ExtractableResponse<Response> 지하철_역_요청(StationRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when().log().all()
                .post("/stations")
                    .then().log().all()
                .extract();
    }
}