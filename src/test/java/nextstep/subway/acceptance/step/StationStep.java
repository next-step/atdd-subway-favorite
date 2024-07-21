package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class StationStep {
    public static ExtractableResponse<Response> 시청역을_생성한다() {
        return 역을_생성한다("시청역");
    }

    public static ExtractableResponse<Response> 서울역을_생성한다() {
        return 역을_생성한다("서울역");
    }

    public static ExtractableResponse<Response> 용산역을_생성한다() {
        return 역을_생성한다("용산역");
    }

    public static ExtractableResponse<Response> 구로역을_생성한다() {
        return 역을_생성한다("구로역");
    }

    public static ExtractableResponse<Response> 역삼역을_생성한다() {
        return 역을_생성한다("역삼역");
    }

    public static ExtractableResponse<Response> 잠실역을_생성한다() {
        return 역을_생성한다("잠실역");
    }

    public static ExtractableResponse<Response> 역을_생성한다(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역_목록을_조회한다() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 역을_삭제한다(Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }
}
