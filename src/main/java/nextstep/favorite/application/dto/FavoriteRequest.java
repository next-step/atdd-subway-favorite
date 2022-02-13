package nextstep.favorite.application.dto;

public class FavoriteRequest {

    private Long memberId;

    private Long sourceId;

    private Long targetId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

}
