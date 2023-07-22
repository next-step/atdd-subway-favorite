package subway.line.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LineResponse {
    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static LineResponse from(Line line) {
        List<Station> stationsInSections = line.getStations();
        List<StationResponse> stationResponses = stationsInSections.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponses)
                .build();
    }
}
