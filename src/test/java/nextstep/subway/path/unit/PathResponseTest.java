package nextstep.subway.path.unit;

import nextstep.path.dto.PathResponse;
import nextstep.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathResponseTest {

    @DisplayName("getStationResponseList와 getDistance를 실행시킨다.")
    @Test
    void getStationResponseList_getDistance() {
        // given
        var stationResponses = List.of(
                StationResponse.of(1L, "Station1"),
                StationResponse.of(2L, "Station2")
        );
        var distance = 20.0;

        // when
        var pathResponse = new PathResponse().of(stationResponses, distance);

        // then
        assertAll(
                () -> assertEquals(stationResponses, pathResponse.getStationResponses()),
                () -> assertEquals(distance, pathResponse.getDistance())
        );

    }
}

