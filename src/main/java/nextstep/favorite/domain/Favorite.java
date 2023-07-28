package nextstep.favorite.domain;

import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_id")
    private Station source;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    protected Favorite() {}

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
