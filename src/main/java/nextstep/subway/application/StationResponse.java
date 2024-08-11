package nextstep.subway.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;

@RequiredArgsConstructor
@Getter
public class StationResponse {
    private final Long id;
    private final String name;

    public static StationResponse of(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
