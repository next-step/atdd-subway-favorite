package nextstep.favorite.dto;

import nextstep.favorite.domain.Favorite;

public class FavoriteResponse {

    private Long id;
    private Long source;
    private Long target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long source, Long target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

    public Long getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
