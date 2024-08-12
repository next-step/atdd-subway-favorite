package nextstep.subway.acceptance;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.dto.LineCreateRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.dto.SectionCreateRequest;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 경로 탐색 인수 테스트")
public class PathAcceptanceTest extends BasicAcceptanceTest{

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 역삼역;
    private Long 남부터미널역;
    private Long 석남역;
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

        교대역 = StationCommonApi.createStation("교대역").jsonPath().getLong("id");
        강남역 = StationCommonApi.createStation("강남역").jsonPath().getLong("id");
        역삼역 = StationCommonApi.createStation("역삼역").jsonPath().getLong("id");
        양재역 = StationCommonApi.createStation("양재역").jsonPath().getLong("id");
        남부터미널역 = StationCommonApi.createStation("남부터미널역").jsonPath().getLong("id");
        석남역 = StationCommonApi.createStation("석남역").jsonPath().getLong("id");

        이호선 = LineCommonApi.createLine(new LineCreateRequest("2호선", "green", 교대역, 강남역, 10)).jsonPath().getLong("id");
        신분당선 = LineCommonApi.createLine(new LineCreateRequest("신분당선", "red", 강남역, 양재역, 4)).jsonPath().getLong("id");
        삼호선 = LineCommonApi.createLine(new LineCreateRequest("3호선", "orange", 교대역, 남부터미널역, 6)).jsonPath().getLong("id");

        LineCommonApi.addSection(이호선, new SectionCreateRequest(강남역, 역삼역, 2));
        LineCommonApi.addSection(삼호선, new SectionCreateRequest(남부터미널역, 양재역, 3));
    }

    @DisplayName("경로 탐색 성공 케이스")
    @Nested
    class successCase {
        @DisplayName("지하철 경로 탐색에 성공한다.")
        @Test
        void successFindPath() {
            //given

            //when
            var response = PathCommonApi.findLinePath(남부터미널역, 역삼역);

            //then
            assertThat(response.jsonPath().getList("stations.name", String.class)).containsExactly("남부터미널역", "양재역", "강남역", "역삼역");
            assertThat(response.jsonPath().getLong("distance")).isEqualTo(9);
        }
    }

    @DisplayName("경로 탐색 실패 케이스")
    @Nested
    class failCase {
        @DisplayName("출발역과 도착역이 같아 지하철 경로 탐색에 실패한다.")
        @Test
        void failFindPathWhenSameEdgeStation() {
            //given

            //when
            var response = PathCommonApi.findLinePath(남부터미널역, 남부터미널역);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("출발역과 도착역이 연결되어 있지 않아 지하철 경로 탐색에 실패한다.")
        @Test
        void failFindPathWhenNotConnected() {
            //given

            //when
            var response = PathCommonApi.findLinePath(석남역, 남부터미널역);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("존재하지 않는 출발역이나 도착역을 조회하여 지하철 경로 탐색에 실패한다.")
        @Test
        void failFindPathWhenNotExist() {
            //given

            //when
            var response = PathCommonApi.findLinePath(12312L, 79L);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }
}
