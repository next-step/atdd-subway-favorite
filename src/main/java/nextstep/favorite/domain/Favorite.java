package nextstep.favorite.domain;

import nextstep.favorite.exception.FavoriteCreationException;
import nextstep.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

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
        validate(memberId, sourceStation, targetStation);
        this.memberId = memberId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    private void validate(final Long memberId, final Station sourceStation, final Station targetStation) {
        if (Objects.isNull(memberId)) {
            throw new FavoriteCreationException("memberId can not be null");
        }
        if (Objects.isNull(sourceStation)) {
            throw new FavoriteCreationException("sourceStation can not be null");
        }
        if (Objects.isNull(targetStation)) {
            throw new FavoriteCreationException("targetStation can not be null");
        }
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
