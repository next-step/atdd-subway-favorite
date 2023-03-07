package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps extends Steps {

    public static ExtractableResponse<Response> 지하철역_생성_요청(final String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static Long 지하철역_생성함(final String name) {
        final ExtractableResponse<Response> response = 지하철역_생성_요청(name);
        응답_코드_검증(response, HttpStatus.CREATED);
        return response.jsonPath().getLong("id");
    }
}
