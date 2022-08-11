package nextstep.subway.domain;

import nextstep.subway.exception.InvalidDataException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {/*no-op*/}

    private Favorite(Long id, Long memberId, Station source, Station target) {

        if (source.equals(target)) {
            throw new InvalidDataException();
        }

        this.id = id;
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Favorite(Long memberId, Station source, Station target) {
        this(null, memberId, source, target);
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
