package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.domain.Station;

@Entity
public class Favorite {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "source")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target")
    private Station target;

    public Favorite() {
    }

    public Favorite(final Long memberId, final Station source, final Station target) {
        this.memberId = memberId;
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
