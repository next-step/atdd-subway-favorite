package nextstep.favorites.domain;

import lombok.Getter;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
@Getter
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station target;

    public Favorites() {
    }

    public Favorites(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Favorites(Station source, Station target) {
        this.source = source;
        this.target = target;
    }
}
