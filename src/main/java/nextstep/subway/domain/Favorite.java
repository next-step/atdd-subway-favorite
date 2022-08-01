package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station target;

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    protected Favorite() {
    }

    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }
}
