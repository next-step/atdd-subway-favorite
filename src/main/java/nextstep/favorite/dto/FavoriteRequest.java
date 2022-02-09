package nextstep.favorite.dto;

import nextstep.favorite.domain.Favorite;

public class FavoriteRequest {

    private Long sourceId;
    private Long targetId;

    public FavoriteRequest() {
    }

    private FavoriteRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static FavoriteRequest of(Long sourceId, Long targetId) {
        return new FavoriteRequest(sourceId, targetId);
    }

    public Favorite toEntity() {
        return Favorite.of(sourceId, targetId);
    }
}
