package nextstep.domain.subway.domain;

import nextstep.domain.member.domain.Member;
import nextstep.global.domain.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.*;


@Entity
public class FavoritePath extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station startStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station endStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public FavoritePath() {
    }

    public FavoritePath(Station startStation, Station endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
    }
}
