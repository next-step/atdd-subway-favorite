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

    public FavoritePath(Station startStation, Station endStation, Member member) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public void validCheck() {
        if (startStation == null || endStation == null || member == null) {
            throw new IllegalArgumentException("즐겨찾기를 생성할 수 없습니다.");
        }
    }
}
