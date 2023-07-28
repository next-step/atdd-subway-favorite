package nextstep.favorite.domain;

import nextstep.member.domain.Member;

import javax.persistence.*;

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

    protected Favorite() {
    }

    public Favorite(Member member, Long source, Long target) {
        this.member = member;
        this.sourceStationId = source;
        this.targetStationId = target;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
