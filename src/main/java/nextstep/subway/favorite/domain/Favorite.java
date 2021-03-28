package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    private Long id;

    private Long memberId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "target_station_id")
    private Station target;

    public Favorite() {

    }

    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }
}
