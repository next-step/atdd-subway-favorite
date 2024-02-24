package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.subway.entity.Sections;
import nextstep.subway.entity.Station;
import nextstep.subway.service.PathFinder;

import javax.persistence.*;
import java.util.List;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    protected Favorite() {}

    public Favorite(Member member, Station sourceStation, Station targetStation, List<Sections> sectionsList) {
        verifyCreateFavorite(sourceStation, targetStation, sectionsList);

        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    private void verifyCreateFavorite(Station sourceStation, Station targetStation, List<Sections> sectionsList) {
        verifySameStation(sourceStation, targetStation);
        verifyConnectedStation(sectionsList, sourceStation, targetStation);
    }

    private void verifySameStation(Station sourceStation, Station targetStation) {
        if(sourceStation == targetStation) {
            throw new IllegalArgumentException("출발역과 도착역은 동일할 수 없다");
        }
    }

    private void verifyConnectedStation(List<Sections> sectionsList, Station sourceStation, Station targetStation) {
        PathFinder pathFinder = new PathFinder(sectionsList);
        pathFinder.getShortestPath(sourceStation, targetStation);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
