package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.exception.SameStationsException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station target;

    public Favorite(){}

    public Favorite(Long memberId, Station source, Station target) {
        checkStationsNotSame(source, target);
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    private void checkStationsNotSame(Station source, Station target) {
        if(source.equals(target)) {
            throw new SameStationsException();
        }
    }

    public Long getId() { return id; }

    public Long getMemberId() { return memberId; }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
