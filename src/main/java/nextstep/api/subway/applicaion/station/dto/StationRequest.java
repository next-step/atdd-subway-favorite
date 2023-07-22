package nextstep.api.subway.applicaion.station.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StationRequest {
    private String name;

    public StationRequest(final String name) {
        this.name = name;
    }
}
