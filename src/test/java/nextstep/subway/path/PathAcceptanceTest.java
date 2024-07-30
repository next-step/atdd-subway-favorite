package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationFixtures;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.LineAssuredTemplate.*;
import static nextstep.subway.line.SectionAssuredTemplate.*;
import static nextstep.subway.station.StationAssuredTemplate.*;

@DisplayName("경로 조회 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    private long 논현역_id;
    private long 양재역_id;
    private long 고속터미널역_id;
    private long 교대역_id;

    @BeforeEach
    void setUp() {
        long 강남역_id = createStationWithId(StationFixtures.강남역.getName());
        this.양재역_id = createStationWithId(StationFixtures.양재역.getName());
        this.논현역_id = createStationWithId(StationFixtures.논현역.getName());
        this.고속터미널역_id = createStationWithId(StationFixtures.고속터미널역.getName());
        this.교대역_id = createStationWithId(StationFixtures.교대역.getName());

        long 신분당선_id = createLine(new LineRequest("신분당선", "green", 논현역_id, 강남역_id, 4L)).then().extract().jsonPath().getLong("id");
        addSection(신분당선_id, new SectionRequest(강남역_id, 양재역_id, 3L));

        long 삼호선_id = createLine(new LineRequest("3호선", "orange", 논현역_id, 고속터미널역_id, 2L)).then().extract().jsonPath().getLong("id");
        addSection(삼호선_id, new SectionRequest(고속터미널역_id, 교대역_id, 1L));
        addSection(삼호선_id, new SectionRequest(교대역_id, 양재역_id, 3L));
    }

    /**
     * Given 필요한 역과, 구간, 노선을 등록합니다.
     * When source 에서 sink 역으로 갈 수 있는 길을 조회합니다.
     * Then source 부터 sink 까지의 가장 빠른 길의 역들과 총 거리를 응답받습니다.
     */
    @DisplayName("가장 빠른 길을 조회합니다.")
    @Test
    void shortest() {
        // given
        // when
        ExtractableResponse<Response> result = PathAssuredTemplate.searchShortestPath(논현역_id, 양재역_id)
                .then().log().all()
                .extract();

        // then
        Assertions.assertThat(result.jsonPath().getList("stations")).hasSize(4)
                .extracting("id", "name")
                .contains(
                        Tuple.tuple((int) 논현역_id, StationFixtures.논현역.getName()),
                        Tuple.tuple((int) 고속터미널역_id, StationFixtures.고속터미널역.getName()),
                        Tuple.tuple((int) 교대역_id, StationFixtures.교대역.getName()),
                        Tuple.tuple((int) 양재역_id, StationFixtures.양재역.getName())
                );

        Assertions.assertThat(result.jsonPath().getLong("distance")).isEqualTo(6);
    }

    /**
     * Given 필요한 역과, 구간, 노선을 등록합니다.
     * When 서로 연결되어 있지 않은 역의 최단거리를 요청합니다.
     * Then 에러 응답을 전달받습니다.
     */
    @DisplayName("서로 연결되어 있지 않는 역의 최단거리는 조회할 수 없습니다.")
    @Test
    void noConnected() {
        // given
        long 사당역_id = createStationWithId(StationFixtures.사당역.getName());
        // when
        ExtractableResponse<Response> result = PathAssuredTemplate.searchShortestPath(사당역_id, 양재역_id)
                .then().log().all()
                .extract();
        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(result.body().asString()).isEqualTo(SubwayErrorMessage.NOT_CONNECTED_STATION.getMessage());
    }

    /**
     * Given 필요한 역과, 구간, 노선을 등록합니다.
     * When 존재하지 않은 역의 최단거리를 요청합니다.
     * Then 에러 응답을 전달받습니다.
     */
    @DisplayName("존재하지 않는 역이라면 최단 거리를 조회할 수 없습니다.")
    @Test
    void noStation() {
        // given
        // when
        ExtractableResponse<Response> result = PathAssuredTemplate.searchShortestPath(Long.MAX_VALUE, 양재역_id)
                .then().log().all()
                .extract();
        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(result.body().asString()).isEqualTo(SubwayErrorMessage.NO_STATION_EXIST.getMessage());
    }
}
