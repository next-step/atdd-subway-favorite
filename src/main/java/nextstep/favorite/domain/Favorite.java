package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.Station;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long source;
    private Long target;

    protected Favorite() {
    }

    public Favorite(Station source, Station target) {
        this.source = source.getId();
        this.target = target.getId();
    }

    public static Favorite of(Station source, Station target) {
        return new Favorite(source, target);
    }

    public Long getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
