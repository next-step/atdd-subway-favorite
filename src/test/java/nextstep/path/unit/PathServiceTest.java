package nextstep.path.unit;

import nextstep.line.domain.LineRepository;
import nextstep.path.application.dto.PathsResponse;
import nextstep.path.application.PathService;
import nextstep.path.application.exception.NotAddedEndToPathsException;
import nextstep.path.application.exception.NotAddedStartToPathsException;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.utils.DatabaseCleanup;
import nextstep.utils.UnitTestFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테스트DB 사용한 지하철 경로 서비스 테스트")
@SpringBootTest
@Transactional
@ActiveProfiles("databaseCleanup")
public class PathServiceTest {

    private static final long 구간에없는역_ID = -1L;
    private static final String 구간에없는역_NAME = "구간에없는역";

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private PathService pathService;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute(getClass());

        Station 강남역 = stationRepository.save(UnitTestFixture.강남역);
        Station 양재역 = stationRepository.save(UnitTestFixture.양재역);
        Station 교대역 = stationRepository.save(UnitTestFixture.교대역);
        Station 홍대역 = stationRepository.save(UnitTestFixture.홍대역);

        lineRepository.save(신분당선(강남역, 양재역));
        lineRepository.save(분당선(양재역, 교대역));
        lineRepository.save(중앙선(교대역, 홍대역));
        lineRepository.save(경의선(홍대역, 강남역));
    }

    @DisplayName("최단경로 조회 함수는, 출발역과 도착역을 입력하면 최단 경로 지하철 역 목록과 총 거리를 반환한다.")
    @Test
    void findShortestPathsTest() {
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
        Station 구간에없는역 = stationRepository.save(Station.of(구간에없는역_ID, 구간에없는역_NAME));

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
        Station 구간에없는역 = stationRepository.save(Station.of(구간에없는역_ID, 구간에없는역_NAME));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> pathService.findShortestPaths(강남역.getId(), 구간에없는역.getId());

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedEndToPathsException.class)
                .hasMessageContaining(String.format("도착역(%s)", 구간에없는역.getName()));
    }
}
