package nextstep.core.pathFinder.application.dto;


import nextstep.core.station.domain.Station;

import java.util.List;

public class PathFinderResponse {
    private final List<Station> stations;
    private final Integer distance;

    public PathFinderResponse(List<Station> stations, Integer distance) {
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
