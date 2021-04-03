package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_id")
    private Station source;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_id")
    private Station target;

    private Long memberId;

    protected Favorite() {
    }

    public Favorite(Station source, Station target, Long memberId) {
        this.source = source;
        this.target = target;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
