package nextstep.path.domain;

import nextstep.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;

    public Path(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }


    public int getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
