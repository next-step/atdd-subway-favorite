package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {

    public static StationResponse 지하철역_생성_조회(String name) {
        ExtractableResponse<Response> response = 지하철역_생성_요청(name);
        String location = response.header("Location");
        ExtractableResponse<Response> 지하철역_조회_요청 = 지하철역_조회_요청(location);
        return 지하철역_조회_요청.as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
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

    public static ExtractableResponse<Response> 지하철역_조회_요청(String uri) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

}
