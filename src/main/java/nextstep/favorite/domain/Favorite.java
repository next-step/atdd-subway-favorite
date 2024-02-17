package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    public Favorite() {
    }

    public Favorite(Station sourceStation, Station targetStation, Member member) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public Member getMember() {
        return member;
    }
}
