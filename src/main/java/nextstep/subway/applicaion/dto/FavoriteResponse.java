package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Station;

@Getter
public class FavoriteResponse {

    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoriteResponse(final long id, final Station sourceStation, final Station targetStation) {
        this.id = id;
        this.source = new StationResponse(sourceStation);
        this.target = new StationResponse(targetStation);
    }
}
