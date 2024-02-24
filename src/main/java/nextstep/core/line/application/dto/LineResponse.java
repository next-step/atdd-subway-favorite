package nextstep.core.line.application.dto;

import nextstep.core.station.application.dto.StationResponse;

import java.util.List;

public class LineResponse {
    private final Long id;

    private final String name;

    private final String color;

    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

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
}