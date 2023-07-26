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
public class LineRetrieveResponse {
    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public static LineRetrieveResponse from(Line line) {
        List<Station> stationsInSections = line.getStations();
        List<StationResponse> stationResponses = stationsInSections.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return LineRetrieveResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponses)
                .build();
    }
}
