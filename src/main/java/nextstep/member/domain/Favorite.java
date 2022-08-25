package nextstep.member.domain;

import nextstep.member.domain.exception.FavoriteException;
import nextstep.subway.domain.Station;

import javax.persistence.*;

import static nextstep.member.domain.exception.FavoriteException.SAME_SOURCE_AND_TARGET;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {
    }

    public Favorite(Station source, Station target) {
        if (source.isSame(target)) {
            throw new FavoriteException(SAME_SOURCE_AND_TARGET);
        }
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
}
