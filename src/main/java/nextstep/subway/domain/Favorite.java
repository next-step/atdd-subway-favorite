package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.member.domain.Member;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long sourceStationId;

    private Long targetStationId;

    public Favorite() {
    }

    public Favorite(Member member, Long sourceStationId, Long targetStationId) {
        this.member = member;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public boolean isCreatedBy(Member member) {
        return this.member.equals(member);
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
