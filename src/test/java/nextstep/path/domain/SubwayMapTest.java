package nextstep.path.domain;

import nextstep.common.fixture.LineFactory;
import nextstep.common.fixture.SectionFactory;
import nextstep.common.fixture.StationFactory;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.path.exception.PathNotFoundException;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class SubwayMapTest {
    private final Long 강남역_Id = 1L;
    private final Long 교대역_Id = 2L;
    private final Long 양재역_Id = 3L;
    private final Long 남부터미널역_Id = 4L;
    private final Long 서울역_Id = 5L;
    private final int 교대역_강남역_distance = 5;
    private final int 강남역_양재역_distance = 10;
    private final int 교대역_남부터미널_distance = 2;
    private final int 남부터미널_양재역_distance = 3;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 서울역;
    private Station 남부터미널역;
    private SubwayMap subwayMap;

    @BeforeEach
    void setUp() {
        교대역 = StationFactory.createStation(교대역_Id, "교대역");
        강남역 = StationFactory.createStation(강남역_Id, "강남역");
        양재역 = StationFactory.createStation(양재역_Id, "양재역");
        남부터미널역 = StationFactory.createStation(남부터미널역_Id, "남부터미널역");
        서울역 = StationFactory.createStation(서울역_Id, "남부터미널역");
        final Section 교대역_강남역_구간 = SectionFactory.createSection(1L, 교대역, 강남역, 교대역_강남역_distance);
        final Section 강남역_양재역_구간 = SectionFactory.createSection(2L, 강남역, 양재역, 강남역_양재역_distance);
        final Section 교대역_남부터미널_구간 = SectionFactory.createSection(3L, 교대역, 남부터미널역, 교대역_남부터미널_distance);
        final Section 남부터미널_양재역_구간 = SectionFactory.createSection(4L, 남부터미널역, 양재역, 남부터미널_양재역_distance);
        final Line 이호선 = LineFactory.createLine(1L, "1호선", "green", 교대역_강남역_구간);
        final Line 신분당선 = LineFactory.createLine(2L, "신분당선", "red", 강남역_양재역_구간);
        final Line 삼호선 = LineFactory.createLine(3L, "2호선", "orange", 교대역_남부터미널_구간);
        삼호선.addSection(남부터미널_양재역_구간);
        subwayMap = new SubwayMap(List.of(이호선, 신분당선, 삼호선));
    }

    @Test
    @DisplayName("findShortestPath 를 통해 최단거리 경로를 반환 받을 수 있다.")
    void findShortestPathTest() {
        final Optional<Path> shortestPathOptional = subwayMap.findShortestPath(강남역, 남부터미널역);

        assertSoftly(softly -> {
            softly.assertThat(shortestPathOptional).isNotEmpty();
            final Path shortestPath = shortestPathOptional.get();
            softly.assertThat(shortestPath.getDistance()).isEqualTo(교대역_강남역_distance + 교대역_남부터미널_distance);
            softly.assertThat(shortestPath.getStations()).extracting("id")
                    .containsExactly(강남역_Id, 교대역_Id, 남부터미널역_Id);
        });
    }

    @Test
    @DisplayName("findShortestPath 를 통해 도달할 수 없는 경로를 탐색하면 PathNotFoundException 을 던진다.")
    void findShortestPathNotFoundTest() {
        assertThatThrownBy(() -> subwayMap.findShortestPath(강남역, 서울역))
                .isInstanceOf(PathNotFoundException.class);
    }
}
