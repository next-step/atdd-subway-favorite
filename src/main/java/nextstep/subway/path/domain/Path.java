package nextstep.subway.path.domain;

import nextstep.subway.station.Station;

import java.util.Collections;
import java.util.List;

public class Path {

    private final List<Station> stations;
    private final long distance;

    public Path(List<Station> stations, long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(this.stations);
    }

    public long getDistance() {
        return distance;
    }
}
