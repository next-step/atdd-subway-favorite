package nextstep.subway.favorite.domain;

import javax.crypto.spec.PSource;
import javax.persistence.*;

@Table(uniqueConstraints = @UniqueConstraint(columnNames={"userId", "sourceId", "targetId"}))
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long sourceId;

    private Long targetId;

    public Favorite() {}

    public Favorite(final Long userId, final Long sourceId, final Long targetId){
        this.userId = userId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
