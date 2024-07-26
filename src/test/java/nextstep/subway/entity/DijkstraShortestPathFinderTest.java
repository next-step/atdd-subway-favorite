package nextstep.subway.entity;

import nextstep.subway.dto.PathResponse;
import nextstep.subway.dto.StationResponse;
import nextstep.subway.exception.IllegalPathException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DijkstraShortestPathFinderTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    @BeforeEach
    void setup() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
    }

    @Test
    @DisplayName("두 역 경로조회 시 최단경로를 리턴")
    void 최단거리_조회() {
        // given
        Line 이호선 = new Line("2호선", "bg-green-600");
        이호선.addSection(교대역, 강남역, 10L);

        Line 신분당선 = new Line("신분당선", "bg-blue-600");
        신분당선.addSection(강남역, 양재역, 10L);


        Line 삼호선 = new Line("3호선", "bg-red-600");
        삼호선.addSection(교대역, 남부터미널역, 2L);
        삼호선.addSection(남부터미널역, 양재역, 3L);

        PathFinderBuilder pathFinderBuilder = DijkstraShortestPathFinder.searchBuilder()
                .addVertex(이호선.getStations())
                .addEdgeWeight(이호선.getSections().getSectionList())
                .addVertex(신분당선.getStations())
                .addEdgeWeight(신분당선.getSections().getSectionList())
                .addVertex(삼호선.getStations())
                .addEdgeWeight(삼호선.getSections().getSectionList());

        // when
        PathResponse pathResponse = pathFinderBuilder
                .setSource(교대역)
                .setTarget(양재역)
                .find();

        // then
        List<StationResponse> responseStations = pathResponse.getStations();
        assertThat(responseStations).hasSize(3);
        assertThat(responseStations.get(0).getId()).isEqualTo(교대역.getId());
        assertThat(responseStations.get(1).getId()).isEqualTo(남부터미널역.getId());
        assertThat(responseStations.get(2).getId()).isEqualTo(양재역.getId());
        assertThat(pathResponse.getDistance()).isEqualTo(5L);
    }

    @Test
    @DisplayName("경로조회 시 출발역과 도착역이 같은 경우 조회 불가능 예외 발생")
    void 출발역과_도착역이_같은_경우_예외_발생() {
        // given
        Line 이호선 = new Line("2호선", "bg-green-600");
        이호선.addSection(교대역, 강남역, 10L);

        Line 신분당선 = new Line("신분당선", "bg-blue-600");
        신분당선.addSection(강남역, 양재역, 10L);


        Line 삼호선 = new Line("3호선", "bg-red-600");
        삼호선.addSection(교대역, 남부터미널역, 2L);
        삼호선.addSection(남부터미널역, 양재역, 3L);

        PathFinderBuilder pathFinderBuilder = DijkstraShortestPathFinder.searchBuilder()
                .addVertex(이호선.getStations())
                .addEdgeWeight(이호선.getSections().getSectionList())
                .addVertex(신분당선.getStations())
                .addEdgeWeight(신분당선.getSections().getSectionList())
                .addVertex(삼호선.getStations())
                .addEdgeWeight(삼호선.getSections().getSectionList());

        // when then
        assertThatThrownBy(() -> pathFinderBuilder.setSource(교대역).setTarget(교대역).find())
                .isInstanceOf(IllegalPathException.class)
                .hasMessage("출발역과 도착역이 같은 경우 경로를 조회할수 없습니다.");
    }

    @Test
    @DisplayName("경로조회 시 출발역과 도착역이 연결이 되어 있지 않은 경우 예외 발생")
    void 출발역과_도착역이_연결_안됨_예외_발생() {
        // given
        Line 이호선 = new Line("2호선", "bg-green-600");
        이호선.addSection(교대역, 남부터미널역, 10L);

        Line 신분당선 = new Line("신분당선", "bg-blue-600");
        신분당선.addSection(강남역, 양재역, 10L);

        PathFinderBuilder pathFinderBuilder = DijkstraShortestPathFinder.searchBuilder()
                .addVertex(이호선.getStations())
                .addEdgeWeight(이호선.getSections().getSectionList())
                .addVertex(신분당선.getStations())
                .addEdgeWeight(신분당선.getSections().getSectionList());

        // when then
        assertThatThrownBy(() -> pathFinderBuilder.setSource(교대역).setTarget(양재역).find())
                .isInstanceOf(IllegalPathException.class)
                .hasMessage("출발역과 도착역이 연결되어있지 않습니다.");
    }

    @Test
    @DisplayName("경로조회 시 존재하지 않은 출발역을 조회 할 경우 예외 발생")
    void 존재하지_않는_출발역_조회_예외_발생() {
        // given
        Station 사당역 = new Station("사당역");

        Line 이호선 = new Line("2호선", "bg-green-600");
        이호선.addSection(교대역, 강남역, 10L);

        Line 신분당선 = new Line("신분당선", "bg-blue-600");
        신분당선.addSection(강남역, 양재역, 10L);


        Line 삼호선 = new Line("3호선", "bg-red-600");
        삼호선.addSection(교대역, 남부터미널역, 2L);
        삼호선.addSection(남부터미널역, 양재역, 3L);

        PathFinderBuilder pathFinderBuilder = DijkstraShortestPathFinder.searchBuilder()
                .addVertex(이호선.getStations())
                .addEdgeWeight(이호선.getSections().getSectionList())
                .addVertex(신분당선.getStations())
                .addEdgeWeight(신분당선.getSections().getSectionList())
                .addVertex(삼호선.getStations())
                .addEdgeWeight(삼호선.getSections().getSectionList());

        // when then
        assertThatThrownBy(() -> pathFinderBuilder.setSource(사당역).setTarget(양재역).find())
                .isInstanceOf(IllegalPathException.class)
                .hasMessage("출발역이 경로에 존재하지 않습니다.");
    }

    @Test
    @DisplayName("경로조회 시 존재하지 않은 도착역을 조회 할 경우 예외 발생")
    void 존재하지_않는_도착역_조회_예외_발생() {
        // given
        Station 사당역 = new Station("사당역");

        Line 이호선 = new Line("2호선", "bg-green-600");
        이호선.addSection(교대역, 강남역, 10L);

        Line 신분당선 = new Line("신분당선", "bg-blue-600");
        신분당선.addSection(강남역, 양재역, 10L);


        Line 삼호선 = new Line("3호선", "bg-red-600");
        삼호선.addSection(교대역, 남부터미널역, 2L);
        삼호선.addSection(남부터미널역, 양재역, 3L);

        PathFinderBuilder pathFinderBuilder = DijkstraShortestPathFinder.searchBuilder()
                .addVertex(이호선.getStations())
                .addEdgeWeight(이호선.getSections().getSectionList())
                .addVertex(신분당선.getStations())
                .addEdgeWeight(신분당선.getSections().getSectionList())
                .addVertex(삼호선.getStations())
                .addEdgeWeight(삼호선.getSections().getSectionList());

        // when then
        assertThatThrownBy(() -> pathFinderBuilder.setSource(교대역).setTarget(사당역).find())
                .isInstanceOf(IllegalPathException.class)
                .hasMessage("도착역이 경로에 존재하지 않습니다.");
    }
}
