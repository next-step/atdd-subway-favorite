package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;

public class StationCreateRequest {
    private String name;

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
