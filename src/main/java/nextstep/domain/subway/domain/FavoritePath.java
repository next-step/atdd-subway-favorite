package nextstep.domain.subway.domain;

import nextstep.domain.member.domain.Member;
import nextstep.global.domain.BaseEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.*;


@Entity
public class FavoritePath extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "start_station_id", nullable = false)
    private Station startStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "end_station_id", nullable = false)
    private Station endStation;

    private Long memberId;

    public FavoritePath() {
    }

    public FavoritePath(Station startStation, Station endStation, Long memberId) {
        if (startStation == null || endStation == null || memberId == null) {
            throw new IllegalArgumentException("즐겨찾기를 생성할 수 없습니다.");
        }
        this.startStation = startStation;
        this.endStation = endStation;
        this.memberId = memberId;
    }


    public Long getId() {
        return id;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public Long getMemberId() {
        return memberId;
    }
}
