package nextstep.subway.dto;

import nextstep.subway.entity.Line;
import nextstep.subway.entity.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    private LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        List<Station> stations = line.getStations();
        List<StationResponse> stationResponse = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        LineResponse lineResponse = new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stationResponse
        );
        return lineResponse;
    }
}
