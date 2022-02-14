package nextstep.member.domain;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    protected Favorite() {}

    public Favorite(Long memberId, Station upStation, Station downStation) {
        this.memberId = memberId;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Favorite(Long id, Long memberId, Station upStation, Station downStation) {
        this.id = id;
        this.memberId = memberId;
        this.upStation = upStation;
        this.downStation = downStation;
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
