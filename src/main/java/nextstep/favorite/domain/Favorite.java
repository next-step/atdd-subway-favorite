package nextstep.favorite.domain;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_tation_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station target;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Favorite() {
    }

    public Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public Member getMember() {
        return member;
    }

    public boolean isNotRegister(LoginMember loginMember) {
        return !this.member.getEmail().equals(loginMember.getEmail());
    }
}
