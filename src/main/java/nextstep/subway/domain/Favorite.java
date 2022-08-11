package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long sourceId;

    private Long targetId;

    protected Favorite() {
    }

    private Favorite(Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static Favorite of(Long memberId, Long sourceId, Long targetId) {
        return new Favorite(memberId, sourceId, targetId);
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

    public boolean isMembersFavorite(Long memberId) {
        return this.memberId == memberId;
    }
}
