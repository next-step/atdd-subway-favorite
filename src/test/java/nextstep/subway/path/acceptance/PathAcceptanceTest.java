package nextstep.subway.path.acceptance;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineStationAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {
    private Long lineId1;
    private Long lineId2;
    private Long lineId3;
    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long stationId4;

    /**
     * 교대역      -      강남역
     * |                 |
     * 남부터미널역           |
     * |                 |
     * 양재역      -       -|
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("교대역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("양재역");
        ExtractableResponse<Response> createdStationResponse4 = 지하철역_등록되어_있음("남부터미널");
        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_등록되어_있음("신분당선", "RED");
        ExtractableResponse<Response> createLineResponse3 = 지하철_노선_등록되어_있음("3호선", "ORANGE");

        lineId1 = createLineResponse1.as(LineResponse.class).getId();
        lineId2 = createLineResponse2.as(LineResponse.class).getId();
        lineId3 = createLineResponse3.as(LineResponse.class).getId();
        stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        stationId4 = createdStationResponse4.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록되어_있음(lineId1, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId1, stationId2, 2, 2);
        지하철_노선에_지하철역_등록되어_있음(lineId2, null, stationId2, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId2, stationId3, 2, 1);
        지하철_노선에_지하철역_등록되어_있음(lineId3, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId1, stationId4, 1, 2);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId4, stationId3, 2, 2);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // 두_역의_최단_거리_경로_조회를_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DISTANCE").
                then().
                log().all().
                extract();

        // 최단_거리_경로_응답됨
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(3);
        assertThat(pathResponse.getDuration()).isEqualTo(4);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 4L, 3L));
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        // 두_역의_최단_거리_경로_조회를_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DURATION").
                then().
                log().all().
                extract();

        // 최단_거리_경로_응답됨
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(4);
        assertThat(pathResponse.getDuration()).isEqualTo(3);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 2L, 3L));
    }
}
