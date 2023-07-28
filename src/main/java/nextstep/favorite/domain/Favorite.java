package nextstep.favorite.domain;

import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "Favorite", fetch = FetchType.EAGER)
    private Station source;

    @OneToOne(mappedBy = "Favorite", fetch = FetchType.EAGER)
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
