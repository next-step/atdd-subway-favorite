package nextstep.path.unit;

import nextstep.path.application.dto.PathsResponse;
import nextstep.line.domain.SectionRepository;
import nextstep.path.application.PathService;
import nextstep.path.application.exception.NotAddedEndToPathsException;
import nextstep.path.application.exception.NotAddedStartToPathsException;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("Mock을 활용한 지하철 경로 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {

    private static final long 구간에없는역_ID = -1L;
    private static final String 구간에없는역_NAME = "구간에없는역";

    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(sectionRepository, stationRepository);
    }

    @DisplayName("최단경로 조회 함수는, 출발역과 도착역을 입력하면 최단 경로 지하철 역 목록과 총 거리를 반환한다.")
    @Test
    void findShortestPathsTest() {
        // given
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));
        when(sectionRepository.findAll()).thenReturn(연결된구간);

        // when
        PathsResponse pathsResponse = pathService.findShortestPaths(강남역.getId(), 교대역.getId());

        // then
        assertThat(pathsResponse.getStations()).isEqualTo(createStationResponse(강남역, 홍대역, 교대역));
        assertThat(pathsResponse.getDistance()).isEqualTo(13);
    }

    @DisplayName("최단경로 조회 함수는, 구간에 등록되지 않은 역이 출발역인 경우 예외를 발생한다.")
    @Test
    void findShortestPathsNotAddedStartToSectionExceptionTest() {
        // given
        Station 구간에없는역 = Station.of(구간에없는역_ID, 구간에없는역_NAME);

        when(sectionRepository.findAll()).thenReturn(연결된구간);
        when(stationRepository.findById(구간에없는역.getId())).thenReturn(Optional.of(구간에없는역));
        when(stationRepository.findById(교대역.getId())).thenReturn(Optional.of(교대역));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> pathService.findShortestPaths(구간에없는역.getId(), 교대역.getId());

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedStartToPathsException.class)
                .hasMessageContaining(String.format("출발역(%s)", 구간에없는역.getName()));
    }

    @DisplayName("최단경로 조회 함수는, 구간에 등록되지 않은 역이 도착역인 경우 예외를 발생한다.")
    @Test
    void findShortestPathsNotAddedEndToSectionExceptionTest() {
        // given
        Station 구간에없는역 = Station.of(구간에없는역_ID, 구간에없는역_NAME);

        when(sectionRepository.findAll()).thenReturn(연결된구간);
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(구간에없는역.getId())).thenReturn(Optional.of(구간에없는역));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> pathService.findShortestPaths(강남역.getId(), 구간에없는역.getId());

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedEndToPathsException.class)
                .hasMessageContaining(String.format("도착역(%s)", 구간에없는역.getName()));
    }
}
