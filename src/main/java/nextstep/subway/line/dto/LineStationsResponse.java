package nextstep.subway.line.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.Station;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class LineStationsResponse {

    private Long id;
    private String name;

    public LineStationsResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }
}
