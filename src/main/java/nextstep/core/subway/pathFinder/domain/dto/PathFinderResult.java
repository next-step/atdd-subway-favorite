package nextstep.core.subway.pathFinder.domain.dto;


import nextstep.core.subway.station.domain.Station;

import java.util.List;

public class PathFinderResult {
    private final List<Station> stations;
    private final Integer distance;

    public PathFinderResult(List<Station> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
