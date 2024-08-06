package nextstep.path.application;

import nextstep.path.exceptions.PathNotFoundException;
import nextstep.path.payload.SearchPathRequest;
import nextstep.path.repository.PathResolver;
import nextstep.station.domain.Station;
import nextstep.station.exception.NonExistentStationException;
import nextstep.station.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PathQueryServiceTest {

    private StationRepository stationRepository;

    private PathQueryService pathQueryService;

    private PathResolver pathResolver;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

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
        전체역 = List.of(교대역, 강남역, 양재역, 남부터미널역);

        stationRepository = mock(StationRepository.class);
        pathResolver = mock(PathResolver.class);
        pathQueryService = new PathQueryService(stationRepository, pathResolver);
    }


    @DisplayName("역이 존재하지 않으면 에러를 반환한다")
    @Test
    void whenStationNonExistThenThrow() {
        Long 없는역 = -1L;
        SearchPathRequest request = new SearchPathRequest(교대역.getId(), 없는역);
        when(stationRepository.findByIdIn(List.of(request.getSource(), request.getTarget())))
                .thenReturn(List.of(교대역));

        assertThrows(NonExistentStationException.class,
                () -> pathQueryService.getShortestPath(request.getSource(), request.getTarget()));
    }

    @Test
    @DisplayName("최단 거리와 경로가 없는 경우 에러를 반환한다")
    void whenShowShortestPath() {
        SearchPathRequest request = new SearchPathRequest(교대역.getId(), 양재역.getId());

        when(stationRepository.findByIdIn(anyCollection()))
                .thenAnswer(invocation -> {
                    Collection<Long> ids = invocation.getArgument(0);
                    return ids.stream()
                            .map(this::findStationById)
                            .collect(Collectors.toList());
                });

        when(pathResolver.get(request.getSource(), request.getTarget()))
                .thenReturn(Optional.empty());

        assertThrows(PathNotFoundException.class, () ->
                pathQueryService.getShortestPath(request.getSource(), request.getTarget())
        );
    }

    private Station findStationById(final Long id) {
        return 전체역.stream().filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
