package nextstep.subway.path.application.dto;

import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations = new ArrayList<>();
    private Long distance;

    private PathResponse(List<StationResponse> stations, Long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<Station> stations, Long distance) {
        List<StationResponse> collect = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new PathResponse(collect, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }
}
