package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private Long id;
    private String name;

    public static StationResponse of(Station station) {
        return new StationResponse(station);
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public StationResponse() {
    }

    public StationResponse(final Station station) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
