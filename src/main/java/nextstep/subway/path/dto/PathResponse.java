package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance) {
        this.stations = StationResponse.listOf(stations);
        this.distance = distance;
    }

    public static PathResponse of(PathResult pathResult, List<Line> lines) {
        List<Station> stations = pathResult.extractStations(lines);
        int distance = pathResult.extractDistance(lines);
        return new PathResponse(stations, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
