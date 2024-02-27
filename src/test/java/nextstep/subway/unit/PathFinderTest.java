package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.SectionEdges;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.exception.PathException;
import nextstep.subway.unit.Fixtures.LineFixture;
import nextstep.subway.unit.Fixtures.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    private PathFinder pathFinder;
    private Station 신사역;
    private Station 강남역;
    private Station 양재역;
    private Station 양재시민의숲역;
    private Station 잠원역;
    private Station 교대역;
    private Station 북한역;
    private Station 남한역;


    @BeforeEach
    void setUp() {
        신사역 = StationFixture.station(1L, "신사역");
        강남역 = StationFixture.station(2L, "강남역");
        양재역 = StationFixture.station(3L, "양재역");
        양재시민의숲역 = StationFixture.station(4L, "양재시민의숲역");
        잠원역 = StationFixture.station(5L, "잠원역");
        교대역 = StationFixture.station(6L, "교대역");
        북한역 = StationFixture.station(7L, "북한역");
        남한역 = StationFixture.station(8L, "남한역");
        Line 신분당선 = LineFixture.line(1L, "신분당선", "RED");
        Line 삼호선 = LineFixture.line(2L, "삼호선", "ORANGE");
        Line 한국선 = LineFixture.line(3L, "한국선", "BLUE");
        신분당선.addSection(신사역, 강남역, 10);
        신분당선.addSection(강남역, 양재역, 10);
        신분당선.addSection(양재역, 양재시민의숲역, 10);
        삼호선.addSection(신사역, 잠원역, 10);
        삼호선.addSection(잠원역, 교대역, 10);
        한국선.addSection(북한역, 남한역, 10);
        SectionEdges edges = new SectionEdges(List.of(신분당선, 삼호선, 한국선));
        pathFinder = new PathFinder(edges);
    }

    /**
     * When findPath 메서드를 호출하면
     * Then 최단 경로를 리턴한다.
     */
    @Test
    void findPath() {
        // given
        Path path = pathFinder.findShortedPath(양재시민의숲역.getId(), 교대역.getId());
        // when
        List<Long> shortedPath = path.getVertexList();
        // then
        assertThat(shortedPath).containsExactly(양재시민의숲역.getId(), 양재역.getId(), 강남역.getId(),
            신사역.getId(), 잠원역.getId(), 교대역.getId());
        assertThat(path.getDistance()).isEqualTo(50);
    }

    /**
     * When 같은 출발역, 도착역으로 findPath 메서드를 호출하면
     * Then 에러가 발생한다
     */
    @Test
    void findPathWithSameStation() {
        // given
        // when
        // then
        assertThatThrownBy(() -> pathFinder.findShortedPath(양재시민의숲역.getId(), 양재시민의숲역.getId()))
            .isInstanceOf(PathException.PathSourceTargetSameException.class);
    }

    /**
     * When 존재하지 않는 출발역, 도착역으로 findPath 메서드를 호출하면
     * Then 에러가 발생한다
     */
    @Test
    void findPathWithNotExistStation() {
        // given
        // when
        // then
        assertThatThrownBy(() -> pathFinder.findShortedPath(양재시민의숲역.getId(), 9L))
            .isInstanceOf(PathException.PathNotFoundException.class);
    }

    /**
     * When 서로 연결되어 있지 않은 출발역, 도착역으로 findPath 메서드를 호출하면
     * Then 에러가 발생한다
     */
    @Test
    void findPathWithNotConnectedStation() {
        // given
        // when
        // then
        assertThatThrownBy(() -> pathFinder.findShortedPath(북한역.getId(), 잠원역.getId()))
            .isInstanceOf(PathException.SourceTargetNotConnectedException.class);
    }
}
