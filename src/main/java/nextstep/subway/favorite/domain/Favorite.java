package nextstep.subway.favorite.domain;

import nextstep.subway.config.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {}

    public Favorite(Long memberID, Long sourceStationId, Long targetStationId) {
        this(null, memberID, sourceStationId, targetStationId);
    }

    public Favorite(Long id, Long memberID, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.memberId = memberID;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
