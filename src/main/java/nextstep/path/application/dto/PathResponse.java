package nextstep.path.application.dto;

import nextstep.path.domain.Path;
import nextstep.station.application.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private int distance;
    private List<StationResponse> stations;

    public PathResponse() {
    }

    public PathResponse(final int distance, final List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static PathResponse from(final Path path) {
        return new PathResponse(
                path.getDistance(),
                path.getStations().stream().map(StationResponse::from).collect(Collectors.toList())
        );
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
