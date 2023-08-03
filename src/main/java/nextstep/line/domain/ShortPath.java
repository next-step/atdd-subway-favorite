package nextstep.line.domain;

import nextstep.station.domain.Station;

import java.util.List;

public class ShortPath {

    List<Station> stations;
    int distance;

    public ShortPath(List<Station> stations, Double distance) {
        this.stations = stations;
        this.distance = distance.intValue();
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
