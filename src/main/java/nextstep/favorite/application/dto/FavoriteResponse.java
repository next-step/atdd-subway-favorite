package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.station.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = new StationResponse(favorite.getSourceStation());
        this.target = new StationResponse(favorite.getTargetStation());
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
