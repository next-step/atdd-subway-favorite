package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;

public class FavoriteRequest {

    private final Long source;
    private final Long target;

    private FavoriteRequest() {
        this.source = null;
        this.target = null;
    }

    public FavoriteRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Favorite toFavorite(final long memberId, final long source, final long target) {
        return new Favorite(memberId, source, target);
    }
}
