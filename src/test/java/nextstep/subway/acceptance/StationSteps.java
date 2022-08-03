package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String token, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return CommonAuthRestAssured.given(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_조회_요청() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(String token, String location) {
        return CommonAuthRestAssured.given(token)
            .when()
            .delete(location)
            .then().log().all()
            .extract();
    }
}
