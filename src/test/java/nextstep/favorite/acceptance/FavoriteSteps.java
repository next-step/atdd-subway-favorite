package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    // 지하철역 추가
    // 노선 추가
    // 구간 추가
    // 지하철역 추가
    // 노선 추가
    // 구간 추가
    public static void 신분당선_3호선_만들기() {
        Long 강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        Long 양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        Long 양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").jsonPath().getLong("id");
        Long 판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        Long 신사역 = 지하철역_생성_요청("신사역").jsonPath().getLong("id");
        Long 잠원역 = 지하철역_생성_요청("잠원역").jsonPath().getLong("id");
        Long 고속터미널역 = 지하철역_생성_요청("고속터미널역").jsonPath().getLong("id");
        Long 교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        Long 남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        Long 신분당선 = 지하철_노선_생성_요청("신분당선", "RED", 강남역, 양재역, 10).jsonPath().getLong("id");
        Long 삼호선 = 지하철_노선_생성_요청("3호선", "YELLOW", 신사역, 잠원역, 10).jsonPath().getLong("id");
        지하철_구간_등록_요청(신분당선, 강남역, 양재역, 10);
        지하철_구간_등록_요청(신분당선, 양재시민의숲역, 양재역, 10);
        지하철_구간_등록_요청(신분당선, 양재역, 판교역, 10);
        지하철_구간_등록_요청(삼호선, 신사역, 잠원역, 10);
        지하철_구간_등록_요청(삼호선, 잠원역, 고속터미널역, 10);
        지하철_구간_등록_요청(삼호선, 고속터미널역, 교대역, 10);
        지하철_구간_등록_요청(삼호선, 교대역, 남부터미널역, 10);
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

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color,
        Long upStationId, Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(Long lineId, Long upStationId,
        Long downStationId, int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 회원_생성_요청(String email, String password,
        Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/members")
            .then().log().all().extract();
    }

}
