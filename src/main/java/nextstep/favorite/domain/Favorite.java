package nextstep.favorite.domain;

import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "source")
    private Station source;

    @OneToOne
    @JoinColumn(name = "target")
    private Station target;

    protected Favorite() {

    }

    private Favorite(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public static Favorite register(Station source, Station target) {
        return new Favorite(source, target);
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
