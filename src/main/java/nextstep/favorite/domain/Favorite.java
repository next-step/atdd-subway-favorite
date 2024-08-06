package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.station.entity.Station;

import javax.persistence.*;

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

    public Favorite(Long id, Member member, Station sourceStation, Station targetStation) {
        this.id = id;
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static Favorite of(final Member member, final Station sourceStation, final Station targetStation) {
        return new Favorite(null, member, sourceStation, targetStation);
    }

    public static Favorite of(final Long id, final Member member, final Station sourceStation, final Station targetStation) {
        return new Favorite(id, member, sourceStation, targetStation);
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

