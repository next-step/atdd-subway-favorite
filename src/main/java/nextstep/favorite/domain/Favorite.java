package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.member.domain.Member;
import nextstep.subway.domain.JGraphTPathFinderImpl;
import nextstep.subway.domain.PathFinder;
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

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    public Favorite() {
    }

    public Favorite(Station sourceStation, Station targetStation, Member member) {
        validateFavorite(sourceStation, targetStation);

        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public void update(Station newStation1, Station newStation2) {
        validateFavorite(newStation1, newStation2);

        this.sourceStation = newStation1;
        this.targetStation = newStation2;
    }

    private void validateFavorite(Station sourceStation, Station targetStation) {
        if(isSameSourceAndTarget(sourceStation, targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public boolean isSameSourceAndTarget(Station sourceStation, Station targetStation) {
        return sourceStation == targetStation;
    }
}
