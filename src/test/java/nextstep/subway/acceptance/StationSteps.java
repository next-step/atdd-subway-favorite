package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationSteps extends Steps {

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_요청() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static List<String> 지하철역_목록_요청함() {
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
        final List<String> stationNames = response.jsonPath().getList("name", String.class);
        return stationNames;
    }

    public static ExtractableResponse<Response> 지하철역_삭제_요청(String location, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .delete(location)
                .then().log().all()
                .extract();
    }
}
