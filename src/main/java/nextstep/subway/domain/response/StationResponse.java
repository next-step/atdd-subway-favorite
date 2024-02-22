package nextstep.subway.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.entity.Station;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StationResponse {
    private Long id;
    private String name;

    public StationResponse createStationResponseFromEntity(Station station) {
        this.id = station.getId();
        this.name = station.getName();

        return this;
    }
}
