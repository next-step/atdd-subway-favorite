package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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


    public Favorite() {
    }

    public Favorite(Long memberId, Station source, Station target) {
        this(null, memberId, source, target);
    }

    public Favorite(Long id, Long memberId, Station source, Station target) {
        this.id = id;
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
