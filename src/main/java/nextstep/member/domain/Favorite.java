package nextstep.member.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long sourceId;
    private Long targetId;

    public Favorite(Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Favorite() {

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

    public boolean isCreatedMember(Member memberB) {
        return this.memberId.equals(memberB.getId());
    }
}
