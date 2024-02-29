package nextstep.core.subway.favorite.domain;

import nextstep.core.member.domain.Member;
import nextstep.core.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "MEMBER_ID")
    private Long memberId;

    @ManyToOne
    private Station sourceStation;

    @ManyToOne
    private Station targetStation;

    protected Favorite() {
    }

    public Favorite(Station source, Station target, Long memberId) {
        this.sourceStation = source;
        this.targetStation = target;
        this.memberId = memberId;
    }

    public boolean isOwn(Member member) {
        return member.isSameId(memberId);
    }

    public Station getSourceStation() {
        return this.sourceStation;
    }

    public Station getTargetStation() {
        return this.targetStation;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }
}
