package nextstep.path.payload;

import nextstep.station.payload.StationResponse;

import java.util.List;

public class ShortestPathResponse {
    private final List<StationResponse> stations;
    private final Long distance;

    public ShortestPathResponse(final List<StationResponse> stations, final Long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }
}
