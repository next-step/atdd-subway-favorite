package nextstep.subway.unit.support;

import nextstep.subway.domain.Station;

public class StationMockFactory {
    public static Station station(long id) {
        return Station.builder()
                      .id(id)
                      .name("station1").build();
    }
}
