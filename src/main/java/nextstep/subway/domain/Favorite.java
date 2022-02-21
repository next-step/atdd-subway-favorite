package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;
    @ManyToOne
    @JoinColumn(name = "target_id")
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
