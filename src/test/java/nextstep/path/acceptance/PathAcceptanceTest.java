package nextstep.path.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.line.acceptance.LineSteps.*;
import static nextstep.station.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10L).jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10L).jsonPath().getLong("id");;
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2L).jsonPath().getLong("id");;

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3L));
    }

    /**
     * When 출발역과 도착역까지의 경로를 조회하면
     * Then 출발역으로부터 도착역까지의 경로에 있는 역 목록과 조회한 경로 구간의 거리를 반환한다.
     */
    @DisplayName("출발역과 도착역까지의 최단 경로를 조회한다.")
    @Test
    void getShortestPath() {
        // when
        var response = 지하철_최단_경로_조회_요청(교대역, 양재역);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
            assertThat(response.jsonPath().getLong("distance")).isEqualTo(5L);
        });


    }


    /**
     * When 출발역과 도착역이 같은 경로를 조회하면
     * Then 예외가 발생한다.
     */
    @DisplayName("출발역과 도착역이 같은 경우 예외가 발생한다.")
    @Test
    void getShortestPathSameStationException() {
        // when
        var response = 지하철_최단_경로_조회_요청(교대역, 교대역);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.jsonPath().getString("code")).isEqualTo("S011");
            assertThat(response.jsonPath().getString("message")).isEqualTo(" 출발역 또는 도착역이 동일합니다.");
        });

    }

    /**
     * When 출발역과 도착역까지의 경로를 조회하면
     * Then 출발역과 도착역이 연결이 되어 있지 않은 경우 예외가 발생한다.
     */
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외가 발생한다.")
    @Test
    void getShortestPathNotConnectedException() {
        // when
        var 서울역 = 지하철역_생성_요청("서울역").jsonPath().getLong("id");
        var 수원역 = 지하철역_생성_요청("서울역").jsonPath().getLong("id");
        var 일호선 = 지하철_노선_생성_요청("1호선", "blue", 서울역, 수원역, 10L).jsonPath().getLong("id");

        var response = 지하철_최단_경로_조회_요청(서울역, 양재역);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.jsonPath().getString("code")).isEqualTo("S009");
            assertThat(response.jsonPath().getString("message")).isEqualTo(" 경로를 찾을 수 없습니다.");
        });
    }

    /**
     * When 존재하지 않은 출발역으로 경로를 조회하면
     * Then 예외가 발생한다.
     */
    @DisplayName("존재하지 않은 출발역으로 경로 조회 할 경우 예외가 발생한다.")
    @Test
    void getShortestPathSourceNotExistException() {
        // when
        var 유령역 = -1L;
        var 출발역없음 = 지하철_최단_경로_조회_요청(유령역, 양재역);

        // then
        assertAll(() -> {
            assertThat(출발역없음.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(출발역없음.jsonPath().getString("code")).isEqualTo("S010");
            assertThat(출발역없음.jsonPath().getString("message")).isEqualTo(" 출발역 또는 도착역이 존재하지 않습니다.");
        });
    }

    /**
     * When 존재하지 않은 도착역을로 경로를 조회하면
     * Then 예외가 발생한다.
     */
    @DisplayName("존재하지 않은 도착역으로 경로 조회 할 경우 예외가 발생한다.")
    @Test
    void getShortestPathTargetNotExistException() {
        // when
        var 유령역 = -1L;
        var 도착역없음 = 지하철_최단_경로_조회_요청(교대역, 유령역);

        // then
        assertAll(() -> {
            assertThat(도착역없음.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(도착역없음.jsonPath().getString("code")).isEqualTo("S010");
            assertThat(도착역없음.jsonPath().getString("message")).isEqualTo(" 출발역 또는 도착역이 존재하지 않습니다.");
        });
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", 6 + "");
        return params;
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, Long distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        return params;
    }
}