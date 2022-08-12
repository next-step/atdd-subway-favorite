package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationSteps extends AuthSteps {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String token, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return given(token).log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(String token, String location, String name) {
        return given(token).log().all()
                .when()
                .delete(location)
                .then().log().all()
                .extract();
    }
}
