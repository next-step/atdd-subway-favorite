package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.station.domain.Station;
import nextstep.station.application.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Favorite favorite, Station source, Station target) {
        this.id = favorite.getId();
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
