package nextstep.subway.station.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StationTest {
    private Long 사용자 = 1L;

    @DisplayName("2자 미만인 경우 에러 발생")
    @Test
    void stationNameWithLowerThen2() {
        assertThatThrownBy(() -> new Station(사용자, "역")).isInstanceOf(InvalidStationException.class);
    }

    @DisplayName("100자 초과인 경우 에러 발생")
    @Test
    void stationNameWithOverThen100() {
        assertThatThrownBy(() -> new Station(사용자,
                "세상에서제일긴이름의역세상에서제일긴이름의역세상에서제일긴이름의역세상에서제일긴이름의역세상에서제일긴이름의역세상에서제일긴이름의역세상에서제일긴이름의역세상에서제일긴이름의역세상에서제일긴이름의역세상"
        )).isInstanceOf(InvalidStationException.class);
    }
}
