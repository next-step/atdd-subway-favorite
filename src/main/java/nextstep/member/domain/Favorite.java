package nextstep.member.domain;

import nextstep.subway.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "source_id")
    Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    Station target;

    protected Favorite() {
    }

    public Favorite(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Favorite(Station source, Station target) {
        this.source = source;
        this.target = target;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) && Objects.equals(source, favorite.source) && Objects.equals(target, favorite.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}
