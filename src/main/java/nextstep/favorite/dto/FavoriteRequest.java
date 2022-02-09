package nextstep.favorite.dto;

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

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
