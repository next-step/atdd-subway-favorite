package nextstep.path.presentation.dto;

import java.util.List;
import nextstep.station.presentation.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public PathResponse(List<StationResponse> stations) {}

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<StationResponse> stations, int distance) {
        return new PathResponse(stations, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
