package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.config.BaseEntity;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
    }

    public Favorite(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(Long loginMemberId, Long source, Long target) {
        this.memberId = loginMemberId;
        this.sourceStationId = source;
        this.targetStationId = target;
    }

    public Favorite(Long favoriteId, Long loginMemberId, Long source, Long target) {
        this.id = favoriteId;
        this.memberId = loginMemberId;
        this.sourceStationId = source;
        this.targetStationId = target;
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
