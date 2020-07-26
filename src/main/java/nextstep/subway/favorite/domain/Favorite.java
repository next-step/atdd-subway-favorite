package nextstep.subway.favorite.domain;

import nextstep.subway.config.BaseEntity;
import nextstep.subway.member.domain.Member;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;

    @ManyToOne
    private Member member;

    public Favorite() {
    }

    public Favorite(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(Long sourceStationId, Long targetStationId, Member member) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.member = member;
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

    public boolean isOwnedBy(Member member) {
        return this.member.equals(member);
    }
}
