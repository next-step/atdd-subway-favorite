package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;

public class FavoriteResponse {
    private Long id;

    public FavoriteResponse(Long id) {
        this.id = id;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId());
    }

    public Long getId() {
        return id;
    }
}
