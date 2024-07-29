package nextstep.subway.line.domain;

import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.IllegalDistanceValueException;
import nextstep.subway.exception.NotSameUpAndDownStationException;
import nextstep.subway.station.StationFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionTest {

    @Test
    @DisplayName("구간의 길이는 0이하 값이 올 수 없습니다.")
    void zeroDistance() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> Section.firstSection(StationFixtures.FIRST_UP_STATION, StationFixtures.FIRST_DOWN_STATION, 0L))
                .isInstanceOf(IllegalDistanceValueException.class)
                .hasMessage(SubwayErrorMessage.ILLEGAL_DISTANCE_VALUE.getMessage());
    }

    @DisplayName("상행역과 하행역은 같으면 안됩니다.")
    @Test
    void upDownDiff() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> Section.firstSection(StationFixtures.FIRST_UP_STATION, StationFixtures.FIRST_UP_STATION, 10L))
                .isInstanceOf(NotSameUpAndDownStationException.class);
    }

    @ParameterizedTest(name = "기존 Section 의 구간 길이 이상으로 구간을 줄일 수 없습니다.")
    @ValueSource(longs = {5, 6})
    void decreaseDistanceBig(long distance) {
        // given
        Section section = Section.firstSection(StationFixtures.FIRST_UP_STATION, StationFixtures.FIRST_DOWN_STATION, 5L);
        // when
        // then
        Assertions.assertThatThrownBy(() -> section.decreaseDistance(distance))
                .isInstanceOf(IllegalDistanceValueException.class)
                .hasMessage(SubwayErrorMessage.LARGE_DISTANCE_THAN_CURRENT_SECTION.getMessage());
    }
}