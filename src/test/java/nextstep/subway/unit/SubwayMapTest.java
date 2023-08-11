package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {

    private static Station 서울역 = new Station("서울역");
    private Station 용산역 = new Station("용산역");
    private static Station 교대역 = new Station("교대역");
    private static Station 양재역 = new Station("양재역");
    private Station 강남역 = new Station("강남역");
    private Station 남부터미널역 = new Station("남부터미널역");

    private Line 일호선 = new Line("1호선", "blue", 서울역, 용산역, 10);
    private Line 이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
    private Line 삼호선 = new Line("3호선", "orange", 교대역, 남부터미널역, 2);
    private Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);

    public static Stream<Arguments> providedStations() {
        return Stream.of(
                Arguments.of(교대역, 양재역, true),
                Arguments.of(서울역, 교대역, false)
        );
    }

    @ParameterizedTest
    @DisplayName("경로 존재 여부 확인 테스트")
    @MethodSource("providedStations")
    void hasPath(Station upStation, Station downStation, boolean expected) {
        assertThat(new SubwayMap(List.of(일호선, 이호선, 삼호선, 신분당선)).hasPath(upStation, downStation)).isEqualTo(expected);
    }
}
