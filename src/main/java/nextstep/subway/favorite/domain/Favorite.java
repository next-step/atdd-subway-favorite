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
    private Long memberID;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
        this(null, null, null, null);
    }

    public Favorite(Long sourceStationId, Long targetStationId) {
        this(null, null, sourceStationId, targetStationId);
    }

    public Favorite(Long id, Long sourceStationId, Long targetStationId) {
        this(null, null, sourceStationId, targetStationId);
    }

    public Favorite(Long id, Long memberID, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.memberID = memberID;
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
