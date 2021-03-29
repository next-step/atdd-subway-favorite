package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {

    }

    private FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of(Long source, Long target) {
        return new FavoriteRequest(source, target);
    }

    public Long getSource() {
        return this.source;
    }

    public Long getTarget() {
        return this.target;
    }
}
