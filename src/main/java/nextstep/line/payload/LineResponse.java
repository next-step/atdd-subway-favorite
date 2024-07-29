package nextstep.line.payload;


import nextstep.line.domain.Line;
import nextstep.station.domain.Station;
import nextstep.station.payload.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    private LineResponse(final Long id, final String name, final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line line, List<Station> stations) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations.stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList())
        );
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
