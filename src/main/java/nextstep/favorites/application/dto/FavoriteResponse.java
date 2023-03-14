package nextstep.favorites.application.dto;

import nextstep.favorites.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponse {
    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        StationResponse source = StationResponse.of(favorite.getSourceStation());
        StationResponse target = StationResponse.of(favorite.getTargetStation());

        return new FavoriteResponse(favorite.getId(), source, target);
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
