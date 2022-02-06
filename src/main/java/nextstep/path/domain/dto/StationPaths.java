package nextstep.path.domain.dto;

import java.util.List;

import lombok.Getter;
import nextstep.line.domain.Distance;
import nextstep.station.domain.Station;

@Getter
public class StationPaths {
    private final List<Station> stations;
    private final Distance distance;

    public StationPaths(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }
}
