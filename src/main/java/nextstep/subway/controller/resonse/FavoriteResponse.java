package nextstep.subway.controller.resonse;

import nextstep.subway.domain.Favorite;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;


    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = new StationResponse(favorite.getSource());
        this.target = new StationResponse(favorite.getTarget());
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
