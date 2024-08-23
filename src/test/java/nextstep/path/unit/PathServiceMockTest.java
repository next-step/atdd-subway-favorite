package nextstep.path.unit;

import nextstep.line.domain.Section;
import nextstep.line.domain.SectionRepository;
import nextstep.path.application.PathService;
import nextstep.path.application.dto.PathsResponse;
import nextstep.path.application.exception.NotAddedEndToPathsException;
import nextstep.path.application.exception.NotAddedStartToPathsException;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

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
    private StationService stationService;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(sectionRepository, stationService);
    }

    @DisplayName("최단경로 조회 함수는, 출발역과 도착역을 입력하면 최단 경로 지하철 역 목록과 총 거리를 반환한다.")
    @Test
    void findShortestPathsTest() {
        // given
        when(stationService.lookUp(강남역.getId())).thenReturn(강남역);
        when(stationService.lookUp(교대역.getId())).thenReturn(교대역);
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
        when(stationService.lookUp(구간에없는역.getId())).thenReturn(구간에없는역);
        when(stationService.lookUp(교대역.getId())).thenReturn(교대역);

        // when
        ThrowingCallable actual = () -> pathService.findShortestPaths(구간에없는역.getId(), 교대역.getId());

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
        when(stationService.lookUp(강남역.getId())).thenReturn(강남역);
        when(stationService.lookUp(구간에없는역.getId())).thenReturn(구간에없는역);

        // when
        ThrowingCallable actual = () -> pathService.findShortestPaths(강남역.getId(), 구간에없는역.getId());

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedEndToPathsException.class)
                .hasMessageContaining(String.format("도착역(%s)", 구간에없는역.getName()));
    }

    @DisplayName("경로검사 합수는, 주어진 출발역과 도착역이 정상적인 경로를 생성할 수 없으면 예외를 발생시킨다.")
    @Test
    void validateSameSourceAndTargetTest() {
        // when
        ThrowingCallable actual = () -> pathService.validatePaths(강남역.getId(), 강남역.getId());

        // then
        assertThatThrownBy(actual).isInstanceOf(SameSourceAndTargetException.class);
    }

    @DisplayName("경로검사 합수는, 주어진 출발역과 도착역이 정상적인 경로를 생성할 수 없으면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("validatePathsParams")
    void validatePathsTest(Station source, Station target, Class<? extends RuntimeException> expected) {
        // given
        List<Section> 연결_안된_구간 = List.of(강남역_양재역, 교대역_홍대역);

        when(sectionRepository.findAll()).thenReturn(연결_안된_구간);
        when(stationService.lookUp(source.getId())).thenReturn(source);
        when(stationService.lookUp(target.getId())).thenReturn(target);

        // when
        ThrowingCallable actual = () -> pathService.validatePaths(target.getId(), source.getId());

        // then
        assertThatThrownBy(actual).isInstanceOf(expected);
    }

    private static Stream<Arguments> validatePathsParams() {
        return Stream.of(
                Arguments.of(강남역, Station.of(0L, "없는역"), NotAddedStationsToPathsException.class),
                Arguments.of(강남역, 교대역, NotConnectedPathsException.class)
        );
    }
}
