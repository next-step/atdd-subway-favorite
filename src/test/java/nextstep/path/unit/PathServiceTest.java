package nextstep.path.unit;

import nextstep.common.exception.NotExistStationException;
import nextstep.common.exception.PathNotFoundException;
import nextstep.common.exception.SameStationException;
import nextstep.line.domain.Line;
import nextstep.section.domain.Section;
import nextstep.section.domain.SectionRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.path.application.dto.PathResponse;
import nextstep.station.application.dto.StationResponse;
import nextstep.path.application.PathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathServiceTest {

    private PathService pathService;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    private List<Station> 전체역;
    private List<Section> 전체구간;

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    private Section section1;
    private Section section2;
    private Section section3;
    private Section section4;

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
        전체역 = List.of(교대역, 강남역, 양재역, 남부터미널역);

        section1 = new Section(교대역.getId(), 강남역.getId(), 10L);
        section2 = new Section(강남역.getId(), 양재역.getId(), 10L);
        section3 = new Section(교대역.getId(), 남부터미널역.getId(), 2L);
        section4 = new Section(남부터미널역.getId(), 양재역.getId(), 3L);
        전체구간 = List.of(section1, section2, section3, section4);

        이호선 = new Line("2호선", "green", section1);
        신분당선 = new Line("신분당선", "red", section2);
        삼호선 = new Line("3호선", "orange", section3);
        삼호선.addSection(section4);

        stationRepository = mock(StationRepository.class);
        sectionRepository = mock(SectionRepository.class);
        pathService = new PathService(stationRepository, sectionRepository);
    }

    @Test
    @DisplayName("최단 거리와 경로를 반환한다.")
    void findShortestPath() {

        when(sectionRepository.findAll())
                .thenReturn(전체구간);
        when(stationRepository.findAll())
                .thenReturn(전체역);

        when(stationRepository.existsById(교대역.getId())).thenReturn(true);
        when(stationRepository.existsById(양재역.getId())).thenReturn(true);

        PathResponse shortestPath = pathService.getShortestPath(교대역.getId(), 양재역.getId());
        assertAll(
                () -> assertThat(shortestPath.getDistance()).isEqualTo(5L),
                () -> assertThat(shortestPath.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                        .containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName())
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외를 발생시킨다.")
    void findShortestPathSameStationException() {

        assertThrows(SameStationException.class,
                () -> pathService.getShortestPath(교대역.getId(), 교대역.getId()));
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되있지 않은 경우 예외를 발생시킨다.")
    void findShortestPathNotConnectedException() {
        var 서울역 = new Station(5L, "서울역");
        var 수원역 = new Station(6L, "수원역");
        var section = new Section(서울역.getId(), 수원역.getId(), 10L);
        var 일호선 = new Line("1호선", "blue", section);

        전체역 = List.of(교대역, 강남역, 양재역, 남부터미널역, 서울역, 수원역);
        전체구간 = List.of(section1, section2, section3, section4, section);

        when(sectionRepository.findAll())
                .thenReturn(전체구간);
        when(stationRepository.findAll())
                .thenReturn(전체역);

        when(stationRepository.existsById(서울역.getId())).thenReturn(true);
        when(stationRepository.existsById(강남역.getId())).thenReturn(true);

        assertThrows(PathNotFoundException.class,
                () -> pathService.getShortestPath(서울역.getId(), 강남역.getId()));
    }

    @Test
    @DisplayName("존재하지 않은 출발역을 조회 할 경우 예외를 발생시킨다.")
    void findShortestPathSourceNotExistException() {
        var 임시역 = 5L;

        when(sectionRepository.findAll())
                .thenReturn(전체구간);
        when(stationRepository.findAll())
                .thenReturn(전체역);

        assertThrows(NotExistStationException.class,
                () -> pathService.getShortestPath(임시역, 교대역.getId()));
    }
    @Test
    @DisplayName("존재하지 않은 도착역을 조회 할 경우 예외를 발생시킨다.")
    void findShortestPathTargetNotExistException() {
        var 임시역 = 5L;

        when(sectionRepository.findAll())
                .thenReturn(전체구간);
        when(stationRepository.findAll())
                .thenReturn(전체역);

        assertThrows(NotExistStationException.class,
                () -> pathService.getShortestPath(교대역.getId(), 임시역));
    }
    private Station findStationById(final Long id) {
        return 전체역.stream().filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
