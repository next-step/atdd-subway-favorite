package nextstep.subway.station.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

public class StationSteps {

    public static Long 역_생성_요청(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract()
                .response().jsonPath().getLong("id");
    }

    public static List<String> 모든_역_조회_요청() {
        List<String> stationNames = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract()
                .response().jsonPath().getList("name", String.class);
        return stationNames;
    }

    public static Long 역_조회_요청(ExtractableResponse<Response> station) {
        return station.response().jsonPath().getLong("id");
    }

    public static Response 역_삭제_요청(String id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract()
                .response();
    }
}
