package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Station;

@Getter
@AllArgsConstructor
public class StationResponse {
    private Long id;
    private String name;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
