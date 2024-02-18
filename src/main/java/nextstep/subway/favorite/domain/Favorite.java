package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.Station;

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

    protected Favorite() {
    }

    public Favorite(Station sourceStation,
                    Station targetStation,
                    Member member) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
    }
}
