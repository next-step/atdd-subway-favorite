package nextstep.subway.favorite.domain;

import javax.crypto.spec.PSource;
import javax.persistence.*;

@Table(uniqueConstraints = @UniqueConstraint(columnNames={"memberId", "sourceId", "targetId"}))
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long sourceId;

    private Long targetId;

    public Favorite() {}

    public Long getMemberId() {
        return memberId;
    }

    public Favorite(final Long memberId, final Long sourceId, final Long targetId){
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
