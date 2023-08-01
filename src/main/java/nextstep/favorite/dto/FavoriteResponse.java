package nextstep.favorite.dto;

import nextstep.favorite.domain.Favorite;

public class FavoriteResponse {
    private final Long id;
    private final FavoriteStationResponse source;
    private final FavoriteStationResponse target;

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                FavoriteStationResponse.from(favorite.getSource()),
                FavoriteStationResponse.from(favorite.getTarget())
        );
    }

    public FavoriteResponse(Long id, FavoriteStationResponse source, FavoriteStationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
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
