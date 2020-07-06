package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        // when
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId = createdStationResponse.as(StationResponse.class).getId();
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", "");
        params.put("stationId", stationId + "");
        params.put("distance", "4");
        params.put("duration", "2");

        ExtractableResponse<Response> response = RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId = createdStationResponse.as(StationResponse.class).getId();
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", "");
        params.put("stationId", stationId + "");
        params.put("distance", "4");
        params.put("duration", "2");

        RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/{lineId}", lineId).
                then().
                log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(1);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", "");
        params.put("stationId", stationId1 + "");
        params.put("distance", "4");
        params.put("duration", "2");

        ExtractableResponse<Response> lineStationResponse = RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        Map<String, String> params2 = new HashMap<>();
        params2.put("preStationId", stationId1 + "");
        params2.put("stationId", stationId2 + "");
        params2.put("distance", "4");
        params2.put("duration", "2");

        RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params2).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        Map<String, String> params3 = new HashMap<>();
        params3.put("preStationId", stationId2 + "");
        params3.put("stationId", stationId3 + "");
        params3.put("distance", "4");
        params3.put("duration", "2");

        RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params3).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();

        // then
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/{lineId}", lineId).
                then().
                log().all().
                extract();

        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 2L, 3L));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        // 지하철_노선에_지하철역_등록_요청
        Long lineId = createdLineResponse.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", "");
        params.put("stationId", stationId1 + "");
        params.put("distance", "4");
        params.put("duration", "2");

        ExtractableResponse<Response> lineStationResponse = RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();

        // 지하철_노선에_지하철역_등록_요청
        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        Map<String, String> params2 = new HashMap<>();
        params.put("preStationId", stationId1 + "");
        params.put("stationId", stationId2 + "");
        params.put("distance", "4");
        params.put("duration", "2");

        RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params2).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();

        // 지하철_노선에_지하철역_등록_요청
        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        Map<String, String> params3 = new HashMap<>();
        params.put("preStationId", stationId1 + "");
        params.put("stationId", stationId3 + "");
        params.put("distance", "4");
        params.put("duration", "2");

        RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params3).
                when().
                post("/lines/{lineId}/stations", lineId).
                then().
                log().all().
                extract();

        // then
        assertThat(lineStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/{lineId}", lineId).
                then().
                log().all().
                extract();

        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getStations()).hasSize(3);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(it -> it.getStation().getId())
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 3L, 2L));
    }

    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines").
                then().
                log().all().
                extract();
    }
}
