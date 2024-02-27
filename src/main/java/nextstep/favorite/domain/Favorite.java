package nextstep.favorite.domain;

import nextstep.subway.line.Line;
import nextstep.subway.path.PathFinder;
import nextstep.subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    public Favorite() {
    }

    public Favorite(Long memberId, Station sourceStation, Station targetStation, List<Line> lines) {
        this(null, memberId, sourceStation, targetStation, lines);
    }

    public Favorite(Long id, Long memberId, Station sourceStation, Station targetStation, List<Line> lines) {
        isConnected(lines, sourceStation, targetStation);

        this.id = id;
        this.memberId = memberId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    private void isConnected(List<Line> lines, Station sourceStation, Station targetStation) {
        new PathFinder(lines).isConnected(sourceStation, targetStation);
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
