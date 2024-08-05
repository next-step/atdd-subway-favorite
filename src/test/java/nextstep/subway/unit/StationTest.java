package nextstep.subway.unit;

import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {
    @Test
    @DisplayName("지하철 역 생성")
    void getStationName() {
        // given
        String stationName = "신논현역";
        Station station = new Station(stationName);

        // when
        String retrievedName = station.getName();

        // then
        assertThat(retrievedName).isEqualTo(stationName);
    }
}
