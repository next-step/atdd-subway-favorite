package nextstep.favorite.domain;

import nextstep.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    protected Favorite() {
    }

    public Favorite(final Long memberId, final Station sourceStation, final Station targetStation) {
        this.memberId = memberId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
