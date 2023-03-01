package nextstep.subway.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Station source;

    @OneToOne
    private Station target;

    public Favorite() {
    }

    public Favorite(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }
}
