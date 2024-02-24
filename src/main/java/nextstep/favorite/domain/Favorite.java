package nextstep.favorite.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.auth.AuthenticationException;
import nextstep.subway.domain.Station;

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

    private Long memberId;

    public Favorite() {
    }

    public Favorite(Station sourceStation, Station targetStation, Long memberId) {
        validateFavorite(sourceStation, targetStation);

        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.memberId = memberId;
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

    public Long getMemberId() {
        return memberId;
    }

    public void update(Station newStation1, Station newStation2) {
        validateFavorite(newStation1, newStation2);

        this.sourceStation = newStation1;
        this.targetStation = newStation2;
    }

    private void validateFavorite(Station sourceStation, Station targetStation) {
        if (sourceStation == null) {
            throw new IllegalArgumentException("출발역이 존재하지 않습니다.");
        }

        if (targetStation == null) {
            throw new IllegalArgumentException("도착역이 존재하지 않습니다.");
        }

        if(isSameSourceAndTarget(sourceStation, targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public boolean isSameSourceAndTarget(Station sourceStation, Station targetStation) {
        return sourceStation == targetStation;
    }

    public void validateToDelete(Long memberId) {
        if(!Objects.equals(this.memberId, memberId)) {
            throw new AuthenticationException();
        }
    }
}
