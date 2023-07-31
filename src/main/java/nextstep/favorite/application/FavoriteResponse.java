package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponse {

    private long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        Long id = favorite.getId();
        StationResponse source = StationResponse.of(favorite.getSource());
        StationResponse target = StationResponse.of(favorite.getTarget());
        return new FavoriteResponse(id, source, target);
    }

    public long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
