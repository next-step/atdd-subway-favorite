package nextstep.subway.path.application;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        Long 사용자 = 1L;
        교대역 = new Station(사용자, "교대역");
        강남역 = new Station(사용자, "강남역");
        양재역 = new Station(사용자, "양재역");
        남부터미널역 = new Station(사용자, "남부터미널역");

        신분당선 = new Line(사용자, "신분당선", "red");
        이호선 = new Line(사용자, "2호선", "red");
        삼호선 = new Line(사용자, "3호선", "red");

        ReflectionTestUtils.setField(교대역, "id", 1L);
        ReflectionTestUtils.setField(강남역, "id", 2L);
        ReflectionTestUtils.setField(양재역, "id", 3L);
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);

        신분당선.addSection(강남역, 양재역, 3, 10);
        이호선.addSection(교대역, 강남역, 3, 10);
        삼호선.addSection(교대역, 남부터미널역, 5, 10);
        삼호선.addSection(남부터미널역, 양재역, 5, 10);
    }

    @Test
    void findPath() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        PathFinder pathFinder = new PathFinder();

        // when
        PathResult pathResult = pathFinder.findPath(lines, 교대역.getId(), 양재역.getId(), PathType.DISTANCE);

        // then
        List<Long> expected = Lists.newArrayList(교대역.getId(), 강남역.getId(), 양재역.getId());
        assertThat(pathResult.getStationIds()).containsExactlyElementsOf(expected);
    }
}
