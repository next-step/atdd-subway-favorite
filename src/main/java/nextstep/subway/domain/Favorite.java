package nextstep.subway.domain;

public class Favorite {
    private Long id;
    private Long memberId;
    private Long sourceId;
    private Long targetId;

    public Favorite(Long id, Long memberId, Long sourceId, Long targetId) {

        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Favorite(Long memberId, Long sourceId, Long targetId) {
        this(null, memberId, sourceId, targetId);
    }

    public Long getId() {
        return id;
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
