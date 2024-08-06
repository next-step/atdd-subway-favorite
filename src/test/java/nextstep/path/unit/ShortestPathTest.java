package nextstep.path.unit;

import nextstep.line.domain.Section;
import nextstep.path.application.exception.NotAddedStationsToPathsException;
import nextstep.path.domain.ShortestPath;
import nextstep.path.application.exception.NotConnectedPathsException;
import nextstep.station.domain.Station;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.utils.UnitTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShortestPathTest {
    private static final List<Section> 열결되지않은구간 = List.of(강남역_양재역, 교대역_홍대역);

    @DisplayName("지하철역 조회 함수는, 가장 짧은 지하철 역 목록을 반환한다.")
    @Test
    void getStations() {
        // given
        ShortestPath shortestPath = ShortestPath.from(연결된구간);

        // when
        List<Station> actual =  shortestPath.getStations(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(List.of(강남역, 홍대역, 교대역));
    }

    @DisplayName("지하철역 조회 함수는, 출발역과 도착역이 연결되어있지 않으면 예외를 발생한다.")
    @Test
    void getStationsNotConnectedStationsExceptionTest() {
        // given
        ShortestPath shortestPath = ShortestPath.from(열결되지않은구간);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> shortestPath.getStations(강남역, 교대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }

    @DisplayName("거리 조회 함수는, 가장 짧은 거리를 반환한다.")
    @Test
    void getDistance() {
        // given
        ShortestPath shortestPath = ShortestPath.from(연결된구간);

        // when
        int actual =  shortestPath.getDistance(강남역, 교대역);

        // then
        assertThat(actual).isEqualTo(DISTANCE_6 + DISTANCE_7);
    }

    @DisplayName("거리 조회 함수는, 출발역과 도착역이 연결되어있지 않으면 예외를 발생한다.")
    @Test
    void getDistanceNotConnectedStationsExceptionTest() {
        // given
        ShortestPath shortestPath = ShortestPath.from(열결되지않은구간);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> shortestPath.getDistance(강남역, 교대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }

    @DisplayName("포함 확인 함수는, 주어진 출발 역 또는 도착 역이 경로에 포함되어 있지 않으면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("validateContainsParams")
    void validateContainsTest(Station start, Station end, String expectedMessage) {
        // given
        ShortestPath shortestPath = ShortestPath.from(연결된구간);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> shortestPath.validateContains(start, end);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotAddedStationsToPathsException.class)
                .hasMessageContaining(expectedMessage);
    }

    private static Stream<Arguments> validateContainsParams() {
        Station 포함되지_않은_역 = Station.of(-1L, "포함되지 않은 역");
        return Stream.of(
                Arguments.of(포함되지_않은_역, 강남역, String.format("출발역(%s)", 포함되지_않은_역.getName())),
                Arguments.of(강남역, 포함되지_않은_역, String.format("도착역(%s)", 포함되지_않은_역.getName()))
        );
    }

    @DisplayName("연결 확인 함수는, 주어진 출발 역과 도착 역이 연결되지 않으면 예외를 발생시킨다.")
    @Test
    void validateConnectedTest() {
        // given
        ShortestPath shortestPath = ShortestPath.from(열결되지않은구간);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> shortestPath.validateConnected(강남역, 교대역);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotConnectedPathsException.class);
    }
}
