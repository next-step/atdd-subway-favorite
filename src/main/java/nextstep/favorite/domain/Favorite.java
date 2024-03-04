package nextstep.favorite.domain;

import nextstep.subway.line.Line;
import nextstep.subway.path.PathMaker;
import nextstep.subway.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    public Favorite() {}

    public Favorite(Long memberId, Station sourceStation, Station targetStation, List<Line> lines) {
        PathMaker pathMaker = new PathMaker(lines);
        pathMaker.findShortestPath(sourceStation, targetStation);

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
