package nextstep.subway.domain.path.dto;

import nextstep.subway.domain.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;

    public PathResponse(List<StationResponse> stations, Long distance) {
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
