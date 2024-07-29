package nextstep.subway.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.domain.model.Line;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(
        Long id,
        String name,
        String color,
        List<StationResponse> stations
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        List<StationResponse> stations = line.getOrderedUnmodifiableStations().stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }
}
