package nextstep.subway.domain.favourite;

import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;

@Entity
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    public Favourite() {

    }

    private Favourite(Member member, Station upStation, Station downStation) {
        this.member = member;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static Favourite of(Member member, Station upStation, Station downStation) {
        return new Favourite(member, upStation, downStation);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
