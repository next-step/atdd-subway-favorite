package nextstep.path.unit;

import nextstep.common.exception.PathNotFoundException;
import nextstep.line.domain.Line;
import nextstep.section.domain.Section;
import nextstep.station.domain.Station;
import nextstep.path.application.dto.PathResponse;
import nextstep.path.domain.ShortestPathFinder;
import nextstep.path.domain.ShortestPathFinderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShortestPathFinderTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    private Section section1;
    private Section section2;
    private Section section3;
    private Section section4;

    private List<Section> 전체구간;
    private List<Station> 전체역;


    /**
     * 교대역    --- *2호선* (10) ---    강남역
     * |                                 |
     * *3호선* (2)                   *신분당선* (10)
     * |                                 |
     * 남부터미널역  --- *3호선* (3) ---   양재
     */

    @BeforeEach
    void setUp() {
        교대역 = new Station(1L, "교대역");
        강남역 = new Station(2L, "강남역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        section1 = new Section(교대역.getId(), 강남역.getId(), 10L);
        section2 = new Section(강남역.getId(), 양재역.getId(), 10L);
        section3 = new Section(교대역.getId(), 남부터미널역.getId(), 2L);
        section4 = new Section(남부터미널역.getId(), 양재역.getId(), 3L);

        이호선 = new Line("2호선", "green", section1);
        신분당선 = new Line("신분당선", "red", section2);
        삼호선 = new Line("3호선", "orange", section3);
        삼호선.addSection(section4);

        전체구간 = List.of(section1, section2, section3, section4);
        전체역 = List.of(교대역, 강남역, 양재역, 남부터미널역);
    }


    @DisplayName("최단 거리를 반환한다")
    @Test
    void findShortestPath() {

        ShortestPathFinder pathFinder = ShortestPathFinderFactory.createPathFinder(전체구간, 전체역);

        PathResponse pathResponse = pathFinder.find(교대역.getId(), 양재역.getId(), 전체역);

        assertThat(pathResponse.getDistance()).isEqualTo(5L);
    }

    @DisplayName("경로가 존재하지 않을 경우 예외를 발생시킨다.")
    @Test
    void findNotFoundPath() {

        var 서울역 = new Station(5L, "서울역");
        var 수원역 = new Station(6L, "수원역");
        new Line("1호선", "blue", new Section(서울역.getId(), 수원역.getId(), 10L));

        var section =  new Section(서울역.getId(), 수원역.getId(), 10L);
        var sections = List.of(section1, section2, section3, section4, section);
        var stations = List.of(교대역, 강남역, 양재역, 남부터미널역, 서울역, 수원역);


        ShortestPathFinder pathFinder = ShortestPathFinderFactory.createPathFinder(sections, stations);

        assertThrows(PathNotFoundException.class,
                () -> pathFinder.find(서울역.getId(), 강남역.getId(),stations));

    }

}
