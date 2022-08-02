package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;

public class FavoriteResponse {

    private final Long id;
    private final FavoriteStationResponse source;
    private final FavoriteStationResponse target;

    public FavoriteResponse() {
        this.id = null;
        this.source = null;
        this.target = null;
    }

    public FavoriteResponse(Long id, FavoriteStationResponse source, FavoriteStationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(),
                new FavoriteStationResponse(favorite.getSource()),
                new FavoriteStationResponse(favorite.getTarget())
        );
    }

    public Long getId() {
        return id;
    }

    public FavoriteStationResponse getSource() {
        return source;
    }

    public FavoriteStationResponse getTarget() {
        return target;
    }
}
