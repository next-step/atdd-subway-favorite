package nextstep.core.favorite.domain;

import nextstep.core.member.domain.Member;
import nextstep.core.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Station sourceStation;

    @ManyToOne
    private Station targetStation;

    protected Favorite() {
    }

    public Favorite(Station source, Station target, Member member) {
        this.sourceStation = source;
        this.targetStation = target;
        this.member = member;
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

    public Member getMember() {
        return member;
    }
}
