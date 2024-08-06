package nextstep.path.application.dto;

import nextstep.station.application.dto.StationResponse;

import java.util.List;

public class PathsResponse {
    private int distance;
    private List<StationResponse> stations;

    public PathsResponse() {
    }

    public PathsResponse(int distance, List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
