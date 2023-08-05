package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Path;

public class PathResponse {

    private final List<StationData> stations;
    private final int distance;

    public PathResponse(List<StationData> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        List<StationData> stations = path.getStations().stream()
                .map(StationData::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();

        return new PathResponse(stations, distance);
    }

    public List<StationData> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
