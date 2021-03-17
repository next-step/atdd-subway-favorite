package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int duration;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int duration, int distance) {
        this.stations = StationResponse.listOf(stations);
        this.duration = duration;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
