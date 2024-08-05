package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AbstractIntegerAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public final class AcceptanceTestUtil {
    public static ExtractableResponse<Response> 노선_생성_Extract(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_조회_Extract(long lineId) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선에_새로운_구간_추가_Extract(Map<String, Object> newSection, long lineId) {
        return RestAssured.given().log().all()
                .body(newSection)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    public static Long 역_생성_후_id_추출(String stationName) {
        return 역_생성(stationName).jsonPath().getLong("id");
    }

    public static Long 노선_생성_후_id_추출(String name,
                              String color,
                              Long upStationId,
                              Long downStationId,
                              Long distance) {
        ExtractableResponse<Response> 노선_생성_응답 = 노선_생성_Extract(노선_생성_매개변수(name, color, upStationId, downStationId, distance));
        return 노선_생성_응답.jsonPath().getLong("id");
    }

    public static Long 노선_생성_후_id_추출(Map<String, Object> 노선_생성_매개변수) {
        ExtractableResponse<Response> 노선_생성_응답 = 노선_생성_Extract(노선_생성_매개변수);
        return 노선_생성_응답.jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> 역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static Map<String, Object> 노선_생성_매개변수(
            String name,
            String color,
            Long upStationId,
            Long downStationId,
            Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    public static Map<String, Object> 구간_생성_매개변수(
            Long upStationId,
            Long downStationId,
            Long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    public static AbstractIntegerAssert<?> 권한_없음(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static AbstractIntegerAssert<?> 정상_응답(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static AbstractIntegerAssert<?> 컨텐츠_없음(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static AbstractIntegerAssert<?> 정상_생성됨(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static AbstractIntegerAssert<?> 존재하지_않는_자원(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static AbstractIntegerAssert<?> 잘못된_요청(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private AcceptanceTestUtil() {
    }
}
