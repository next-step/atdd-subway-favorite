package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id")
    private Station target;

    protected Favorite() {
    }

    private Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Long memberId, Station source, Station target) {
        return new Favorite(memberId, source, target);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
