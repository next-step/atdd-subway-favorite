package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationData> stations;

    public LineResponse(Long id, String name, String color, List<StationData> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        List<StationData> stations = line.getStations().stream()
                .map(StationData::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
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

    public List<StationData> getStations() {
        return stations;
    }
}

