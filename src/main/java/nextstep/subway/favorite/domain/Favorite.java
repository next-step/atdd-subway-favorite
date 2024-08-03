package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.member.domain.Member;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Favorite() {
    }

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public boolean isSameMember(Member member) {
        return this.member.equals(member);
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
