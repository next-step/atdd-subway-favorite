package nextstep.favorite.domain;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station sourceStation;

    @JoinColumn(name = "target_id")
    @ManyToOne
    private Station targetStation;

    protected Favorite() {
    }

    private Favorite(Long memberId, Station sourceStation, Station targetStation) {
        this.memberId = memberId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static Favorite of(Long memberId, Station sourceStation, Station targetStation) {
        return new Favorite(memberId, sourceStation, targetStation);
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
}
