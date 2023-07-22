package nextstep.api.subway.applicaion.station.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.api.subway.domain.station.Station;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse toResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static List<StationResponse> toResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }
}
