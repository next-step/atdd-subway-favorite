package nextstep.subway.domain.line.dto;

import nextstep.subway.domain.station.dto.StationResponse;
import nextstep.subway.domain.line.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations = new ArrayList<>();

    public LineResponse() {
    }

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = line.getSections().stream()
                .flatMap(section -> Stream.of(new StationResponse(section.getUpwardStation()), new StationResponse(section.getDownwardStation())))
                .distinct()
                .collect(Collectors.toList());
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineResponse that = (LineResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations);
    }
}
