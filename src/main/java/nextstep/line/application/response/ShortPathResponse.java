package nextstep.line.application.response;

import nextstep.line.domain.ShortPath;
import nextstep.station.application.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ShortPathResponse {

    private List<StationResponse> stations;
    private int distance;

    private ShortPathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static ShortPathResponse of(ShortPath shortPath) {
        return new ShortPathResponse(
                shortPath.getStations().stream().map(StationResponse::of).collect(Collectors.toList()),
                shortPath.getDistance()
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
